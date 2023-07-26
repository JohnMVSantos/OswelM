package project.oswel.speechrecognition.recognizer;

import project.oswel.speechrecognition.microphone.Microphone;
import javax.sound.sampled.LineUnavailableException;

/**
 * Class that acts as a thread to recognize user input.
 * @author John Santos
 */
public class Recognize implements Runnable {
    
    /**
     * This object contains methods that can handle speech recognition using
     * Google's duplex speech API.
     */
    private GSpeechDuplex duplex;

    /**
     * This object provides methods that can capture audio from the 
     * microphone.
     */
    private Microphone mic;

    /**
     * Constructor
     * 
     * @param duplex
     *      Use this GSpeechDuplex object to connect with the mic and allow
     *      Google API to perform data recognition.
     * @param mic
     *      This Microphone object allows fetching audio from the 
     *      microphone.
     */
    public Recognize(GSpeechDuplex duplex, Microphone mic) {
        this.duplex = duplex;
        this.mic = mic;
    }
    
    /**
     * This method overrides the method for the Runnable interface which 
     * executes this code whenever the Thread is started. 
     * This thread allows reading from the microphone and fetching
     * recognized data. 
     */
    public void run() {
        try {
            duplex.recognize(mic.getTargetDataLine(), mic.getAudioFormat());
        } catch (LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
