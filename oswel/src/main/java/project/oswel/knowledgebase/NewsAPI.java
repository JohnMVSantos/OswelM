/* Oswell backend Java Application.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

package project.oswel.knowledgebase;

import org.json.JSONException;
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
     * @param apiKey This is the api key to allow data fetching from the API (String).
     * @param endPoint This is the endpoint to access the API (String).
     */
    public NewsAPI (String apiKey, String endPoint) {
        this.apiKey = apiKey; 
        this.endPoint = endPoint;
    }

    /**
     * Returns the topheadline description for the specific topic set.
     * @param topic The topic to search for the current events (String).
     * @return An array of strings containing author, title, and description (Stringp[]).
     */
    public String[] getNewsByTopic(String topic) {
        String finalEndpoint = this.endPoint + 
                            String.format("everything?q=%s", topic) + 
                            String.format("&apiKey=%s", this.apiKey);
        return this.initiateRequest(finalEndpoint);
    }

    /**
     * Returns the news topheadline description in the country.
     * @param countryCode The country code to access top headline (String).
     * @return An array of strings containing author, title, and description (String[]).
     */
    public String[] getNewsTopHeadline(String countryCode) {
        String finalEndpoint = this.endPoint + 
                            "top-headlines?country=" + countryCode + 
                            String.format("&apiKey=%s", this.apiKey);
        return this.initiateRequest(finalEndpoint);
    }

    /**
     * Populates the summary object with the extracted information of the client response.
     * @param summary This contains the author, title, and description of the 
     *  top headline in the country (String[]).
     * @param article This is the client request response from the API (JSONObject).
     */
    private void populateSummary(String[] summary, JSONObject article){
        try {
            summary[0] = article.getString("author");
            summary[1] = article.getString("title");
            summary[2] = article.getString("description");
        } catch(JSONException e) {
            summary[2] = "Could not find a description for this article";
        }
    }

    /**
     * Performs the request based on the finalEndpoint of the API.
     * @param finalEndpoint The endpoint to access in the API (String).
     * @return An array of strings containing author, title, and description (String[]).
     */
    private String[] initiateRequest(String finalEndpoint) {
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
                this.populateSummary(summary, article);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return summary;
    }
}
