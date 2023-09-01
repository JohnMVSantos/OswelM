package project.oswel.utilities;

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
import org.nd4j.common.io.ClassPathResource;
import project.oswel.nlp.SpeechProcess;
import project.oswel.speech.model.SSML;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.io.FileInputStream;
import java.io.BufferedReader;
import org.json.JSONTokener;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONObject;

/**
 * This class performs initialization process prior to starting the main
 * functionalities. These preprocesses include reading the license file to
 * fetch the API keys needed and reading the settings file based on the 
 * user's configurations.
 * @author John Santos
 */
public abstract class Initialization {

    private static final Logger LOGGER = Logger
                                            .getLogger(
                                                Initialization.class
                                                                .getName());
	private static final Microphone mic = new Microphone(FLACFileWriter.FLAC);
	private static TTSService tts = TTSService.builder()
											 .usePlayer(true)
											 .build();				

    /**
	 * Reads a JSON file to grab the contents.
	 * @param licenseFileName The name of the JSON file to read. 
     * @return A JSONObject containing the contents of the JSON file. 
	 */
    public static JSONObject readJSONFile(String fileName) {
        JSONObject jsonFile = new JSONObject();
		try {
			String filePath = new ClassPathResource(fileName)
										.getFile()
										.getPath();
			InputStream is = new FileInputStream(filePath);
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			JSONTokener tokener = new JSONTokener(in);
			jsonFile = new JSONObject(tokener);		
		} catch (IOException e) {
			LOGGER.severe(
                "Reading" + fileName + " resulted in an IOException.");
				System.exit(1);
		}
        return jsonFile;
	}

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
        tts.sendText(ssml);
	}

	/**
	 * Sets the voice recognition language and intializes the resources
	 * needed for voice recognition in the application if provided with
	 * an Google API key.
	 * @param googleSpeechKey
	 * @return GSpeechDuplex object used for recognizing speech.
	 */
	public static GSpeechDuplex setVoiceRecognition(String googleSpeechKey) {
		GSpeechDuplex duplex = new GSpeechDuplex(googleSpeechKey);
		duplex.setLanguage("en");
		return duplex;
	}

	/**
	 * Starts the speech recognition process and process each 
	 * user response collected to return an appropriate response.
	 * @param duplex Object responsible for recognizing speech.
	 * @param speechInterpreter Object responsible for interpreting speech.
	 * @param serialConnect Object responsible for connecting to IoT devices. 
	 */
	public static void startProcess(
		GSpeechDuplex duplex, 
		SpeechProcess speechInterpreter, 
		SerialConnection serialConnect
	) {
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

							if (serialConnect.checkConnection()) {
								serialConnect.writeBytes(
									oswelOutput[1].getBytes(), 1000);
							}
							// Wait to complete computing the length of speech.
							duplex.wait(2000);
							
							if (oswelOutput[0].equalsIgnoreCase(
											"departure")) {
								duplex.wait((int) Math.abs(
								Mp3Player.recordedTimeInSec * 10));
								if (serialConnect.checkConnection()) {
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
		Recognize listener = new Recognize(duplex, mic);
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
	}
}
