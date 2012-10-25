package javabeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketIpcServer {
    public static final int RETRY_COUNT_MAX = 100;
    private int port;
    private boolean listening = false;
    private BufferedReader in;

    private ServerSocket serverSocket = null;

    public SocketIpcServer(SnoozeController snoozeController) {
        this.snoozeController = snoozeController;
        
        Random random = new Random();

        int retryCount = 0;
        
        while(!listening && retryCount < RETRY_COUNT_MAX) {
            port = random.nextInt() % 10000 + 10000;

            try {
                retryCount++;
                serverSocket = new ServerSocket(port);
                listening = true;
            } catch (IOException ex) {
                Logger.getLogger(SocketIpcServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (!listening) {
            Logger.getLogger(SocketIpcServer.class.getName()).log(Level.SEVERE, null, new IOException("Couldn't create server"));
            System.exit(1);
        }

        Socket clientSocket = null;
        while(true) {
            try {
                clientSocket = serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                if((inputLine = in.readLine()) != null) {
                    snoozeController.setSnoozeDurationMinutes(Double.parseDouble(inputLine));
                }
            } catch (IOException ex) {
                Logger.getLogger(SocketIpcServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private SnoozeController snoozeController;
    
    public void close() {
        try {
            in.close();
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketIpcServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
