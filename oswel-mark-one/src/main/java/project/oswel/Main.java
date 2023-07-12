package project.oswel;

import marytts.signalproc.effects.StadiumEffect;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import project.oswel.speech.TextToSpeech;
import project.oswel.knowledgebase.Weather;
// import project.oswel.OswelApi;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, Exception{
		
        
		TextToSpeech tts = new TextToSpeech();
		// Setting the Current Voice: Options are "dfki-spike-hsmm" and "dfki-obadiah-hsmm"
		tts.setVoice("dfki-obadiah-hsmm");	

		// Voice StadiumEffect
		StadiumEffect stadiumEffect = new StadiumEffect();
		stadiumEffect.setParams("amount:5.0");
		//Apply the effects
		tts.getMarytts().setAudioEffects(stadiumEffect.getFullEffectAsString());


        // tts.speak(user_input, 2.0f, false, true);

		//Weather.timelineRequestHttpClient();
		//tts.speak("The temperature outside is currently above 30 degrees Celsius. This indicates that the weather is hot and possibly uncomfortable. It is important to stay hydrated and seek shade if possible to avoid heat exhaustion or heatstroke. It may be a good idea to avoid strenuous outdoor activities during the hottest part of the day and instead opt to stay indoors in air conditioning or in a cool, shady area. Remember to wear sunscreen to protect your skin from the sun's harmful rays.", 2.0f, false, true);

		//tts.speak("I have indeed been uploaded. We're online and ready.", 2.0f, false, true);

		OswelApi oswelapi = new OswelApi();
		oswelapi.fetchAll();
		String output = oswelapi.getData("user");
		System.out.println(output);
		output = oswelapi.postData("weather", "My name is John Santos");
		System.out.println(output);
		//tts.speak("It is currently 5:47 PM", 2.0f, false, true);\

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
