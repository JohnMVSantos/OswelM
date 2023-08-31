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
import java.nio.charset.Charset;
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

    private static final Logger LOGGER = Logger
                                            .getLogger(
                                                WeatherAPI.class
                                                            .getName());
    private String endPoint;
    private String unitGroup = "metric";
    private String apiKey;
    private String location;

    // This contains the weather information for the dates passed.
    private JSONArray weatherInformation;

    /**
     * Creates an object to allow weather data fetching from the API given
     * the API key. 
     * @param apiKey The api-key to access the API data.
     */
    public WeatherAPI(String apiKey, String endPoint) { 
        this.apiKey = apiKey; 
        this.endPoint = endPoint;
    }

    /**
     * Returns the location for which the weather information was parsed.
     * @return The city location (String).
     */
    public String getLocation() { return this.location; }

    /**
     * This method sets the unitGroup to return the weather information.
     * @param unitGroup The unitGroup to set (String). 
     */
    public void setUnitGroup(String unitGroup) {
        this.unitGroup = unitGroup;
    }

    /**
     * This method requests weather information using the following API:
     * https://www.visualcrossing.com/weather-api
     * Dates should be in YYYY-MM-DD format.
     * @param startDate The start date to recieve weather information.
     * @param endDate The end date to recieve weather information.
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

            try {
                CloseableHttpResponse response = httpclient.execute(get); 
                String rawResult=null;
                try {
                    if (response.getStatusLine()
                                .getStatusCode() != HttpStatus.SC_OK) {
                        System.out.printf(
                            "Bad response status code:%d%n", 
                            response.getStatusLine().getStatusCode());
                        return;
                    }
                    
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        rawResult=EntityUtils.toString(
                            entity, Charset.forName("utf-8"));
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

        } catch(URISyntaxException e) {
            LOGGER.severe( 
                "Encountered URISyntaxException when " + 
                "instantiating a new URIBuilder");
            System.exit(1);
        }
	}

    /**
     * This method parses the JSON timeline to parse the individual contents
     * of the weather information.
     * @param rawResult This is the unparsed weather result. 
     */
	private void parseTimelineJson(String rawResult) {
		if (rawResult==null || rawResult.isEmpty()) {
			System.out.printf("No raw data%n");
			return;
		}
        JSONObject timelineResponse = new JSONObject(rawResult);
        this.weatherInformation = timelineResponse.getJSONArray("days");  
	}

    /**
     * This method returns the weather information for the index of the day
     * specified. 
     * @param index This is the index of the days passed between startDate and
     * endDate. The startDate is at index 0. 
     */
    public JSONObject getWeatherInfoDay(String name) {
        int index = WeekDay.getWeekDayFromString(name.toLowerCase()).getIndex();
        JSONObject weatherInfo = this.weatherInformation.getJSONObject(index);
        return weatherInfo;
    }
}
