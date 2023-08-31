package project.oswel.utilities;

import org.nd4j.common.io.ClassPathResource;
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
}
