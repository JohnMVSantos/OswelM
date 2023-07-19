package project.oswel;

import marytts.signalproc.effects.StadiumEffect;
import net.sourceforge.javaflacencoder.FLACFileWriter;

import java.io.FileReader;
import java.io.IOException;

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
import project.oswel.knowledgebase.schedule.DateTime;
import project.oswel.nlp.ChatGPT;
import project.oswel.nlp.ChatKeras;
import project.oswel.nlp.NER;
// import project.oswel.OswelApi;
import project.oswel.utilities.Utils;

import project.oswel.exceptions.InvalidAPIKeyException;


public class Main {

	
	private static TextToSpeech tts = new TextToSpeech();
	private static final Microphone mic = new Microphone(FLACFileWriter.FLAC);
	private static ChatKeras chatKeras = new ChatKeras(
											"oswel.h5",
											"words.txt",
											"classes.txt",
											"intents.json"
												);
	private static Weather weatherInfo;

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
								oswelOutput = chatKeras.getRandomResponse(userInput);
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
		JSONObject oswelLicense = Utils.readOswelLicense("oswel.lic");
		Utils.validateLicenseContents(oswelLicense);

		System.out.println("Configuring Oswel voice...");
		// Set Oswel voice.
		setVoiceAndEffect("dfki-obadiah-hsmm", 5.0);

		NER ner = new NER("en-ner-date.bin");
		String[] dates = ner.findDate("Give me the weather on Thursday please");
		for (String date: dates) {
			System.out.println(date);
		}
		
		// Initializing weather information for the week.
		// weatherInfo = new Weather(
		// 	(String) oswelLicense.get("visualcrossing"));
		// String[] weekStartEndDates = DateTime.getStartEndWeekDates();
		// weatherInfo.timelineRequestHttpClient(
		// 	weekStartEndDates[0], weekStartEndDates[1], "Calgary,AB");
		// JSONObject dayValue = weatherInfo.getWeatherInfoDay("saturday");
        // double maxTemp = dayValue.getDouble("tempmax");
        // double minTemp = dayValue.getDouble("tempmin");
        // double temp = dayValue.getDouble("temp");
        // double precip = dayValue.getDouble("precipprob");
        // double description = dayValue.getString("description");
		// System.out.printf("%s\t%.1f\t%s\n", dateTime, temp, description);
		// tts.speak(String.format("The current temperature is: %.1f degrees celsius that is %s", temp, description), 2.0f, false, true);
		
		// Start Voice Recognition
		// GSpeechDuplex duplex = setVoiceRecognition(
		// 	(String) oswelLicense.get("googlespeech"));
		// startProcess(duplex);

		// JWiki jwiki = new JWiki(userInput);
		// oswelOutput = jwiki.getExtractText();

		// Sample Getting ChatGPT response. 
		// ChatGPT chatGPT = new ChatGPT("quickstart-QUdJIGlzIGNvbWluZy4uLi4K");
		// String description = chatGPT.getGPTResponse("It is higher than 30 degrees celsius outside.");
		// System.out.println(description);

    };    
}
