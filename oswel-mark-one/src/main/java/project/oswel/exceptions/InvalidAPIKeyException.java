package project.oswel.exceptions;

 /*******************************************************************************
 * This exception is thrown when throughout the validation of the Oswel  
 * license contains an invalid key.
 * @author John Santos
 ******************************************************************************/
public class InvalidAPIKeyException extends Exception {

    /**
     * Constructor
     * @param error This is the description of the error that occurs. 
     */
    public InvalidAPIKeyException (String error) {
        super(error);
    } 
}
