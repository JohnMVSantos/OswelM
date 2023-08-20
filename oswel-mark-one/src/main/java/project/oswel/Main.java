package project.oswel;

import project.oswel.speechrecognition.recognizer.GSpeechResponseListener;
import project.oswel.speechrecognition.recognizer.GoogleResponse;
import project.oswel.speechrecognition.recognizer.GSpeechDuplex;
import project.oswel.speechrecognition.microphone.Microphone;
import project.oswel.speechrecognition.recognizer.Recognize;
import net.sourceforge.javaflacencoder.FLACFileWriter;
import project.oswel.speech.constant.TtsStyleEnum;
import project.oswel.speech.constant.VoiceEnum;
import project.oswel.speech.service.TTSService;
import project.oswel.speech.player.Mp3Player;
import project.oswel.speech.model.SSML;
import project.oswel.utilities.Utils;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.io.IOException;

/**
 * This is the main thread which starts the thread for speech recognition
 * and then parses the user input to be processed by the NLP model and 
 * collects data based on the response from various API's.
 * @author John Santos
 */
public class Main {

	private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());
	private static final Microphone mic = new Microphone(FLACFileWriter.FLAC);
	private static Utils utils;
	private static Thread recognizerThread;
	private static TTSService ts = TTSService
										.builder()
										.usePlayer(true)
										.build();

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
							if (userInput != "") {
								oswelOutput = utils.processResponse(userInput);
								LOGGER.info(
										"Oswel said [" + 
										oswelOutput[0] + 
										"]: " + 
										oswelOutput[1]
								);
								speak(oswelOutput[1]);
								if (oswelOutput[0].equalsIgnoreCase(
												"departure")) {
									duplex.wait(4000);
									System.exit(1);	
								}
								duplex.wait((int) Math.abs(
									Mp3Player.recordedTimeInSec * 10));
								LOGGER.info("Listening...");
							}		
						} catch (Exception  e) {
							e.printStackTrace();
						}
					}
				} else {
					LOGGER.severe("No speech was recognized...");
				}
			}
		});

		Recognize recognizer = new Recognize(duplex, mic);
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
		
		// Reading and validating the license containing API keys.
		utils = new Utils("oswel.lic");

		//Start Voice Recognition
		GSpeechDuplex duplex = setVoiceRecognition(
			(String) utils.getLicense().get("googlespeech"));
		LOGGER.info("Listening ...");
		startProcess(duplex);
    };    
}