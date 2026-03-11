package edu.drexel.se311.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * CALCULATOR SERVER
 *
 * Listens on localhost:9090 for incoming client connections.
 * For each connection it:
 *
 *   1. Reads a result string from the client
 *   2. Logs the result for auditing
 *   3. Sends back an acknowledgement
 *
 * Under the client-rich architecture, the server does not evaluate
 * expressions — it merely receives result strings and logs them.
 *
 * Each connection is handled on its own thread for concurrent clients.
 */
public class CalculatorServer {

    public static final String HOST = "localhost";
    public static final int    PORT = 9090;

    // ── Start listening ───────────────────────────────────────────────────

    public void start() {
        System.out.println("CalculatorServer listening on " + HOST + ":" + PORT);

        try (ServerSocket serverSocket = new ServerSocket(
                PORT, 50, InetAddress.getByName(HOST))) {

            while (true) {
                Socket client = serverSocket.accept();
                new Thread(() -> handle(client)).start();
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    // ── Handle one client connection ──────────────────────────────────────

    private void handle(Socket socket) {
        try (socket) {
            // Read result string from client
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String resultString = in.readLine();
            
            if (resultString != null) {
                // Log the result
                System.out.println("Received result from client: " + resultString);
                
                // Send acknowledgement
                out.println("OK");
            }

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    // ── Entry point ───────────────────────────────────────────────────────

    public static void main(String[] args) {
        new CalculatorServer().start();
    }
}
