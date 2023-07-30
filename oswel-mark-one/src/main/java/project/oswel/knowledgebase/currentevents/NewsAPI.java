package project.oswel.knowledgebase.currentevents;

import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Request;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.IOException;

/**
 * This class allows connection to newsapi.org to parse news information
 * from top headlines. 
 * @author John Santos
 */
public class NewsAPI {
    private static OkHttpClient client = new OkHttpClient();
    private static JSONParser parser = new JSONParser();
    private String endPoint;
    private String apiKey;
    
    /**
     * Creates a new NewsAPI object given the api key.
     * @param apiKey This is the api key to allow data fetching from the API.
     */
    public NewsAPI (String apiKey, String endPoint) {
        this.apiKey = apiKey; 
        this.endPoint = endPoint;
    }

    /**
     * Returns the topheadline description for the specific topic set.
     * @param topic The topic to search for the current events.
     * @return The news description related to the topic passed (String).
     */
    public String getNewsByTopic(String topic) {

        String finalEndpoint = this.endPoint + 
                            String.format("everything?q=%s", topic) + 
                            String.format("&apiKey=%s", this.apiKey);

        String description = "[ERROR] Failed To Get Data";
        Request request = new Request.Builder()
                .url(finalEndpoint)
                .get()
                .build();   

        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            JSONObject jsonObject = (JSONObject) parser.parse(data);
            if (jsonObject.get("status")
                        .toString()
                        .equalsIgnoreCase("ok")) {
                JSONArray articles = (JSONArray) jsonObject.get("articles");
                JSONObject article = (JSONObject) articles.get(0);
                String author = (String) article.get("author");
                String title = (String) article.get("title");
                description = (String) article.get("description");
            }
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return description;
    }

    /**
     * Returns the news topheadline description in the US.
     * @return The description of the news headline (String).
     */
    public String getNewsTopHeadline() {

        String finalEndpoint = this.endPoint + 
                            "top-headlines?country=us" + 
                            String.format("&apiKey=%s", this.apiKey);

        String description = "[ERROR] Failed To Get Data";
        Request request = new Request.Builder()
                .url(finalEndpoint)
                .get()
                .build();   

        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            JSONObject jsonObject = (JSONObject) parser.parse(data);
            if (jsonObject.get("status")
                        .toString()
                        .equalsIgnoreCase("ok")) {
                JSONArray articles = (JSONArray) jsonObject.get("articles");
                JSONObject article = (JSONObject) articles.get(0);
                String author = (String) article.get("author");
                String title = (String) article.get("title");
                description = (String) article.get("description");
            }
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return description;
    }
}
