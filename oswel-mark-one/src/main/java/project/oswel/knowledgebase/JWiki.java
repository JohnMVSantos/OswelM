package project.oswel.knowledgebase;

import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Request;
import org.json.simple.JSONObject;
import java.io.IOException;

/**
 * JWiki provides summary gathered from wikipedia for a certain topic 
 * represented by a single string keyword.
 * {@link https://github.com/viralvaghela/Jwiki} 
 */
public class JWiki {
    final String BASE_URL="https://en.wikipedia.org/api/rest_v1/page/summary/";
    String subject=null;
    String displayTitle="";
    String imageURL="";
    String extractText="";

    /**
     * This creates a new object with the subject to search for.
     * @param subject The subject to search for in wikipedia. 
     */
    public JWiki(String subject)
    {
        this.subject=subject;
        getData();
    }

    /**
     * Communicates to the API to fetch the description of the topic passed. 
     */
    private void getData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL+subject)
                .get()
                .build();
        try {
            Response response=client.newCall(request).execute();
            String data = response.body().string();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(data);

            //get title from JSON response
            displayTitle= (String) jsonObject.get("displaytitle");

            //first create a image object and then get image URL
            JSONObject jsonObjectOriginalImage = 
                            (JSONObject) jsonObject.get("originalimage");
            imageURL= (String) jsonObjectOriginalImage.get("source");

            //get text
            extractText = (String)jsonObject.get("extract");
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the title of the wikipedia page.
     * @return The title (String).
     */
    public String getDisplayTitle() {return displayTitle;}

    /**
     * Returns the URL pointing to the wikipedia page.
     * @return The URL (String).
     */
    public String getImageURL() {return imageURL;}

    /**
     * Returns the extracted text based on the topic provided.
     * @return The description of the topic provided (String).
     */
    public String getExtractText() {return extractText;}
}
