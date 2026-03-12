package edu.drexel.se311.calculator.observers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ServerConnection implements CalculatorObserver {

    private final String host;
    private final int    port;

    public ServerConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void onDisplayUpdate(String text) {
        // Do nothing
    }

    @Override
    public void onResultReady(int result, String expression) {
        sendToServer(expression + " = " + result);
    }

    @Override
    public void onError(String message) {
        // Do nothing
    }

    private void sendToServer(String resultString) {
        try (Socket socket = new Socket(host, port)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(resultString);

            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            String ack = in.readLine();
            System.out.println("Server ack: " + ack);

        } catch (IOException e) {
            System.err.println("Error sending to server: " + e.getMessage());
        }
    }
}