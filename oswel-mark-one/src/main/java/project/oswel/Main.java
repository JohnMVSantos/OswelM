package project.oswel;

import marytts.signalproc.effects.StadiumEffect;
import net.sourceforge.javaflacencoder.FLACFileWriter;
import java.io.IOException;




import project.oswel.speech.TextToSpeech;
import project.oswel.speechrecognition.microphone.Microphone;
import project.oswel.speechrecognition.recognizer.GSpeechDuplex;
import project.oswel.speechrecognition.recognizer.GSpeechResponseListener;
import project.oswel.speechrecognition.recognizer.GoogleResponse;
import project.oswel.speechrecognition.recognizer.Recognize;
import project.oswel.knowledgebase.JWiki;
import project.oswel.knowledgebase.Weather;
import project.oswel.knowledgebase.currentevents.NewsAPI;


import project.oswel.knowledgebase.schedule.DateTime;
import project.oswel.nlp.ChatGPT;
import project.oswel.nlp.ChatKeras;
import project.oswel.nlp.NER;
// import project.oswel.OswelApi;
import project.oswel.utilities.Utils;

import project.oswel.exceptions.InvalidAPIKeyException;


public class Main {

	private static Utils utils;
	private static TextToSpeech tts = new TextToSpeech();
	private static final Microphone mic = new Microphone(FLACFileWriter.FLAC);
	
	private static void setVoiceAndEffect(String voice, double d) {
		// Setting the Current Voice.
		// Options are dfki-spike-hsmm, dfki-obadiah-hsmm, cmu-bdl-hsmm, cmu-rms-hsmm
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
								oswelOutput = utils.processResponse(userInput);
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
		
		// Reading and validating the license containing API keys.
		utils = new Utils("oswel.lic");

		// String oswelResponse = utils.processResponse("What is the weather in New York");
		// System.out.println(oswelResponse);
		
		System.out.println("Configuring Oswel voice...");
		// Set Oswel voice.
		setVoiceAndEffect("cmu-bdl-hsmm", 5.0);
		
		//Start Voice Recognition
		GSpeechDuplex duplex = setVoiceRecognition(
			(String) utils.getLicense().get("googlespeech"));
		startProcess(duplex);

		// JWiki jwiki = new JWiki(userInput);
		// oswelOutput = jwiki.getExtractText();

		// Sample Getting ChatGPT response. 
		// ChatGPT chatGPT = new ChatGPT("quickstart-QUdJIGlzIGNvbWluZy4uLi4K");
		// String description = chatGPT.getGPTResponse("It is higher than 30 degrees celsius outside.");
		// System.out.println(description);

    };    
}
