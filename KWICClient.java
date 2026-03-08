package edu.drexel.se311.kwic;

import edu.drexel.se311.kwic.io.Commands;
import edu.drexel.se311.kwic.io.ConsoleInput;
import edu.drexel.se311.kwic.io.ConsoleOutput;
import edu.drexel.se311.kwic.io.InputStrategy;
import edu.drexel.se311.kwic.io.OutputStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class KWICClient {
    private static final String SERVER_HOSTNAME = "localhost";
    private static final int SERVER_PORT = 1234;
    private static final int TIMEOUT_MS = 30000;

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    private InputStrategy input = new ConsoleInput();
    private OutputStrategy output = new ConsoleOutput();

    public static void main(String[] args) {
        KWICClient client = new KWICClient();
        client.connect();
        client.runClientLoop();
    }

    public KWICClient() {

    }

    public void connect() {
        try {
            socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);

            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
        } catch (ConnectException e) {
            output.display("Server is not running. Please start the server and try again.");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void runClientLoop() {
        input.open();
        while (true) { 
            System.out.print("> ");
            String line = input.getCommand();
            if (line == null || line.equals("exit")) {
                break;
            }
            if (line.trim().isEmpty()) {
                continue;
            }
            if (!line.startsWith(Commands.KEYWORD_SEARCH)) {
                output.display("Invalid command. Supported commands: keyword-search <keyword>");
                continue;
            }
            try {
                writer.write(line);
                writer.newLine();
                writer.flush();

                socket.setSoTimeout(TIMEOUT_MS);

                List<String> serverResponse = new ArrayList<>();
                String responseLine;
                String messageLinesStr = reader.readLine();
                if (messageLinesStr == null) {
                    output.display("Server closed connection.");
                    break;
                }
                int messageLines = Integer.parseInt(messageLinesStr);
                for (int i = 0; i < messageLines; i ++) {
                    responseLine = reader.readLine();
                    if (responseLine == null) {
                        output.display("Server closed connection.");
                        break;
                    }
                    serverResponse.add(responseLine);
                }
                output.display(serverResponse);

                socket.setSoTimeout(0);
            } catch (SocketTimeoutException e) {
                output.display("No response from server. Connection timed out.");
                break;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            writer.close();
            reader.close();
            socket.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}