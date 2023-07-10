package project.oswel;


import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Request;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.io.IOException;


public class OswelApi {


    final String BASE_URL="http://127.0.0.1:8000/api/all/";

    public OswelApi()
    {
       
    }

    public void fetchAll() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL)
                .get()
                .build();
        try {
            Response response=client.newCall(request).execute();
            String data = response.body().string();
            //JSONArray dataArray = new JSONArray(data);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(data);

           
            
  
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    public void getData() { }

    public void postData() { }

    public void putData() { }

    public void deleteData() { }
    
}
