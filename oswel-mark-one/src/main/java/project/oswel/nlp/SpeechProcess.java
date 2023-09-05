package project.oswel.nlp;

import project.oswel.knowledgebase.WeatherAPI;
import project.oswel.knowledgebase.NewsAPI;
import project.oswel.knowledgebase.JWiki;
import project.oswel.time.DateTime;
import project.oswel.time.WeekDay;
import java.util.logging.Logger;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Stack;

/**
 * This class processes the user input collected from the speech 
 * recognition methods and then process the user input using the 
 * Keras NLP model to determine the category the user input belongs
 * which returns the appropriate response by gathering information
 * collected from various API's.
 * @author John Santos
 */
public class SpeechProcess {

    private static HashMap<String, String> countriesMapping = DateTime
                                                            .getCountryCodes();
    private static final Logger LOGGER = Logger
                                            .getLogger(
                                                SpeechProcess.class
                                                            .getName());

    private ChatKeras chatKeras;
    private WeatherAPI weatherInfo;
    private NewsAPI newsAPI;
    private JWiki jwiki;
    private NER ner;

	/**
	 * Retrieves the chatKeras object.
	 * @return ChatKeras object that handles categorizing speech.
	 */
	public ChatKeras getChatKeras() {
		return this.chatKeras;
	}
    
    /**
	 * Creates the speech interpreter object but requires the 
     * license file contents containing API keys needed
	 * to fetch from various APIs to process the response.
	 * @param oswelLicense JSONObject containing the API keys.
	 * @param resources JSONObject containing file names for the model and 
     *                  resources.
     * @param endpoints JSONObject containing endpoints to the APIs.
     * @param cityLocation The current city location to fetch weather response.
	 */
    public SpeechProcess(
        JSONObject oswelLicense, 
        JSONObject resources, 
        JSONObject endpoints, 
        String cityLocation
    ) {
        LOGGER.info("Loading model resources...");
        this.chatKeras =  new ChatKeras(
							resources.getString("oswelNLPModel"),
							resources.getString("wordsFile"),
							resources.getString("classesFile"),
							resources.getString("intentsFile"));

		this.weatherInfo = new WeatherAPI(
			oswelLicense.getString("visualcrossing"),
			endpoints.getString("weather"));
        this.getWeatherWeek(cityLocation);

		this.ner = new NER(
			resources.getString("locationNER"), 
			resources.getString("posNER"));

		this.newsAPI = new NewsAPI(
			oswelLicense.getString("newsapi"),
			endpoints.getString("currentEvents"));
		
		this.jwiki = new JWiki(endpoints.getString("wikipedia"));	
    }

    /**
	 * This method collected the nouns in a given sentence.
	 * @param sentence The sentence to collect the nouns.
	 * @return The indices of each word in the sentence identified to be nouns.
	 */
	private Stack<Integer> collectNouns(String sentence) {
		Stack<Integer> nouns = new Stack<Integer>();
		String[] tags = ner.tagSentence(sentence);
		for (int i=0; i<tags.length; i++) {
			if (tags[i].equalsIgnoreCase("NN")) {
				nouns.push(i);
			}
		}
		return nouns;
	}
    
    /**
     * Checks if a day is present in the user prompt. For example "Tuesday".
     * @param userResponse The string prompt given by the user.
     * @return The day if present in the prompt, otherwise "None".
     */
    private String getDayInUserPrompt(String userResponse) {
        String[] daysOfWeek = WeekDay.getDaysOfWeek();
        String day = "None";
		ArrayList<String> userWords = new ArrayList<String>(
			Arrays.asList(userResponse.split(" ")));
		for(int i=0; i<daysOfWeek.length; i++) {
			if (userWords.contains(daysOfWeek[i])) {
				day = daysOfWeek[i];
				break;
			} 
		}
        return day;
    }

    /**
     * This method updates the weather information for the current week based
     * on the city provided.
     * @param cityLocation The city to get the weather information for the 
     *                     current week. 
     */
    private void getWeatherWeek(String cityLocation) {
        String[] weekStartEndDates = DateTime.getStartEndWeekDates();
        weatherInfo.timelineRequestHttpClient(
            weekStartEndDates[0], weekStartEndDates[1], 
            cityLocation
        );
    }

    private String formatWeatherResponse(
        String location, String day, JSONObject dayValue
    ) {
		double maxTemp = dayValue.getDouble("tempmax");
		double minTemp = dayValue.getDouble("tempmin");
		double temp = dayValue.getDouble("temp");
		double precip = dayValue.getDouble("precipprob");
		String description = dayValue.getString("description");

		return "In " + location + " %s "
				+ String.format("on %s is %.1f degrees celsius with %s. ", 
							day, temp, description)
				+ String.format(
					"A high of %.1f degrees and ", 
							maxTemp)
				+ String.format(
						"a low of %.1f degrees celsius. ", 
						minTemp)  
				+ String.format(
						"There is a %.1f percent chance of rain", 
						precip);
    }

