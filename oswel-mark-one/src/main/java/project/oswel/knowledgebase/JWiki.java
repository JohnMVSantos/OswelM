/* Oswell backend Java Application.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

package project.oswel.knowledgebase;

import org.json.JSONException;
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
    private String displayTitle = "";
    private String imageURL = "";

    /**
     * This creates a new object with the subject to search for.
     * @param subject The subject to search for in wikipedia (String). 
     */
    public JWiki(String endPoint) { this.endPoint = endPoint; }

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
     * Assigns the imageURL variable in the class. 
     * @param jsonObject Client response as a JSONObject.
     */
    private void assignImageURL(JSONObject jsonObject) {
        try {
            JSONObject jsonObjectOriginalImage = jsonObject
                                        .getJSONObject("originalimage");
            imageURL= jsonObjectOriginalImage
                                        .getString("source");
        } catch (JSONException e) {
            imageURL = "None";
        }
    }

    /**
     * Communicates to the API to fetch the description of the topic passed. 
     * @param subject The subject to get a definition in Wikipedia (String). 
     * @return The extracted definition of the subject (String).
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

            // Get title from JSON response.
            displayTitle = jsonObject.getString("displaytitle");

            // First create a image object and then get image URL.
            this.assignImageURL(jsonObject);
            
            // Get the text.
            extractText = jsonObject.getString("extract");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return extractText;
    }
}
