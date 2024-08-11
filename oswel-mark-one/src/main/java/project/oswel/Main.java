/* Oswell backend Java Application.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

package project.oswel;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import project.oswel.speechrecognition.recognizer.GSpeechDuplex;
import org.springframework.boot.SpringApplication;
import project.oswel.utilities.Initialization;
import project.oswel.nlp.SpeechProcess;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 * This is the main thread which starts the thread for speech recognition
 * and then parses the user input to be processed by the NLP model and 
 * collects data based on the response from various API's.
 * @author John Santos
 */
@SpringBootApplication
public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	/**
	 * This is the main function for which the program starts. 
	 * @param args Command line arguments but it is not used in this case. 
	 */
    public static void main(String[] args) {		
		LOGGER.info("Starting the server...");
		SpringApplication.run(Main.class, args);
		
		LOGGER.info("Reading license file...");
		JSONObject oswelLicense = Initialization
									.readJSONFile("oswel.lic");

		LOGGER.info("Reading settings file...");
		JSONObject settings = Initialization
									.readJSONFile("settings.json");
		JSONObject resources = settings.getJSONObject("resources");
		JSONObject endpoints = settings.getJSONObject("endpoints");
		SpeechProcess speechInterpreter = new SpeechProcess(
										oswelLicense, 
										resources, 
										endpoints, 
										settings.getString("cityLocation"));
		Initialization.startConfirmation(speechInterpreter);
		
		//Start Voice Recognition
		GSpeechDuplex duplex = Initialization.setVoiceRecognition(
								oswelLicense.getString("googlespeech"));
		
		LOGGER.info("Listening ...");
		Initialization.startProcess(duplex, speechInterpreter); 
    }    
}