/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javabeeper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rbri053
 */
public class SocketIpcClient {
    private PrintWriter out = null;
    private final int port;
    
    public SocketIpcClient(int port) {

        this.port = port;
        try {
            try (Socket echoSocket = new Socket("120.0.0.1", port)) {
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                out.close();
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: taranis.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: taranis.");
            System.exit(1);
        }


    }    
    
    public void sendTimeMinutes(double minutes) {
        out.println(minutes);
    }
}
