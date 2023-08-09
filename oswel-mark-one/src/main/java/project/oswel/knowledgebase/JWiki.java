package project.oswel.knowledgebase;

import okhttp3.OkHttpClient;
import java.io.IOException;
import org.json.JSONObject;
import okhttp3.Response;
import okhttp3.Request;

/**
 * JWiki provides summary gathered from wikipedia for a certain topic 
 * represented by a single string keyword.
 * {@link https://github.com/viralvaghela/Jwiki} 
 */
public class JWiki {
    private String endPoint;
    String displayTitle="";
    String imageURL="";

    /**
     * This creates a new object with the subject to search for.
     * @param subject The subject to search for in wikipedia. 
     */
    public JWiki(String endPoint)
    {
        this.endPoint = endPoint;
    }

    /**
     * Communicates to the API to fetch the description of the topic passed. 
     */
    public String getData(String subject) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(this.endPoint+subject)
                .get()
                .build();
        String extractText = "";
        try {
            Response response=client.newCall(request).execute();
            String data = response.body().string();
            JSONObject jsonObject = new JSONObject(data);

            //get title from JSON response
            displayTitle= jsonObject.getString("displaytitle");

            //first create a image object and then get image URL
            JSONObject jsonObjectOriginalImage = jsonObject.getJSONObject("originalimage");
            imageURL= jsonObjectOriginalImage.getString("source");

            //get text
            extractText = jsonObject.getString("extract");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return extractText;
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

}
