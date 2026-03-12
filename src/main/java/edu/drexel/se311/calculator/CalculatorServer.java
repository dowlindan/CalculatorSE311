package edu.drexel.se311.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class CalculatorServer {

    public static final String HOST = "localhost";
    public static final int    PORT = 9090;

    private int successfulCalculations = 0;
    private final List<String> equations = new ArrayList<>();


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

    private synchronized void handle(Socket socket) {
        try (socket) {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String equationString = in.readLine();
            
            if (equationString != null) {
                successfulCalculations++;
                equations.add(equationString);
                
                System.out.println("Received equation from client: " + equationString);
                System.out.println("\n=== Calculation Summary ===");
                System.out.println("Total Successful Calculations: " + successfulCalculations);
                System.out.println("All Equations:");
                for (int i = 0; i < equations.size(); i++) {
                    System.out.println("  " + (i + 1) + ". " + equations.get(i));
                }
                System.out.println("===========================\n");
                
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
