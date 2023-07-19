package project.oswel.utilities;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.nd4j.common.io.ClassPathResource;

import project.oswel.exceptions.InvalidAPIKeyException;

public class Utils {

    private static JSONParser parser = new JSONParser();

    public static JSONObject readOswelLicense(String licenseFileName) {
		JSONObject oswelLicense = new JSONObject();
		try {
			String licensePath = new ClassPathResource(licenseFileName)
				.getFile()
				.getPath();
			oswelLicense = (JSONObject) parser.parse(
				new FileReader(licensePath));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return oswelLicense;
	}

	public static void validateLicenseContents(JSONObject oswelLicense) 
												throws InvalidAPIKeyException {
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
}
