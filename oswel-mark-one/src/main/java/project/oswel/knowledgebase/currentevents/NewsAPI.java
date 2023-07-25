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
 * 
 */
public class NewsAPI {
    private static OkHttpClient client = new OkHttpClient();
    private static JSONParser parser = new JSONParser();
    private String apiKey;
    private String endPoint = "https://newsapi.org/v2/";
    

    public NewsAPI (String apiKey) {
        this.apiKey = apiKey; 
    }

    public String getNewsByTopic(String topic) {

        String finalEndpoint = this.endPoint + String.format("everything?q=%s", topic) + 
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
            if (jsonObject.get("status").toString().equalsIgnoreCase("ok")) {
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

    public String getNewsTopHeadline() {

        String finalEndpoint = this.endPoint + "top-headlines?country=us" + 
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
            if (jsonObject.get("status").toString().equalsIgnoreCase("ok")) {
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
