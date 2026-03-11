package edu.drexel.se311.calculator.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import edu.drexel.se311.calculator.observers.CalculatorObserver;

/**
 * SERVER CONNECTION
 *
 * Client-side observer that sends the computed result string to the
 * server for logging when a calculation completes.
 * Networking happens synchronously; callers are responsible for
 * executing on a background thread if needed.
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
        // Send the result to the server for logging
        CalculatorProtocolMessage message = CalculatorProtocolMessage.request(String.valueOf(result));
        sendToServer(message);
    }

    @Override
    public void onError(String message) {
        // Could potentially log error to server, but not required
    }

    private void sendToServer(CalculatorProtocolMessage message) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream out = new ObjectOutputStream(
                socket.getOutputStream()
            );
            out.writeObject(message);
            out.flush();

            ObjectInputStream in = new ObjectInputStream(
                socket.getInputStream()
            );
            CalculatorProtocolMessage response =
                (CalculatorProtocolMessage) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            // In case of error, log it but don't crash
            System.err.println("Error sending to server: " + e.getMessage());
        }
    }
}