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

public class ChatGPT {

    private static OkHttpClient client = new OkHttpClient();
    private static JSONParser parser = new JSONParser();
    private String apiKey;
    private String endPoint = "https://api.deepai.org/api/text-generator";

    public ChatGPT(String apiKey) {
        this.apiKey = apiKey; 
    }

    public ChatGPT(String apiKey, String endPoint) {
        this.apiKey = apiKey;
        this.endPoint = endPoint;
    }

    public void setEndpoint(String endPoint) { this.endPoint = endPoint; }

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
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            JSONObject jsonObject = (JSONObject) parser.parse(data);
            description = (String) jsonObject.get("output");
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return description;
        }
}
