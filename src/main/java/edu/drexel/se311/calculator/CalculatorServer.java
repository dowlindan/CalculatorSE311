package edu.drexel.se311.calculator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import edu.drexel.se311.calculator.net.CalculatorProtocolMessage;

/**
 * CALCULATOR SERVER
 *
 * Listens on localhost:9090 for incoming CalculatorProtocolMessage
 * objects. For each REQUEST received it:
 *
 *   1. Deserializes the CalculatorProtocolMessage from the socket
 *   2. Extracts the ExpressionNode tree from the message
 *   3. Runs the visitor pipeline  (Validate → PrettyPrint → Evaluate)
 *   4. Sends back a RESPONSE or ERROR CalculatorProtocolMessage
 *
 * Each connection is handled on its own thread so the server can
 * serve multiple clients concurrently.
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
            // Deserialize the incoming request message
            ObjectInputStream  in  = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            CalculatorProtocolMessage request =
                (CalculatorProtocolMessage) in.readObject();


            CalculatorProtocolMessage response = process(request);

            // Serialize and send the response message back
            out.writeObject(response);
            out.flush();

            // System.out.println("Sent response [" + response.getRequestId() + "] "
            //     + "type=" + response.getType());

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    // ── Process request → visitor pipeline → build response ──────────────

    private CalculatorProtocolMessage process(CalculatorProtocolMessage request) {
        if (request.getType() != CalculatorProtocolMessage.Type.REQUEST) {
            return CalculatorProtocolMessage.error(
                "Expected REQUEST, got " + request.getType()
            );
        }

        // Under the new client‑rich design the request payload is simply
        // a string containing the calculation result.  The server does not
        // attempt to evaluate anything; it merely logs the value and
        // returns an acknowledgement message.
        String resultString = request.getPayload();
        System.out.println("Received result from client: " + resultString);

        // We could add further logging or persistence here.  Always reply
        // with a simple ack so the client knows the message made it.
        return CalculatorProtocolMessage.response("OK");
    }

    // ── Entry point ───────────────────────────────────────────────────────

    public static void main(String[] args) {
        new CalculatorServer().start();
    }
}