package javabeeper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketIpcServer {
    public static final int RETRY_COUNT_MAX = 100;
    private int port;

    public int getPort() {
        return port;
    }
    private boolean listening = false;

    private ServerSocket serverSocket = null;

    public SocketIpcServer(final SnoozeController snoozeController) {
        this.snoozeController = snoozeController;
        
        Random random = new Random();

        int retryCount = 0;
        
        while(!listening && retryCount < RETRY_COUNT_MAX) {
//            port = random.nextInt() % 10000 + 10000;
            port = 21007;

            try {
                retryCount++;
                serverSocket = new ServerSocket(port);
                listening = true;
            } catch (IOException ex) {
                Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "Problem creating server socket on port " + port, ex);
            }
        }
        if (!listening) {
            Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "Problem creating server, number of retries: " +retryCount, new IOException("Couldn't create server"));
            System.exit(1);
        }
        
        Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Successfully created server, listening on port: " + port);

    }
    
    private SnoozeController snoozeController;
    
    public void close() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "Problem closing port:" + port, ex);
        }

    }

    public void listen() {
        new Thread() {
            public void run() {
                Socket clientSocket = null;
                while(true) {
                    try {
                        clientSocket = serverSocket.accept();
                        Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Received client connection " + clientSocket.getInetAddress());
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                        String inputLine;
                        if((inputLine = in.readLine()) != null) {
                            double snoozeDuration = Double.parseDouble(inputLine);
                            Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Received snooze signal, duration (minutes): {0}", snoozeDuration);
                            out.println("OK, got " + snoozeDuration);
                            
                            snoozeController.restartSnoozing(Utilities.minutesToHoursMinutesSeconds(snoozeDuration));
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "Problem accepting input on server port: " + port, ex);
                    }
                }
            }
        }.start();
    }
    
    public static void main (String[] args) {
        SocketIpcServer server = new SocketIpcServer(null);

                while(true) {
                    try {
                        Socket clientSocket = server.serverSocket.accept();

                        Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Received client connection " + clientSocket.getInetAddress());

                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                        String inputLine;
                        if((inputLine = in.readLine()) != null) {
                            double snoozeDuration = Double.parseDouble(inputLine);
                            Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Received snooze signal, duration (minutes): {0}", snoozeDuration);
                            out.println("OK, got " + snoozeDuration);
                            
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "Problem accepting input on server port: " + server.port, ex);
                    }
                }
        
        
    }
}
