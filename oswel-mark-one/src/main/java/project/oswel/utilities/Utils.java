package project.oswel.utilities;

import project.oswel.exceptions.WeatherFetchFailedException;
import project.oswel.knowledgebase.currentevents.NewsAPI;
import project.oswel.exceptions.InvalidAPIKeyException;
import project.oswel.knowledgebase.schedule.DateTime;
import project.oswel.knowledgebase.schedule.WeekDay;
import org.nd4j.common.io.ClassPathResource;
import project.oswel.knowledgebase.Weather;
import project.oswel.knowledgebase.JWiki;
import project.oswel.nlp.ChatKeras;
import java.util.logging.Logger;
import project.oswel.nlp.NER;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileInputStream;
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
		JSONObject resources = settings.getJSONObject("resources");
		JSONObject endpoints = settings.getJSONObject("endpoints");


		LOGGER.info("Loading model resources...");
		chatKeras =  new ChatKeras(
							resources.getString("oswelNLPModel"),
							resources.getString("wordsFile"),
							resources.getString("classesFile"),
							resources.getString("intentsFile"));

		weatherInfo = new Weather(
			oswelLicense.getString("visualcrossing"),
			endpoints.getString("weather"));

		ner = new NER(
			resources.getString("locationNER"), 
			resources.getString("posNER"));

		newsAPI = new NewsAPI(
			oswelLicense.getString("newsapi"),
			endpoints.getString("currentEvents"));
		
		jwiki = new JWiki(endpoints.getString("wikipedia"));
		
		// String[] weekStartEndDates = DateTime.getStartEndWeekDates();
		// try {
		// 	weatherInfo.timelineRequestHttpClient(
		// 		weekStartEndDates[0], weekStartEndDates[1], 
		// 		(String) settings.get("cityLocation")
		// 	);
		// } catch (WeatherFetchFailedException e) {
		// 	throw new WeatherFetchFailedException(e.getMessage());
		// }
	}

	/**
	 * This method reads the file settings.json to grab the user set 
	 * information for the endpoints and resources file names. 
	 * @param settingsFileName The file name containing the settings.
	 * 						   It is usually settings.json under resources.
	 * @return JSONObject containing the contents of the JSON file. 
	 */
	private JSONObject readSettings(String settingsFileName) { 
		JSONObject jsonObject = new JSONObject();
        try {
            String settingsPath = new ClassPathResource(settingsFileName)
                                        .getFile()
                                        .getPath();
			InputStream is = new FileInputStream(settingsPath);
			if (is == null) {
				throw new NullPointerException(
					"Cannot find resource file " + settingsPath);
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				JSONTokener tokener = new JSONTokener(in);
				jsonObject = new JSONObject(tokener);
			}		
        } catch (IOException e) {
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
		
			InputStream is = new FileInputStream(licensePath);
			if (is == null) {
				throw new NullPointerException(
					"Cannot find resource file " + licensePath);
			} else {
				BufferedReader in = new BufferedReader(new InputStreamReader(is));
				JSONTokener tokener = new JSONTokener(in);
				oswelLicense = new JSONObject(tokener);
			}		
		} catch (IOException e) {
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
		String googleSpeechKey = oswelLicense.getString("googlespeech");
		String weatherKey = oswelLicense.getString("visualcrossing");
		String openAIKey = oswelLicense.getString("openai");
		String deepAIKey = oswelLicense.getString("deepai");

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
		double maxTemp = dayValue.getDouble("tempmax");
		double minTemp = dayValue.getDouble("tempmin");
		double temp = dayValue.getDouble("temp");
		double precip = dayValue.getDouble("precipprob");
		String description = dayValue.getString("description");

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
		String[] summary = new String[3];
		Stack<Integer> nouns = this.collectNouns(userResponse);
		if (nouns.size() >= 2) {
			String topic = words[nouns.pop()];
			summary = newsAPI.getNewsByTopic(topic);
		} else {
			summary = newsAPI.getNewsTopHeadline();
		}
		return "%s " + summary[0] + summary[1] + summary[2];
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
		String category = oswelResponse.getString("category");
		double score = oswelResponse.getDouble("score");
		String oswelMessage = oswelResponse.getString("response");
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
