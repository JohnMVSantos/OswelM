package project.oswel;

import project.oswel.speechrecognition.recognizer.GSpeechDuplex;
import project.oswel.connections.SerialConnection;
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
public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	/**
	 * This is the main function for which the program starts. 
	 * @param args Command line arguments but it is not used in this case. 
	 */
    public static void main(String[] args) {
		boolean connect = false;
		String device = "linuxSerial";
		if (args.length >= 2) {
			if (args[0].equalsIgnoreCase("connect")) {
				connect = true;
				if (args[1].equalsIgnoreCase("linuxSerial")) {
					device = "linuxSerial";
				} else if (
					args[1].equalsIgnoreCase("windowsSerial")) {
					device = "windowsSerial";
				} else {
					LOGGER.severe(
						"Choices are linuxSerial or WindowsSerial");
				}
			}	
		}
		
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

		if (connect) {
			SerialConnection serialConnect = new SerialConnection(
				settings.getString(device));
			Initialization.startProcess(
				duplex, speechInterpreter, serialConnect);  
		} else {
			SerialConnection serialConnect = new SerialConnection();
			Initialization.startProcess(
				duplex, speechInterpreter, serialConnect);  
		}
    };    
}