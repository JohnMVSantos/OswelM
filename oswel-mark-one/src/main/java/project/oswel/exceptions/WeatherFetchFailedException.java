package project.oswel.exceptions;

/**
 * This exception is thrown when throughout the processing of fetching the 
 * westher data fails with the specified string error.
 * @author John Santos
 */
public class WeatherFetchFailedException extends Exception {
    /**
     * Constructor
     * @param error This is the description of the error that occurs. 
     */
    public WeatherFetchFailedException(String error) {
        super(error);
    } 
}
