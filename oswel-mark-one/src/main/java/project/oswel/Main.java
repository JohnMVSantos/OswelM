package project.oswel;

import marytts.signalproc.effects.StadiumEffect;
import net.sourceforge.javaflacencoder.FLACFileWriter;
import netscape.javascript.JSObject;

import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.nd4j.common.io.ClassPathResource;

import project.oswel.speech.TextToSpeech;
import project.oswel.speechrecognition.microphone.Microphone;
import project.oswel.speechrecognition.recognizer.GSpeechDuplex;
import project.oswel.speechrecognition.recognizer.GSpeechResponseListener;
import project.oswel.speechrecognition.recognizer.GoogleResponse;
import project.oswel.speechrecognition.recognizer.Recognize;
import project.oswel.knowledgebase.JWiki;
import project.oswel.knowledgebase.Weather;
import project.oswel.nlp.ChatGPT;
import project.oswel.nlp.ChatKeras;
// import project.oswel.OswelApi;

import project.oswel.exceptions.InvalidAPIKeyException;


public class Main {

	private static JSONParser parser = new JSONParser();
	private static TextToSpeech tts = new TextToSpeech();
	private static final Microphone mic = new Microphone(FLACFileWriter.FLAC);

	private static JSONObject readOswelLicense(String licenseFileName) {
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

	private static void validateLicenseContents(JSONObject oswelLicense) 
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

	private static void setVoiceAndEffect(String voice, double d) {
		// Setting the Current Voice.
		// Options are "dfki-spike-hsmm" and "dfki-obadiah-hsmm"
		tts.setVoice(voice);	
		StadiumEffect stadiumEffect = new StadiumEffect();
		stadiumEffect.setParams("amount:" + d);
		// Apply the effects.
		tts.getMarytts().setAudioEffects(stadiumEffect.getFullEffectAsString());
	}

	private static void speak(String prompt) {
		tts.speak(prompt, 2.0f, false, true);
	}

	private static GSpeechDuplex setVoiceRecognition(String googleSpeechKey) {
		GSpeechDuplex duplex = new GSpeechDuplex(googleSpeechKey);
		duplex.setLanguage("en");
		return duplex;
	}

	private static void startProcess(GSpeechDuplex duplex) {
		duplex.addResponseListener(new GSpeechResponseListener() {
			public void onResponse(GoogleResponse googleResponse) {
				String userInput = "";
				String oswelOutput = "";
				userInput = googleResponse.getResponse();

				if (userInput != null) {
					if (googleResponse.isFinalResponse()) {
						System.out.println("User said: " + userInput);		
						try {
							if (userInput != "") {
								JWiki jwiki = new JWiki(userInput);
								oswelOutput = jwiki.getExtractText();
								System.out.println("Oswel said: " + oswelOutput);
								speak(oswelOutput);
							}		
						} catch (Exception  e) {
							e.printStackTrace();
						}
					}
				} else {
					System.out.println("Output was null");
				}
			}
		});

		Recognize recognizer = new Recognize(duplex, mic);
        Thread recognizerThread = new Thread(recognizer);
        recognizerThread.start();
	}
    public static void main(String[] args) 
						throws IOException, InterruptedException, Exception{
		
		System.out.println("Reading and validating license file...");
		// Reading and validating the license containing API keys.
		JSONObject oswelLicense = readOswelLicense("oswel.lic");
		validateLicenseContents(oswelLicense);

		System.out.println("Configuring Oswel voice...");
		// Set Oswel voice.
		setVoiceAndEffect("dfki-obadiah-hsmm", 5.0);
		
		System.out.println("Loading Oswel Keras NLP model...");
		// Load Python trained Keras NLP model.
		ChatKeras chatKeras = new ChatKeras(
			"oswel.h5",
			"words.txt",
			"classes.txt",
			"intents.json"
		);

		
		// Start Voice Recognition
		// GSpeechDuplex duplex = setVoiceRecognition(
		// 	(String) oswelLicense.get("googlespeech"));
		// startProcess(duplex);

		// OswelApi oswelapi = new OswelApi();
		// String output = oswelapi.getGPTResponse();
		// System.out.println(output);

		

		// Sample Speaking
		speak("I have indeed been uploaded. We're online and ready.");


		// Sample deploying Keras NLP.
		String response = chatKeras.getRandomResponse("Hello what is the time right now?");
		System.out.println(response);
		response = chatKeras.getRandomResponse("How are you doing?");
		System.out.println(response);
		
		// Sample Getting ChatGPT response. 
		// ChatGPT chatGPT = new ChatGPT("quickstart-QUdJIGlzIGNvbWluZy4uLi4K");
		// String description = chatGPT.getGPTResponse("It is higher than 30 degrees celsius outside.");
		// System.out.println(description);

	
		// Sample Getting the time and date information.
	
		
		// Sample getting weather information. 
		// Weather weatherInfo = new Weather("Secret", "Calgary,AB");
		// weatherInfo.timelineRequestHttpClient("2023-06-18", "2023-06-24");
		// weatherInfo.setWeatherInfoDay("saturday");
		// double temp = weatherInfo.getTemp();
		// //String condition = weatherInfo.getCondition();
		// String description = weatherInfo.getDescription();
		// String dateTime = weatherInfo.getDateTime();

		// System.out.printf("%s\t%.1f\t%s\n", dateTime, temp, description);
		// tts.speak(String.format("The current temperature is: %.1f degrees celsius that is %s", temp, description), 2.0f, false, true);


    };
    
}
