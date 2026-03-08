package edu.drexel.se311.kwic;

import java.net.Socket;

public class KWICRequestHandler extends Thread {
    private Socket clientSocket;
    private KWICDriver driver;

    public KWICRequestHandler(Socket clientSocket, KWICDriver driver) {
        this.clientSocket = clientSocket;
        this.driver = driver;
    }

    @Override
    public void run() {
        runClientServiceLoop();
    }

    public void runClientServiceLoop() {
        try {
            driver.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}