package edu.drexel.se311.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class CalculatorServer {

    public static final String HOST = "localhost";
    public static final int    PORT = 9090;


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

    private void handle(Socket socket) {
        try (socket) {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String resultString = in.readLine();
            
            if (resultString != null) {
                System.out.println("Received result from client: " + resultString);
                
                out.println("OK");
            }

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new CalculatorServer().start();
    }
}
