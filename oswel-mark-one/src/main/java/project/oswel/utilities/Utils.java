package project.oswel.utilities;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.nd4j.common.io.ClassPathResource;

import project.oswel.exceptions.InvalidAPIKeyException;
import project.oswel.exceptions.WeatherFetchFailedException;
import project.oswel.knowledgebase.Weather;
import project.oswel.knowledgebase.schedule.DateTime;
import project.oswel.knowledgebase.schedule.WeekDay;
import project.oswel.nlp.ChatKeras;
import project.oswel.nlp.NER;

public class Utils {

    private static JSONParser parser = new JSONParser();
	private static JSONObject oswelLicense = new JSONObject();
	private static ChatKeras chatKeras;
	private static Weather weatherInfo;
	private static NER ner;

	public Utils(String licenseFileName) throws InvalidAPIKeyException, WeatherFetchFailedException {

		System.out.println("Reading and validating license file...");
		try {
			this.validateLicenseContents(licenseFileName);
		} catch (InvalidAPIKeyException e) {
			throw new InvalidAPIKeyException(e.getMessage());
		}

		System.out.println("Loading model resources...");
		chatKeras =  new ChatKeras(
							"oswel.h5",
							"words.txt",
							"classes.txt",
							"intents.json"
							);

		weatherInfo = new Weather(
			(String) oswelLicense.get("visualcrossing"));

		ner = new NER("en-ner-location.bin");
		
		// String[] weekStartEndDates = DateTime.getStartEndWeekDates();
		// try {
		// 	weatherInfo.timelineRequestHttpClient(
		// 		weekStartEndDates[0], weekStartEndDates[1], "Calgary");
		// } catch (WeatherFetchFailedException e) {
		// 	throw new WeatherFetchFailedException(e.getMessage());
		// }
	}

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

	public JSONObject getLicense() {
		return oswelLicense;
	}

	private String processWeatherResponse(String userResponse, String oswelResponse) throws WeatherFetchFailedException {
		String[] daysOfWeek = WeekDay.getDaysOfWeek();
		String[] locations = ner.findLocation(userResponse);
		if (locations.length > 0) {
			if (!locations[0].equalsIgnoreCase(weatherInfo.getLocation())) {
				String[] weekStartEndDates = DateTime.getStartEndWeekDates();
				try {
					weatherInfo.timelineRequestHttpClient(
						weekStartEndDates[0], weekStartEndDates[1], locations[0]);
				} catch (WeatherFetchFailedException e) {
					throw new WeatherFetchFailedException(e.getMessage());
				}
			}
		}

		String day = "None";
		JSONObject dayValue = new JSONObject();
		ArrayList<String> userWords = new ArrayList<String>(Arrays.asList(userResponse.split(" ")));
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

		return String.format("In %s, ", location) + oswelResponse + 
			String.format(" on %s %.1f degrees celsius with %s.", day, temp, description)
				+ String.format(" A high of %.1f degrees and a low of %.1f degrees celsius.", maxTemp, minTemp);
		
	}

	private String processTimeResponse(String userResponse, String oswelResponse) {
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
		return String.format("In %s, ", location) + oswelResponse + " " + time;
		
	}

	private void processDateTimeResponse(String response, String oswelResponse) {

	}

	public String processResponse(String userResponse) throws WeatherFetchFailedException {
		String[] oswelResponse = chatKeras.getRandomResponse(userResponse);
		String category = oswelResponse[0];
		String oswelMessage = oswelResponse[1];
		

		if (category.equalsIgnoreCase("weather")) {
			// oswelMessage = this.processWeatherResponse(userResponse, oswelMessage);
			return "Trying to save the API.";
		} else if (category.equalsIgnoreCase("time")) {
			oswelMessage = this.processTimeResponse(userResponse, oswelMessage);
		}

		return oswelMessage;
	}
}
