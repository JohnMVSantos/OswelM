package project.oswel.nlp;

import com.squareup.okhttp.FormEncodingBuilder;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Request;
import org.json.simple.JSONObject;
import java.io.IOException;

/**
 * This class communicates to the endpoint api.deepai.org which runs 
 * ChatGPT on the POST requests with the prompt to generate a response.
 * @author John Santos
 */
public class ChatGPT {

    private String endPoint = "https://api.deepai.org/api/text-generator";
    private OkHttpClient client = new OkHttpClient();
    private JSONParser parser = new JSONParser();
    private String apiKey;

    /**
     * Creates a ChatGPT object to allow usage of the methods to post 
     * requests to generate a response from ChatGPT.
     * @param apiKey The API key to allow usage of the application.
     */
    public ChatGPT(String apiKey) {
        this.apiKey = apiKey; 
    }

    /**
     * Creates a ChatGPT object specifying the endpoint which hosts
     * an API which runs ChatGPT to allow POST requests to generate a 
     * response.
     * @param apiKey The API Key required for the specific endpoint.
     * @param endPoint The specified endpoint to call.
     */
    public ChatGPT(String apiKey, String endPoint) {
        this.apiKey = apiKey;
        this.endPoint = endPoint;
    }

    /**
     * Sets the endpoint to use. 
     * @param endPoint The string endpoint to set.
     */
    public void setEndpoint(String endPoint) { this.endPoint = endPoint; }

    /**
     * POST requests to the specified endpoint to generate a ChatGPT response.
     * @param prompt The prompt to send to the endpoint. 
     * @return The string describing the ChatGPT response. 
     */
    public String getGPTResponse(String prompt) {

        RequestBody formBody = new FormEncodingBuilder()
            .add("text", prompt)
            .build();

        String description = "[ERROR] Failed To Get Data";
        Request request = new Request.Builder()
                .header("api-key", this.apiKey)
                .url(this.endPoint)
                .post(formBody)
                .build();   

        try {
            Response response = this.client.newCall(request).execute();
            String data = response.body().string();
            JSONObject jsonObject = (JSONObject) this.parser.parse(data);
            description = (String) jsonObject.get("output");
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return description;
    }
}
