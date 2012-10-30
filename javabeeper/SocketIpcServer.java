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

    public synchronized boolean isListening() {
        return listening;
    }

    private ServerSocket serverSocket = null;

    public SocketIpcServer(final SnoozeController snoozeController) {
        this.snoozeController = snoozeController;
        
        createServerSocket();
    }
    
    private SnoozeController snoozeController;
    
    public synchronized void close() {
        listening = false;
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "Problem closing port:" + port, ex);
        }
    }

    private Thread serverThread = null;
    
    public void listen() {
        serverThread = new Thread() {
            @Override
            public void run() {
                Socket clientSocket = null;
                while(isListening()) {
                    try {
                        if(isListening()) {
                            clientSocket = serverSocket.accept();
                        } else {
                            break;
                        }
                        Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Received client connection {0}", clientSocket.getInetAddress());
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        out.println("Hello, this is the Snooze socket server.");
                        
                        snoozeController.addObserver(new ConnectionToClient(clientSocket));

                        String inputLine;
                        if((inputLine = in.readLine()) != null) {
                            double snoozeDuration = Double.parseDouble(inputLine);
                            Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Received snooze signal, duration (minutes): {0}", snoozeDuration);
                            out.println("OK, got " + snoozeDuration);
                            
                            snoozeController.restartSnoozing(Utilities.minutesToHoursMinutesSeconds(snoozeDuration));
                        }
                    } catch (IOException ex) {
                        if(snoozeController.isAlertAsSeparateProcess()) {
                            Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "Problem accepting input on server port: " + port, ex);
                        }
                    } 
                }
            }
        };
        serverThread.start();
    }
    
    public static void main (String[] args) {
        SocketIpcServer server = new SocketIpcServer(null);

                while(true) {
                    try {
                        Socket clientSocket = server.serverSocket.accept();

                        Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Received client connection {0}", clientSocket.getInetAddress());

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

    private void createServerSocket() {
        Random random = new Random();

        int retryCount = 0;
        
        while(!listening && retryCount < RETRY_COUNT_MAX) {
            port = random.nextInt() % 10000 + 20000;

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
        
        Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.INFO, "Successfully created server, listening on port: {0}", port);
    }
    
    class ConnectionToClient implements SnoozeObserver {
        private PrintWriter particularClientOut = null;
        private Socket clientSocket = null;

        private ConnectionToClient(Socket clientSocket) {
            try {
                this.clientSocket = clientSocket;
                particularClientOut = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException ex) {
                Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "Problem getting input, for client socket: " + clientSocket.getInetAddress(), ex);
            }
        }

        @Override
        public void setSnoozeDurationAndDoSnooze(double snoozeDurationMinutes) {
            particularClientOut.println(SnoozeController.SNOOZE_SOCKET_MESSAGE_CLIENT_CAN_QUIT);
            try {
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(SnoozeController.BEEPER_LOGGER_ID).log(Level.SEVERE, "Problem closing a particular client socket: " + clientSocket.getInetAddress(), ex);
            }
            
        }

        @Override
        public void setSnoozeDuration(double snoozeDurationMinutes) {
        }

        @Override
        public void setSoundEnabled(boolean soundEnabled) {
        }

        @Override
        public void itIsTimeToShowAlert() {
        }
        
    }
}
