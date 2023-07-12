package project.oswel;


import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


import java.io.IOException;


public class OswelApi {

    private static OkHttpClient client = new OkHttpClient();
    private static JSONParser parser = new JSONParser();
    final String BASE_URL = "http://127.0.0.1:8000/api";
    

    public OswelApi() { }


    public JSONArray fetchAll() {

        JSONArray jsonArray = null;
        Request request = new Request.Builder()
                .url(BASE_URL + "/all")
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            jsonArray = (JSONArray)parser.parse(data);           
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public String getData(String category) { 

        String description = "[ERROR] Failed To Get Data";
        Request request = new Request.Builder()
                .url(BASE_URL + "/get/" + category)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            JSONObject jsonObject = (JSONObject) parser.parse(data);
            description = (String) jsonObject.get("description");
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return description;
    }

    public String postData(String category, String description) { 

        // RequestBody formBody = new FormEncodingBuilder()
        //     .add("category", category)
        //     .add("description", description)
        //     .build();

        String json = "{\"category\": \"weather\",\"description\":\"John\"}";

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
            .url(BASE_URL + "/get/" + category)
            .post(body)
            .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "ERROR: Cannot Post";
        

        
       


        // Callback callBack = new Callback() {

        //     @Override
        //     public void onFailure(Request request, IOException e) {
        //         // TODO Auto-generated method stub
        //         System.out.println(e.getMessage());
        //     }

        //     @Override
        //     public void onResponse(Response response) throws IOException {
        //         // TODO Auto-generated method stub
        //         // response.body().string()
        //         throw new IOException();
        //     }
        // };

        // try {
        //     client.newCall(request).execute();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
       



    //     new OkHttpClient().newCall(new Request.Builder()
    //         .addHeader("Content-Type", "application/json")
    //         .url("URL...")
    //         .post(RequestBody.create(
    //                 MediaType.parse("application/json; charset=utf-8"),
    //                 obj.toString())
    //         ).build())
    // .enqueue(new Callback() {
    //     @Override
    //     public void onFailure(Call call, IOException e) {
    //         Log.d("ZAKA",e.getMessage());
    //     }
    //     @Override
    //     public void onResponse(Call call, Response response) throws IOException {
    //         Log.d("ZAKA","Report : -> "+response.body().string());
    //     }
    // });
    }

    public void putData() { }

    public void deleteData() { }
    
}
