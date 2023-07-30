package project.oswel.utilities;

import project.oswel.exceptions.WeatherFetchFailedException;
import project.oswel.knowledgebase.currentevents.NewsAPI;
import project.oswel.exceptions.InvalidAPIKeyException;
import project.oswel.knowledgebase.schedule.DateTime;
import project.oswel.knowledgebase.schedule.WeekDay;
import org.json.simple.parser.ParseException;
import org.nd4j.common.io.ClassPathResource;
import project.oswel.knowledgebase.Weather;
import org.json.simple.parser.JSONParser;
import project.oswel.knowledgebase.JWiki;
import project.oswel.nlp.ChatKeras;
import org.json.simple.JSONObject;
import java.util.logging.Logger;
import project.oswel.nlp.NER;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Stack;

/**
 * This classes process the user input collected from the speech 
 * recognition methods and then process the user input using the 
 * Keras NLP model to determine the category the user input belongs
 * which returns the appropriate response by gathering information
 * collected from various API's.
 * @author John Santos
 */
public class Utils {

	private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());
	private static JSONObject oswelLicense = new JSONObject();
    private static JSONParser parser = new JSONParser();
	private static ChatKeras chatKeras;
	private static Weather weatherInfo;
	private static NewsAPI newsAPI;
	private static JWiki jwiki;
	private static NER ner;

	/**
	 * Creates the utils object but requires the license file name 
	 * primarily stored in the resources folder containing API keys needed
	 * to fetch from various APIs to process the response.
	 * @param licenseFileName The name of the license file containing the 
	 * 						  API keys stored inside the resources folder.
	 * @throws InvalidAPIKeyException
	 * @throws WeatherFetchFailedException
	 */
	public Utils(String licenseFileName) 
				throws InvalidAPIKeyException, WeatherFetchFailedException {

		LOGGER.info("Reading and validating license file...");
		try {
			this.validateLicenseContents(licenseFileName);
		} catch (InvalidAPIKeyException e) {
			throw new InvalidAPIKeyException(e.getMessage());
		}

		LOGGER.info("Reading settings file...");
		JSONObject settings = this.readSettings("settings.json");
		JSONObject resources = (JSONObject) settings.get("resources");
		JSONObject endpoints = (JSONObject) settings.get("endpoints");


		LOGGER.info("Loading model resources...");
		chatKeras =  new ChatKeras(
							(String) resources.get("oswelNLPModel"),
							(String) resources.get("wordsFile"),
							(String) resources.get("classesFile"),
							(String) resources.get("intentsFile")
						);

		weatherInfo = new Weather(
			(String) oswelLicense.get("visualcrossing"),
			(String) endpoints.get("weather")
		);

		ner = new NER(
			(String) resources.get("locationNER"), 
			(String) resources.get("posNER")
		);

		newsAPI = new NewsAPI(
			(String) oswelLicense.get("newsapi"),
			(String) endpoints.get("currentEvents"));
		
		jwiki = new JWiki((String) endpoints.get("wikipedia"));
		
		String[] weekStartEndDates = DateTime.getStartEndWeekDates();
		try {
			weatherInfo.timelineRequestHttpClient(
				weekStartEndDates[0], weekStartEndDates[1], 
				(String) settings.get("cityLocation")
			);
		} catch (WeatherFetchFailedException e) {
			throw new WeatherFetchFailedException(e.getMessage());
		}
	}

	private JSONObject readSettings(String settingsFileName) { 
        JSONParser parser = new JSONParser();
		JSONObject jsonObject = new JSONObject();
        try {
            String settingsPath = new ClassPathResource(settingsFileName)
                                        .getFile()
                                        .getPath();
            jsonObject = (JSONObject) parser.parse(
                                            new FileReader(settingsPath));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } 
		return jsonObject;
    }

	/**
	 * Reads the license file to grab the associated key for the APIs.
	 * @param licenseFileName The name of the license file to read. 
	 */
    private void readOswelLicense(String licenseFileName) {
		try {
			String licensePath = new ClassPathResource(licenseFileName)
										.getFile()
										.getPath();
			oswelLicense = (JSONObject) parser.parse(
				new FileReader(licensePath));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method checks whether the contents of the license file contains
	 * valid API keys.
	 * @param licenseFileName The name of the file containing the API keys
	 * 						  stored inside the resources folder. 
	 * @throws InvalidAPIKeyException
	 */
	private void validateLicenseContents(String licenseFileName) 
												throws InvalidAPIKeyException {
		this.readOswelLicense(licenseFileName);
		String googleSpeechKey = (String) oswelLicense.get("googlespeech");
		String weatherKey = (String) oswelLicense.get("visualcrossing");
		String openAIKey = (String) oswelLicense.get("openai");
		String deepAIKey = (String) oswelLicense.get("deepai");
		String palmAIKey = (String) oswelLicense.get("palm");

		if (googleSpeechKey.equals("None")) {
			throw new InvalidAPIKeyException("A proper Google Speech " +
			"Recognition API Key is needed to allow speech " +
			"recognition in the application. Please visit: " +
			"https://cloud.google.com/speech-to-text/docs/before-you-begin " + 
			"for more information.");
		}

		if (weatherKey.equals("None")) {
			throw new InvalidAPIKeyException("A proper VisualCrossing " +
			"API Key is needed to access weather information. Please visit: " +
			"https://www.visualcrossing.com/weather-api " + 
			"for more information.");
		}

		if (openAIKey.equals("None") && 
				deepAIKey.equals("None")) {
			throw new InvalidAPIKeyException("Text generation is " +
			"handled through the use of either OpenAI, PaLM, or DeepAI API. " +
			"One of these keys is required to proceed with the application.");
		}
 	}

	/**
	 * Returns the license contents.
	 * @return The contents of the license file (JSONObject).
	 */
	public JSONObject getLicense() {
		return oswelLicense;
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
	 * This method processes the weather response to detect if the user
	 * specified the location to fetch weather information from. Or if the
	 * user specified the day to fetch the weather. 
	 * @param userResponse The response of the user to process.
	 * @return The processed string to output (String).
	 * @throws WeatherFetchFailedException
	 */
	private String processWeatherResponse(String userResponse) 
								throws WeatherFetchFailedException {

		String[] daysOfWeek = WeekDay.getDaysOfWeek();
		String[] locations = ner.findLocation(userResponse);
		if (locations.length > 0) {
			if (!locations[0].equalsIgnoreCase(weatherInfo.getLocation())) {
				String[] weekStartEndDates = DateTime.getStartEndWeekDates();
				try {
					weatherInfo.timelineRequestHttpClient(
						weekStartEndDates[0], 
						weekStartEndDates[1], 
						locations[0]
					);
				} catch (WeatherFetchFailedException e) {
					throw new WeatherFetchFailedException(e.getMessage());
				}
			}
		}

		String day = "None";
		JSONObject dayValue = new JSONObject();
		ArrayList<String> userWords = new ArrayList<String>(
			Arrays.asList(userResponse.split(" ")));
		for(int i=0; i<daysOfWeek.length; i++) {
			if (userWords.contains(daysOfWeek[i])) {
				day = daysOfWeek[i];
				dayValue = weatherInfo.getWeatherInfoDay(day);
				break;
			} 
		}

		if (day.equals("None")) {
			day = DateTime.getCurrentDay();
			dayValue = weatherInfo.getWeatherInfoDay(day);
		}
		
		String location = weatherInfo.getLocation();
		double maxTemp = (double) dayValue.get("tempmax");
		double minTemp = (double) dayValue.get("tempmin");
		double temp = (double) dayValue.get("temp");
		double precip = (double) dayValue.get("precipprob");
		String description = (String) dayValue.get("description");

		return "In " + location + " %s "
				+ String.format("on %s %.1f degrees celsius with %s. ", 
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
		String date = "";
		date = DateTime.getCurrentDate();
		return "%s " + date;
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
		String description = "";
		Stack<Integer> nouns = this.collectNouns(userResponse);
		if (nouns.size() >= 2) {
			String topic = words[nouns.pop()];
			description = newsAPI.getNewsByTopic(topic);
		} else {
			description = newsAPI.getNewsTopHeadline();
		}
		return "%s " + description;
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
	 * @throws WeatherFetchFailedException
	 */
	public String[] processResponse(String userResponse) 
									throws WeatherFetchFailedException {
		JSONObject oswelResponse = chatKeras.getRandomResponse(userResponse);
		String category = (String) oswelResponse.get("category");
		double score = (double) oswelResponse.get("score");
		String oswelMessage = (String) oswelResponse.get("response");
		String[] finalResponse = new String[2];

		if (score >= 0.50) {
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
			}
			finalResponse[0] = category;
			finalResponse[1] = oswelMessage;
		} else {
			String wikiMessage = "";
			wikiMessage = this.processWikipediaResponse(userResponse);
			finalResponse[0] = "general";
			finalResponse[1] = wikiMessage;
		}
		return finalResponse;
	}
}
