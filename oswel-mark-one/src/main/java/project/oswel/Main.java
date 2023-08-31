package project.oswel;

import project.oswel.speechrecognition.recognizer.GSpeechDuplex.CaptureState;
import project.oswel.speechrecognition.recognizer.GSpeechResponseListener;
import project.oswel.speechrecognition.recognizer.GoogleResponse;
import project.oswel.speechrecognition.recognizer.GSpeechDuplex;
import project.oswel.speechrecognition.microphone.Microphone;
import project.oswel.speechrecognition.recognizer.Recognize;
import net.sourceforge.javaflacencoder.FLACFileWriter;
import project.oswel.connections.SerialConnection;
import project.oswel.speech.constant.TtsStyleEnum;
import project.oswel.speech.constant.VoiceEnum;
import project.oswel.speech.service.TTSService;
import project.oswel.utilities.Initialization;
import project.oswel.speech.player.Mp3Player;
import project.oswel.nlp.SpeechProcess;
import project.oswel.speech.model.SSML;
import java.util.logging.Logger;

import org.bytedeco.librealsense.device;
import org.json.JSONObject;
import java.io.IOException;

/**
 * This is the main thread which starts the thread for speech recognition
 * and then parses the user input to be processed by the NLP model and 
 * collects data based on the response from various API's.
 * @author John Santos
 */
public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
	private static final Microphone mic = new Microphone(FLACFileWriter.FLAC);
	private static TTSService ts = TTSService.builder()
											 .usePlayer(true)
											 .build();				
	private static SpeechProcess speechInterpreter;
	private static Thread recognizerThread;
	private static Recognize recognizer;
	private static SerialConnection serialConnect;
	private static boolean connect = false;
	
	/**
	 * Provides speaking capabilities in the application. 
	 * @param prompt The string to speak.
	 */
	private static void speak(String prompt) {
		SSML ssml = SSML.builder()
                .synthesisText(prompt)
                .voice(VoiceEnum.en_GB_RyanNeural)
                .style(TtsStyleEnum.friendly)
				.build();
        ts.sendText(ssml);
	}

	/**
	 * Sets the voice recognition language and intializes the resources
	 * needed for voice recognition in the application if provided with
	 * an Google API key.
	 * @param googleSpeechKey
	 * @return
	 */
	private static GSpeechDuplex setVoiceRecognition(String googleSpeechKey) {
		GSpeechDuplex duplex = new GSpeechDuplex(googleSpeechKey);
		duplex.setLanguage("en");
		return duplex;
	}
	
	/**
	 * Starts the speech recognition process and process each 
	 * user response collected to return an appropriate response.
	 * @param duplex
	 */
	private static void startProcess(GSpeechDuplex duplex) {
		duplex.addResponseListener(new GSpeechResponseListener() {
			public void onResponse(GoogleResponse googleResponse) {
				String userInput = "";
				String[] oswelOutput;
				userInput = googleResponse.getResponse();
				if (userInput != null) {
					if (googleResponse.isFinalResponse()) {
						LOGGER.info("User said: " + userInput);	
						try {
							oswelOutput = speechInterpreter
												.processResponse(userInput);
							LOGGER.info(
								"Oswel said [" + oswelOutput[0] + "]: " + 
								oswelOutput[1]
							);
							duplex.setRecognitionState(CaptureState.STOP);
							speak(oswelOutput[1]);

							if (connect) {
								serialConnect.writeBytes(
									oswelOutput[1].getBytes(), 1000);
							}
							// Wait to complete computing the length of speech.
							duplex.wait(2000);
							
							if (oswelOutput[0].equalsIgnoreCase(
											"departure")) {
								duplex.wait((int) Math.abs(
								Mp3Player.recordedTimeInSec * 10));
								if (connect) {
									serialConnect.closeConnection();
								}
								System.exit(1);	
							}

							// Wait for Oswell to complete speaking.
							int delay = (int) Math.abs(
								Mp3Player.recordedTimeInSec * 10);
							if (delay > 0) duplex.wait(delay);
							LOGGER.info("Listening...");
							duplex.setRecognitionState(CaptureState.CONTINUE);
									
						} catch (InterruptedException e) {
							LOGGER.severe(
								"The recognition process was interrupted.");
								System.exit(1);
						}
					}
				} else {
					LOGGER.severe("speech was not recognized.");
					System.exit(1);
				}
			}
		});
		recognizer = new Recognize(duplex, mic);
        recognizerThread = new Thread(recognizer);
        recognizerThread.start();
	}

	/**
	 * This is the main function for which the program starts. 
	 * @param args Command line arguments but it is not used in this case. 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws Exception
	 */
    public static void main(String[] args) 
						throws IOException, InterruptedException, Exception{
		
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
						"Choices for the connection are linuxSerial or WindowsSerial");
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
		speechInterpreter = new SpeechProcess(
										oswelLicense, 
										resources, 
										endpoints, 
										settings.getString("cityLocation"));
		if (connect) {
			serialConnect = new SerialConnection(
				settings.getString(device));
		}

		//Start Voice Recognition
		GSpeechDuplex duplex = setVoiceRecognition(
								oswelLicense.getString("googlespeech"));
		LOGGER.info("Listening ...");
		startProcess(duplex);  
    };    
}