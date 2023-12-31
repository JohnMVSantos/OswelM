package project.oswel.speechrecognition.synthesizer;

import java.net.URLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.net.URL;

/**
 * This class uses the V2 version of Google's Text to Speech API. 
 * While this class requires an API key, the endpoint allows for 
 * additional specification of parameters including speed and pitch. 
 * See the constructor for instructions regarding the API_Key.
 * @author Skylion (Aaron Gokaslan)
 * {@link https://github.com/goxr3plus/java-google-speech-api/tree/master/src/main/java/com/goxr3plus/speech}
 */
public class SynthesizerV2 extends BaseSynthesizer {

	private static final String GOOGLE_SYNTHESISER_URL = 
	"https://www.google.com/speech-api/v2/synthesize?enc=mpeg" + 
	"&client=chromium";
	
	/**
	 * API_KEY used for requests
	 */
	private final String API_KEY;

	/**
	 * language of the Text you want to translate
	 */
	private String languageCode;
	
	/**
	 * The pitch of the generated audio
	 */
	private double pitch = 1.0;
	
	/**
	 * The speed of the generated audio
	 */
	private double speed = 1.0;
	
	/**
	 * Constructor
	 * @param API_KEY The API-Key for Google's Speech API. 
	 * An API key can be obtained by requesting one by 
	 * following the process shown at this 
	 * <a href="http://www.chromium.org/developers/how-tos/api-keys">url</a>.
	 */
	public SynthesizerV2(String API_KEY){
		this.API_KEY = API_KEY;
	}
	
	/**
	 * Returns the current language code for the Synthesiser.
	 * Example: English(Generic) = en, English (US) = en-US, 
	 * English (UK) = en-GB. and Spanish = es;
	 * @return the current language code parameter
	 */
	public String getLanguage(){ return languageCode; }

	/**
	 * Note: set language to auto to enable automatic language detection.
	 * Setting to null will also implement Google's automatic language detection
	 * @param languageCode The language code you would like to 
	 * 					modify languageCode to.
	 */
	public void setLanguage(String languageCode){
		this.languageCode = languageCode;
	}

	/**
	 * @return the pitch
	 */
	public double getPitch() { return pitch; }

	/**
	 * Sets the pitch of the audio.
	 * Valid values range from 0 to 2 inclusive.
	 * Values above 1 correspond to higher pitch, 
	 * values below 1 correspond to lower pitch.
	 * @param pitch the pitch to set
	 */
	public void setPitch(double pitch) { this.pitch = pitch; }

	/**
	 * @return the speed
	 */
	public double getSpeed() { return speed; }

	/**
	 * Sets the speed of audio.
	 * Valid values range from 0 to 2 inclusive.
	 * Values higher than one correspond to faster and vice versa. 
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) { this.speed = speed; }
	
	@Override
	public InputStream getMP3Data(String synthText) throws IOException{

		// Ensures retention of language settings if set to auto
		String languageCode = this.languageCode;

		if(languageCode == null || 
		   languageCode.equals("") || 
		   languageCode.equalsIgnoreCase("auto")){

			try{
				// Detects language
				languageCode = detectLanguage(synthText);
				if(languageCode == null){
					// Reverts to Default Language if it can't detect it.
					languageCode = "en-us";
				}
			}
			catch(Exception ex){
				ex.printStackTrace();
				// Reverts to Default Language if it can't detect it.
				languageCode = "en-us";
			}
		}

		if(synthText.length()>100){
			// Parses String if too long
			List<String> fragments = parseString(synthText);
			String tmp = getLanguage();
			// Keeps it from autodetecting each fragment.
			setLanguage(languageCode);
			InputStream out = getMP3Data(fragments);
			// Reverts it to it's previous Language such as auto.
			setLanguage(tmp);
			return out;
		}

		// Encode
		String encoded = URLEncoder.encode(synthText, "UTF-8"); 

		StringBuilder sb = new StringBuilder(GOOGLE_SYNTHESISER_URL);
		sb.append("&key=" + API_KEY);
		sb.append("&text=" + encoded);
		sb.append("&lang=" + languageCode);

		if(speed>=0 && speed<=2.0){
			sb.append("&speed=" + speed/2.0);
		}
		
		if(pitch>=0 && pitch<=2.0){
			sb.append("&pitch=" + pitch/2.0);
		}
		
		// Create url
		URL url = new URL(sb.toString()); 

		// Open New URL connection channel.
		URLConnection urlConn = url.openConnection(); 

		urlConn.addRequestProperty(
			"User-Agent", 
			// Adding header for user agent is required
			"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:2.0) " + 
			"Gecko/20100101 Firefox/4.0"); 
		return urlConn.getInputStream();
	}
}