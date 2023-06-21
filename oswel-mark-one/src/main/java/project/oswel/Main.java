package project.oswel;

import marytts.signalproc.effects.StadiumEffect;
import java.io.IOException;
import project.oswel.speech.TextToSpeech;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException{
		
        
		TextToSpeech tts = new TextToSpeech();
		// Setting the Current Voice: Options are "dfki-spike-hsmm" and "dfki-obadiah-hsmm"
		tts.setVoice("dfki-obadiah-hsmm");	

		// Voice StadiumEffect
		StadiumEffect stadiumEffect = new StadiumEffect();
		stadiumEffect.setParams("amount:60.0");
		//Apply the effects
		tts.getMarytts().setAudioEffects(stadiumEffect.getFullEffectAsString());


        // tts.speak(user_input, 2.0f, false, true);

		tts.speak("I have indeed been uploaded sir. We're online and ready.", 2.0f, false, true);

    };
    
}
