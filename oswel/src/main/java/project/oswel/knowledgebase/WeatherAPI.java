/* Oswell backend Java Application.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

package project.oswel.knowledgebase;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import org.apache.http.util.EntityUtils;
import java.net.URISyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import project.oswel.time.WeekDay;
import java.util.logging.Logger;
import java.io.IOException;
import java.net.URLEncoder;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Class that contains methods fetch current or historical weather information.
 * {@link https://github.com/visualcrossing/WeatherApi/blob/master/Java/com/
 * visualcrossing/weather/samples/TimelineApiForecastSample.java} 
*/
public class WeatherAPI {

    private static final Logger LOGGER = Logger.getLogger(WeatherAPI.class.getName());
    private String endPoint;
    private String unitGroup = "metric";
    private String apiKey;
    private String location;

    // This contains the weather information for the dates passed.
    private JSONArray weatherInformation;

    /**
     * Creates an object to allow weather data fetching from the API given
     * the API key. 
     * @param apiKey The api-key to access the API data (String).
     * @param endPoint The endpoint to access the API (String).
     */
    public WeatherAPI(String apiKey, String endPoint) { 
        this.apiKey = apiKey; 
        this.endPoint = endPoint;
    }

    /**
     * This method sets the unitGroup to return the weather information.
     * @param unitGroup The unitGroup to set (String). 
     */
    public void setUnitGroup(String unitGroup) {
        this.unitGroup = unitGroup;
    }

    /**
     * Returns the location for which the weather information was parsed.
     * @return The city location (String).
     */
    public String getLocation() { return this.location; }

    /**
     * This method returns the weather information for the index of the day
     * specified. 
     * @param name The name of the week in the day to retrieve the weather information (String).
     * @return The weather information for that day of the week (JSONObject).
     */
    public JSONObject getWeatherInfoDay(String name) {
        int index = WeekDay.getWeekDayFromString(name.toLowerCase()).getIndex();
        return this.weatherInformation.getJSONObject(index);
    }

    /**
     * This method parses the JSON timeline to parse the individual contents
     * of the weather information.
     * @param rawResult This is the unparsed weather result (String). 
     */
	private void parseTimelineJson(String rawResult) {
		if (rawResult==null || rawResult.isEmpty()) {
            String warningMsg = String.format(
                "No raw weather data captured in %s%n",this.location);
            LOGGER.warning(warningMsg);
			return;
		}
        JSONObject timelineResponse = new JSONObject(rawResult);
        this.weatherInformation = timelineResponse.getJSONArray("days");  
	}
    
    /**
     * Processes the string raw result from the client into a JSONObject.
     * @param get Http get object (HttpGet).
     * @param httpclient Http client object to execute API calls for the response (CloseableHttpClient).
     */
    private void processRawResult(HttpGet get, CloseableHttpClient httpclient){
        try {
            CloseableHttpResponse response = httpclient.execute(get); 
            String rawResult=null;
            try {
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    String warningMsg = String.format(
                        "Bad response status code:%d%n", 
                        response.getStatusLine().getStatusCode()
                    );
                    LOGGER.warning(warningMsg);
                    return;
                }
                
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    rawResult=EntityUtils.toString(
                        entity, StandardCharsets.UTF_8);
                }
                
            } finally {
                response.close();
            }
            parseTimelineJson(rawResult);   

        } catch(IOException e) {
            LOGGER.severe(
                "Encountered an IOException when trying " + 
                "to recieve a response from the HTTP client.");
            System.exit(1);
        }
    }

    /**
     * This method requests weather information using the following API:
     * https://www.visualcrossing.com/weather-api
     * Dates should be in YYYY-MM-DD format.
     * @param startDate The start date to recieve weather information (String).
     * @param endDate The end date to recieve weather information (String).
     * @param location The location to retrieve weather information (String).
     */
    public void timelineRequestHttpClient(
            String startDate, String endDate, String location
    ) {
		StringBuilder requestBuilder = new StringBuilder(this.endPoint);
        this.location = location;
        try {
		    requestBuilder.append(
                URLEncoder.encode(
                    location, 
                    StandardCharsets.UTF_8.toString()));
        } catch(UnsupportedEncodingException e) {
            LOGGER.severe( 
                "Encountered an UnsupportedEncodingException " +
                "when encoding the specified location.");
            System.exit(1);
        }
		
		if (startDate!=null && !startDate.isEmpty()) {
			requestBuilder.append("/").append(startDate);
			if (endDate!=null && !endDate.isEmpty()) {
				requestBuilder.append("/").append(endDate);
			}
		}

		try {
		    URIBuilder builder = new URIBuilder(requestBuilder.toString());
            builder.setParameter("unitGroup", unitGroup)
                    .setParameter("key", apiKey);

            HttpGet get = new HttpGet(builder.build());
            CloseableHttpClient httpclient = HttpClients.createDefault();
            this.processRawResult(get, httpclient);

        } catch(URISyntaxException e) {
            LOGGER.severe( 
                "Encountered URISyntaxException when " + 
                "instantiating a new URIBuilder");
            System.exit(1);
        }
	}
}
