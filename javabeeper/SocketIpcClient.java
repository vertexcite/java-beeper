/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javabeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;

/**
 *
 * @author rbri053
 */
public class SocketIpcClient implements SnoozeObserver {
    
    private final int port;

    private PrintWriter out = null;
    private BufferedReader in = null;
    
    public SocketIpcClient(int port) {

        this.port = port;
        try {
            try (Socket snoozeServerSocket = new Socket("127.0.0.1", port)) {
                out = new PrintWriter(snoozeServerSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(snoozeServerSocket.getInputStream()));
            }
        } catch (UnknownHostException e) {
            java.util.logging.Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "Don't know about host: 127.0.0.1.", e);
            System.exit(1);
        } catch (IOException e) {
            java.util.logging.Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "I/O problem for the connection to: 127.0.0.1:" + port, e);
            System.exit(1);
        }
        java.util.logging.Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Connected to: 127.0.0.1:{0}", port);


    }    
    
    public static void main(String[] args) {
        SocketIpcClient s = new SocketIpcClient(21007);
        s.sendTimeMinutes(26.1);
    }
    
    public void sendTimeMinutes(double minutes) {
//        try {
//            String reply = in.readLine();

            out.println(minutes);
            out.println();
            out.println();
            out.println();
            out.println();
            out.println();
            out.println();
            out.println();
            out.flush();
            java.util.logging.Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Transmitted datum: {0} to 127.0.0.1:{1}", new Object[]{minutes, port});
//            out.flush();
            
//            java.util.logging.Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Got reply: {0}", reply);

            out.close();
            

//        } catch (IOException ex) {
//            java.util.logging.Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "Problem trying to transmit datum.", ex);
//        }
        
    }

    @Override
    public void setSnoozeDurationAndDoSnooze(double snoozeDurationMinutes) {
        sendTimeMinutes(snoozeDurationMinutes);
    }
    
    

    @Override
    public void setSoundEnabled(boolean soundEnabled) {
    }

    @Override
    public void setSnoozeDuration(double snoozeDurationMinutes) {
    }
}
