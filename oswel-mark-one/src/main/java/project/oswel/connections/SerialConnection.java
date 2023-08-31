package project.oswel.connections;

import com.fazecast.jSerialComm.SerialPort;
import java.util.logging.Logger;
import java.io.IOException;

public class SerialConnection {
    
    private static final Logger LOGGER = Logger
                                            .getLogger(
                                                SerialConnection.class
                                                                .getName());
    private boolean connection = false;
    private SerialPort sp;	

    public SerialConnection(String comPort) {
		this.sp = SerialPort.getCommPort(comPort); 
		this.setConnectionParameters(
            115200, 0, 0);
        if (this.sp.openPort()) {
            this.connection = true;
        }  else {
			LOGGER.severe("Failed to establish serial connection.");
            System.exit(1);
		}		
    }

    public SerialConnection(
        String comPort, int baudrate, int readTimeout, int writeTimeout
    ) {
		this.sp = SerialPort.getCommPort(comPort); 
		this.setConnectionParameters(baudrate, readTimeout, writeTimeout);
        if (this.sp.openPort()) {
            LOGGER.info("Serial connection is established.");
            this.connection = true;
        }  else {
			LOGGER.severe("Failed to establish serial connection.");
            System.exit(1);
		}		    
    }

    private void setConnectionParameters(
        int baudrate, int readTimeout, int writeTimeout
    ) {
        this.sp.setComPortParameters(
			baudrate, 
			8, 
			1, 
			0); 
		this.sp.setComPortTimeouts(
			SerialPort.TIMEOUT_WRITE_BLOCKING, 
			readTimeout, 
			writeTimeout); 
    }

    public boolean checkConnection() { return this.connection; }

    public void writeBytes(byte[] input, int sleepTime) {
        try {
            this.sp.getOutputStream().write(input);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                LOGGER.severe(
                    "Sleep was interrupted during write operation");
                    System.exit(1);
            }
            this.sp.getOutputStream().flush();
        } catch (IOException e) {
            LOGGER.severe(
                "IOException occured during the write operation.");
                System.exit(1);
        }
    }

    public void closeConnection() {
        if (sp.closePort()) {
            LOGGER.info(
                "Serial connection is now closed.");
        } else {
            LOGGER.severe(
                "Failed to close serial connection.");
                System.exit(1);
        }
    }
}
