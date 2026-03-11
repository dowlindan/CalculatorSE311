package edu.drexel.se311.calculator.observers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * SERVER CONNECTION
 *
 * Client-side observer that sends calculation results to the server
 * for logging. Communicates via text-based protocol over sockets.
 *
 * When a calculation completes, sends the result string to the server
 * and waits for an acknowledgement response.
 */
public class ServerConnection implements CalculatorObserver {

    private final String host;
    private final int    port;

    public ServerConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void onDisplayUpdate(String text) {
        // Display updates don't need to be sent to server
    }

    @Override
    public void onResultReady(int result) {
        // Send the result string to the server for logging
        sendToServer(String.valueOf(result));
    }

    @Override
    public void onError(String message) {
        // Could potentially log error to server, but not required
    }

    /**
     * Send result string to server and receive acknowledgement.
     * Runs synchronously; caller is responsible for threading if needed.
     */
    private void sendToServer(String resultString) {
        try (Socket socket = new Socket(host, port)) {
            // Send result string to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(resultString);

            // Receive acknowledgement
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            String ack = in.readLine();
            System.out.println("Server ack: " + ack);

        } catch (IOException e) {
            // In case of error, log but don't crash
            System.err.println("Error sending to server: " + e.getMessage());
        }
    }
}