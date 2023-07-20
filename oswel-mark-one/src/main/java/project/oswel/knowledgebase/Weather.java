package project.oswel.knowledgebase;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpGet;
import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;
import java.nio.charset.StandardCharsets;
import org.apache.http.util.EntityUtils;
import java.net.URISyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import project.oswel.exceptions.WeatherFetchFailedException;
import project.oswel.knowledgebase.schedule.WeekDay;

import java.io.IOException;
import java.net.URLEncoder;
import org.json.simple.JSONArray;
import java.time.Instant;
import java.time.ZoneId;

/*******************************************************************************
 * Class that contains methods fetch current or historical weather information.
 * {@link https://github.com/visualcrossing/WeatherApi/blob/master/Java/com/
 * visualcrossing/weather/samples/TimelineApiForecastSample.java} 
 ******************************************************************************/
public class Weather {

    private static final String APIENDPOINT = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private String unitGroup = "metric";
    private String apiKey;
    private String location;

    // This contains the weather information for the dates passed.
    private JSONArray weatherInformation;
    private ZoneId zoneId;

    /**
     * Constructor
     * @param apiKey The api-key to access the API data.
     */
    public Weather(String apiKey) { 
        this.apiKey = apiKey; 
    }

    /**
     * This method requests weather information using the following API:
     * https://www.visualcrossing.com/weather-api
     * Dates should be in YYYY-MM-DD format.
     * @param startDate The start date to recieve weather information.
     * @param endDate The end date to recieve weather information.
     * @throws WeatherFetchFailedException This exception is thrown when the 
     *            following exceptions are thrown: 
     *            UnsupportedEncodingException,URISyntaxException, IOException.
     */
    public void timelineRequestHttpClient(
            String startDate, String endDate, String location) 
                                        throws WeatherFetchFailedException{
		StringBuilder requestBuilder = new StringBuilder(APIENDPOINT);
        this.location = location;
        try {
		    requestBuilder.append(
                URLEncoder.encode(
                    location, 
                    StandardCharsets.UTF_8.toString()));
        } catch(UnsupportedEncodingException e) {
            throw new WeatherFetchFailedException(
                "Encountered an UnsupportedEncodingException " +
                "when encoding the specified location.");
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
                throw new WeatherFetchFailedException(
                    "Encountered an IOException when trying " + 
                    "to recieve a response from the HTTP client.");
            }

        } catch(URISyntaxException e) {
            throw new WeatherFetchFailedException(
                "Encountered URISyntaxException when " + 
                "instantiating a new URIBuilder");
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

        JSONParser parser = new JSONParser();  
        JSONObject timelineResponse;
        try {
            timelineResponse = (JSONObject) parser.parse(rawResult);
            this.weatherInformation = (JSONArray) timelineResponse.get("days");
        } catch (ParseException e) {
            e.printStackTrace();
        }  
	}

    /**
     * This method returns the weather information for the index of the day
     * specified. 
     * @param index This is the index of the days passed between startDate and
     * endDate. The startDate is at index 0. 
     */
    public JSONObject getWeatherInfoDay(String name) {
        int index = WeekDay.getWeekDayFromString(name.toLowerCase()).getIndex();
        JSONObject weatherInfo = (JSONObject) this.weatherInformation.get(index);
        // ZonedDateTime datetime = ZonedDateTime.ofInstant(
        //     Instant.ofEpochSecond(
        //         weatherInfo.getLong("datetimeEpoch")), 
        //         this.zoneId);
        return weatherInfo;
    }

    public String getLocation() {
        return this.location;
    }
}
