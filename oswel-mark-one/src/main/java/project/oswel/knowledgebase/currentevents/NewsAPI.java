package project.oswel.knowledgebase.currentevents;

import okhttp3.OkHttpClient;
import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONArray;
import okhttp3.Response;
import okhttp3.Request;

/**
 * This class allows connection to newsapi.org to parse news information
 * from top headlines. 
 * @author John Santos
 */
public class NewsAPI {
    private static OkHttpClient client = new OkHttpClient();
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
    public String[] getNewsByTopic(String topic) {

        String finalEndpoint = this.endPoint + 
                            String.format("everything?q=%s", topic) + 
                            String.format("&apiKey=%s", this.apiKey);

        Request request = new Request.Builder()
                .url(finalEndpoint)
                .get()
                .build();   

        String[] summary = new String[3];

        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.getString("status")
                    .equalsIgnoreCase("ok")) {
                JSONArray articles = jsonObject.getJSONArray("articles");
                JSONObject article = articles.getJSONObject(0);
                summary[0] = article.getString("author");
                summary[1] = article.getString("title");
                summary[2] = article.getString("description");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return summary;
    }

    /**
     * Returns the news topheadline description in the US.
     * @return The description of the news headline (String).
     */
    public String[] getNewsTopHeadline() {

        String finalEndpoint = this.endPoint + 
                            "top-headlines?country=us" + 
                            String.format("&apiKey=%s", this.apiKey);

        Request request = new Request.Builder()
                .url(finalEndpoint)
                .get()
                .build();   

        String[] summary = new String[3];

        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.getString("status")
                    .equalsIgnoreCase("ok")) {
                JSONArray articles = jsonObject.getJSONArray("articles");
                JSONObject article = articles.getJSONObject(0);
                summary[0] = article.getString("author");
                summary[1] = article.getString("title");
                summary[2] = article.getString("description");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return summary;
    }
}
