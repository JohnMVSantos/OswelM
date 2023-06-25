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
 * {@link https://github.com/visualcrossing/WeatherApi/blob/master/Java/com/
 * visualcrossing/weather/samples/TimelineApiForecastSample.java} 
 ******************************************************************************/
public class Weather {

    private static final String APIENDPOINT = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private String unitGroup = "metric";
    private String location;
    private String apiKey;

    // This contains the weather information for the dates passed.
    private JSONArray weatherInformation;
    private ZoneId zoneId;

    // The following members are weather information for the date prompted.
    private String dateTime;
    private double maxTemp;
    private double minTemp;
    private double temp;
    private double precip;
    private String condition;
    private String description;

    /**
     * Constructor
     * @param apiKey The api-key to access the API data.
     */
    public Weather(String apiKey, String location) { 
        this.apiKey = apiKey; 
        this.location = location;
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
    public void timelineRequestHttpClient(String startDate, String endDate) 
                                        throws WeatherFetchFailedException{
		StringBuilder requestBuilder = new StringBuilder(APIENDPOINT);
        try {
		    requestBuilder.append(
                URLEncoder.encode(
                    this.location, 
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
		
		JSONObject timelineResponse = new JSONObject(rawResult);
		this.zoneId = ZoneId.of(timelineResponse.getString("timezone"));
        this.weatherInformation = timelineResponse.getJSONArray("days");
	}

    /**
     * This method returns the weather information for the index of the day
     * specified. 
     * @param index This is the index of the days passed between startDate and
     * endDate. The startDate is at index 0. 
     */
    public void setWeatherInfoDay(String name) {
        int index = WeekDay.getWeekDayFromString(name.toLowerCase()).getIndex();
        JSONObject dayValue = this.weatherInformation.getJSONObject(index);
        ZonedDateTime datetime = ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(
                dayValue.getLong("datetimeEpoch")), 
                this.zoneId);
        this.dateTime = datetime.format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.maxTemp = dayValue.getDouble("tempmax");
        this.minTemp = dayValue.getDouble("tempmin");
        this.temp = dayValue.getDouble("temp");
        this.precip = dayValue.getDouble("precipprob");
        //this.condition = dayValue.getString("condition");
        this.description = dayValue.getString("description");
    }

    public double getMaxTemp() { return this.maxTemp; }
    public double getMinTemp() { return this.minTemp; }
    public double getTemp() { return this.temp; }
    public double getPrecipitation() { return this.precip; }
    public String getCondition() { return this.condition; }
    public String getDescription() { return this.description; }
    public String getDateTime() { return this.dateTime; }
 
}
