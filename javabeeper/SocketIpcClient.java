package javabeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;

public class SocketIpcClient implements SnoozeObserver {
    
    private final int port;

    private BufferedReader in = null;
    private Socket snoozeServerSocket = null;
    private PrintWriter out = null;
    
    public SocketIpcClient(int port) {

        this.port = port;
        try {
            snoozeServerSocket = new Socket("127.0.0.1", port);
            out = new PrintWriter(snoozeServerSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(snoozeServerSocket.getInputStream()));
            java.util.logging.Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Connected to: 127.0.0.1:{0}", port);
            String receivedString = in.readLine();
            java.util.logging.Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Received from: 127.0.0.1: {0}", receivedString);
        } catch (UnknownHostException e) {
            java.util.logging.Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "Don't know about host: 127.0.0.1.", e);
            System.exit(1);
        } catch (IOException e) {
            java.util.logging.Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "I/O problem for the connection to: 127.0.0.1:" + port, e);
            System.exit(1);
        }
        


    }    
    
    public static void main(String[] args) {
        SocketIpcClient s = new SocketIpcClient(21007);
        s.sendTimeMinutes(26.1);
    }
    
    public void sendTimeMinutes(double minutes) {

            out.println(minutes);
            java.util.logging.Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Transmitted datum: {0} to 127.0.0.1:{1}", new Object[]{minutes, port});
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
