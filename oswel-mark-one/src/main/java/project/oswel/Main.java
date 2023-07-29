package project.oswel;

import project.oswel.speechrecognition.recognizer.GSpeechResponseListener;
import project.oswel.speechrecognition.recognizer.GoogleResponse;
import project.oswel.speechrecognition.recognizer.GSpeechDuplex;
import project.oswel.speechrecognition.microphone.Microphone;
import project.oswel.speechrecognition.recognizer.Recognize;
import net.sourceforge.javaflacencoder.FLACFileWriter;
import marytts.signalproc.effects.StadiumEffect;
import project.oswel.speech.TextToSpeech;
import project.oswel.utilities.Utils;
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
	private static TextToSpeech tts = new TextToSpeech();
	private static Utils utils;
	
	/**
	 * Sets the voice of the application and any other effects. 
	 * @param voice The name of the voice to use in the application. 
	 * @param d The amount to set the stadium effect (float).
	 */
	private static void setVoiceAndEffect(String voice, double d) {
		// Setting the Current Voice.
		// Options are dfki-spike-hsmm, dfki-obadiah-hsmm, cmu-bdl-hsmm, cmu-rms-hsmm
		tts.setVoice(voice);	
		StadiumEffect stadiumEffect = new StadiumEffect();
		stadiumEffect.setParams("amount:" + d);
		// Apply the effects.
		tts.getMarytts().setAudioEffects(stadiumEffect.getFullEffectAsString());
	}

	/**
	 * Provides speaking capabilities in the application. 
	 * @param prompt The string to speak.
	 */
	private static void speak(String prompt) {
		tts.speak(prompt, 2.0f, false, true);
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
								LOGGER.info("Oswel said: " + 
														oswelOutput[1]);
								speak(oswelOutput[1]);
								if (oswelOutput[0].equalsIgnoreCase(
												"departure")) {
									System.exit(1);
								}
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
        Thread recognizerThread = new Thread(recognizer);
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
		
		LOGGER.info("Configuring Oswel voice...");
		// Set Oswel voice.
		setVoiceAndEffect("cmu-bdl-hsmm", 5.0);
		
		//Start Voice Recognition
		GSpeechDuplex duplex = setVoiceRecognition(
			(String) utils.getLicense().get("googlespeech"));
		LOGGER.info("Listening ...");
		startProcess(duplex);

		// Sample Getting ChatGPT response. 
		// ChatGPT chatGPT = new ChatGPT("quickstart-QUdJIGlzIGNvbWluZy4uLi4K");
		// String description = chatGPT.getGPTResponse("It is higher than 30 degrees celsius outside.");
		// System.out.println(description);
    };    
}