	/**
	 * This method processes the weather response to detect if the user
	 * specified the location to fetch weather information from. Or if the
	 * user specified the day to fetch the weather. 
	 * @param userResponse The response of the user to process.
	 * @return The processed string to output (String).
	 */
	private String processWeatherResponse(String userResponse) {
		String[] locations = ner.findLocation(userResponse);
		if (locations.length > 0) {
			if (!locations[0].equalsIgnoreCase(weatherInfo.getLocation())) {
                this.getWeatherWeek(locations[0]);
			}
		}

        JSONObject dayValue = new JSONObject();
		String day = this.getDayInUserPrompt(userResponse);
        if (day.equals("None")) {
			day = DateTime.getCurrentDay();
			dayValue = weatherInfo.getWeatherInfoDay(day);
		} else {
            dayValue = weatherInfo.getWeatherInfoDay(day);
        }	
		return this.formatWeatherResponse(
            weatherInfo.getLocation(), day, dayValue);
	}

	/**
	 * This method process the time response to detect if the user 
	 * specified the location to parse the time. Otherwise, by default it
	 * parses the time in Calgary.
	 * @param userResponse The response from the user to process.
	 * @return The processed string containing the time information for a
	 * 		   particular city.
	 */
	private String processTimeResponse(String userResponse) {
		String[] locations = ner.findLocation(userResponse);
		String time = "";
		String location = "";
		if (locations.length > 0) {
			location = locations[0];
			time = DateTime.getCurrentTimeCity(location);
		} else {
			location = "Calgary";
			time = DateTime.getCurrentTime();
		}
		return "In " + location + " %s " + time;
	}

	/**
	 * This method processes the date response which returns the current
	 * date. 
	 * @param userResponse The user response to process.
	 * @return The processed response containing the current date (String).
	 */
	private String processDateResponse(String userResponse) {
		return "%s " + DateTime.getCurrentDate();
	}	

	/**
	 * This method processes the current events/news response to return
	 * the top headlines in the US or by specific category. 
	 * @param userResponse The user response to process.
	 * @return The processed response containing the fetched current evernts
	 * 		   information.
	 */
	private String processNewsResponse(String userResponse) {
		String[] words = userResponse.split(" ");
		String[] summary = new String[3];
		Stack<Integer> nouns = this.collectNouns(userResponse);
		if (nouns.size() >= 2) {
			String topic = words[nouns.pop()];
			summary = newsAPI.getNewsByTopic(topic);
		} else {
			String[] locations = ner.findLocation(userResponse);
			String countryCode = "us";
			if (locations.length > 0) {
				String location = locations[0].toLowerCase();
				if (countriesMapping.containsKey(location)) {
					countryCode = countriesMapping.get(location);
				}
			}
			summary = newsAPI.getNewsTopHeadline(countryCode);
		}
		return "%s " + summary[0] + " " + summary[1] + " " + summary[2];
	}

	/**
	 * This method searches for wikipedia for the high value term or topic
	 * detected in the user's response.
	 * @param userResponse The user response to process.
	 * @param oswelResponse The random generated response returned by the 
	 * 						NLP model
	 * @return The wikipedia information based on the topic specified. 
	 */
	private String processWikipediaResponse(String userResponse) {		
		String[] words = userResponse.split(" ");
		String description = "";
		Stack<Integer> nouns = this.collectNouns(userResponse);
		if (nouns.size() >= 2) {
			String topic = words[nouns.pop()];
			description = jwiki.getData(topic);
		} 
		return description;
	}

	/**
	 * This method processes the response to get the category for which
	 * the user response belongs and attempts to output the appropriate 
	 * response by gathering information from various API.
	 * @param userResponse The user response to process.
	 * @return The processed response as an attempt to answer/satisfy the
	 * user response. 
	 */
	public String[] processResponse(String userResponse) {
		JSONObject oswelResponse = chatKeras.getRandomResponse(userResponse);
		String category = oswelResponse.getString("category");
		String oswelMessage = oswelResponse.getString("response");
		String[] finalResponse = new String[2];

		if (category.equalsIgnoreCase("weather")) {
			oswelMessage = String.format(
								this.processWeatherResponse(userResponse), 
								oswelMessage);
		} else if (category.equalsIgnoreCase("time")) {
			oswelMessage = String.format(
								this.processTimeResponse(userResponse), 
								oswelMessage);
		} else if (category.equalsIgnoreCase("date")) {
			oswelMessage = String.format(
								this.processDateResponse(userResponse), 
								oswelMessage);
		} else if (category.equalsIgnoreCase("events")) {
			oswelMessage = String.format(
								this.processNewsResponse(userResponse), 
								oswelMessage);
		} else if (category.equalsIgnoreCase("confirmation")) {
			String wikiMessage = this.processWikipediaResponse(userResponse);
			if (wikiMessage != "") {
				category = "general";
				oswelMessage = wikiMessage;
				// TODO: ChatGPT response needs to be implemented here.
			}
		} else {
			;
		}
		finalResponse[0] = category;
		finalResponse[1] = oswelMessage;
		return finalResponse;
	}
}
