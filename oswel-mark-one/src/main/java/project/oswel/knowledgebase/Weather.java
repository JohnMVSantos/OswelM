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
import org.json.JSONObject;
import java.io.IOException;
import java.net.URLEncoder;
import org.json.JSONArray;
import java.time.Instant;
import java.time.ZoneId;

/*******************************************************************************
 * Class that contains methods fetch current or historical weather information.
 * Source was found in: 
 * https://github.com/visualcrossing/WeatherApi/blob/master/Java/com/
 * visualcrossing/weather/samples/TimelineApiForecastSample.java
 ******************************************************************************/
public class Weather {

    private static final String APIENDPOINT = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private String unitGroup = "metric";
    private String location;
    // Dates should be in YYYY-MM-DD format.
    private String startDate;
    private String endDate;
    private String apiKey;

    // This contains the weather information for the dates passed.
    private JSONArray weatherInformation;

    /**
     * Constructor
     * @param apiKey The api-key to access the API data.
     * @param startDate The start date to recieve weather information.
     * @param endDate The end date to recieve weather information.
     */
    public Weather(String apiKey, String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.apiKey = apiKey;
    }

    /**
     * This method requests weather information using the following API:
     * https://www.visualcrossing.com/weather-api
     * @throws WeatherFetchFailedException This exception is thrown when the 
     *            following exceptions are thrown: 
     *            UnsupportedEncodingException,URISyntaxException, IOException.
     */
    public void timelineRequestHttpClient() throws WeatherFetchFailedException{
		StringBuilder requestBuilder = new StringBuilder(APIENDPOINT);
        try {
		    requestBuilder.append(
                URLEncoder.encode(this.location, StandardCharsets.UTF_8.toString()));
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
		
		JSONObject timelineResponse = new JSONObject(rawResult);
		ZoneId zoneId=ZoneId.of(timelineResponse.getString("timezone"));
        this.weatherInformation = timelineResponse.getJSONArray("days");

		
		JSONArray values=timelineResponse.getJSONArray("days");
		
		System.out.printf("Date\tMaxTemp\tMinTemp\tPrecip\tSource%n");
		for (int i = 0; i < values.length(); i++) {
			JSONObject dayValue = values.getJSONObject(i);
            
            ZonedDateTime datetime=ZonedDateTime.ofInstant(Instant.ofEpochSecond(dayValue.getLong("datetimeEpoch")), zoneId);
            
            double maxtemp=dayValue.getDouble("tempmax");
            double mintemp=dayValue.getDouble("tempmin");
            double pop=dayValue.getDouble("precip");
            String source=dayValue.getString("source");
            System.out.printf("%s\t%.1f\t%.1f\t%.1f\t%s%n", datetime.format(DateTimeFormatter.ISO_LOCAL_DATE), maxtemp, mintemp, pop,source );
        }
	}

    /**
     * This method returns the weather information for the index of the day
     * specified. 
     * @param index This is the index of the days passed between startDate and
     * endDate. The startDate is at index 0. 
     */
    private void getWeatherInfoDay(int index) {

    }



    
}
