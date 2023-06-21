package project.oswel.speechrecognition.synthesizer;

import java.net.URLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.net.URL;

/*******************************************************************************
 * Synthesiser class that connects to Google's unoffical API to retrieve data
 *
 * @author Luke Kuza, Aaron Gokaslan (Skylion)
 ******************************************************************************/
public class Synthesizer extends BaseSynthesizer {

	/**
	 * URL to query for Google synthesiser
	 */
	private final static String GOOGLE_SYNTHESISER_URL = 
	"http://translate.google.com/translate_tts";

	/**
	 * language of the Text you want to translate
	 */
	private String languageCode; 

	/**
	 * LANG_XX_XXXX Variables are language codes. 
	 */
	public static final String LANG_AU_ENGLISH = "en-AU";
	public static final String LANG_US_ENGLISH = "en-US";
	public static final String LANG_UK_ENGLISH = "en-GB";
	public static final String LANG_ES_SPANISH = "es";
	public static final String LANG_FR_FRENCH = "fr";
	public static final String LANG_DE_GERMAN = "de";
	public static final String LANG_PT_PORTUGUESE = "pt-pt";
	public static final String LANG_PT_BRAZILIAN = "pt-br";
	// Please add on more regional languages as you find them. 
	// Also try to include the accent code if you can can.

	/**
	 * Constructor
	 */
	public Synthesizer() {
		languageCode = "auto";
	}

	/**
	 * Constructor that takes language code parameter. 
	 * Specify to "auto" for language autoDetection 
	 * @param languageCode The language code [ Example "es" for SPANISH ]
	 */
	public Synthesizer(String languageCode){
		this.languageCode = languageCode;
	}

	/**
	 * Returns the current language code for the Synthesiser.
	 * Example: English(Generic) = en, English (US) = en-US, 
	 * English (UK) = en-GB. and Spanish = es;
	 * @return the current language code parameter
	 */
	public String getLanguage(){
		return languageCode;
	}

	/**
	 * Note: set language to auto to enable automatic language detection.
	 * Setting to null will also implement Google's automatic language detection
	 * @param languageCode The language code you would like to 
	 * 					modify languageCode to.
	 */
	public void setLanguage(String languageCode){
		this.languageCode = languageCode;
	}

	@Override
	public InputStream getMP3Data(String synthText) throws IOException{

		// Ensures retention of language settings if set to auto
		String languageCode = this.languageCode;

		if(languageCode == null || 
		   languageCode.equals("") || 
		   languageCode.equalsIgnoreCase("auto")){

			languageCode = detectLanguage(synthText);//Detects language
			/* NOTE: Detect language relies on an entirely seperate endpoint.
			 * If the GoogleTranslate API stops working, do not use 
			 * the auto parameter and switch to something else or a best guess.
			 */
			if(languageCode == null){
				// Reverts to Default Language if it can't detect it.
				languageCode = "en-us";
				// Throw an error message here eventually
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

		StringBuilder sb = new StringBuilder();
		// The base URL prefixed by the query parameter.
		sb.append(GOOGLE_SYNTHESISER_URL); 
		sb.append("?tl=");
		// The query parameter to specify the language code.
		sb.append(languageCode); 
		sb.append("&q=");
		// We encode the String using URL Encoder
		sb.append(encoded); 
		// Some unknown parameters needed to make the URL work
		sb.append("&ie=UTF-8&total=1&idx=0"); 
		sb.append("&textlen=");
		// We need some String length now.
		sb.append(synthText.length()); 
		// Once again, a weird parameter.
		sb.append("&client=tw-ob"); 
		// Client=t no longer works as it requires a token, 
		// but client=tw-ob seems to work just fine.

		URL url = new URL(sb.toString());
		// Open New URL connection channel.
		URLConnection urlConn = url.openConnection(); //Open connection

		//Adding header for user agent is required
		urlConn.addRequestProperty(
			"User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:2.0) "
			+ "Gecko/20100101 Firefox/4.0");

		return urlConn.getInputStream();
	}
}