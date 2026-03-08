package edu.drexel.se311.kwic;

import edu.drexel.se311.kwic.configreading.*;
import edu.drexel.se311.kwic.fileparsing.AbstractFileParser;
import edu.drexel.se311.kwic.io.*;
import edu.drexel.se311.kwic.sorting.*;
import java.net.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class KWICServer {
    private static final int PORT_NUMBER = 1234;
    private static final String LOG_FILE = "./outputs/log.txt";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: (java exec) <config-filename>");
            System.exit(1);
        }
        
        String configFilename = args[0];

        try {
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            while (true) { 
                System.out.println("Listening for new connections.");
                Socket client = serverSocket.accept();
                System.out.println("One client connected with IP: " + client.getInetAddress());
                KWICDriver driver = fromServerConfig(configFilename, LOG_FILE, client);
                KWICRequestHandler handler = new KWICRequestHandler(client, driver);
                handler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    

    public static KWICDriver fromServerConfig(String configFilename, String loggingFile, Socket client) {
        OptionReader.readOptions(configFilename);
        
        String inputFile = OptionReader.getString("InputFileName");
        String inputObjString = OptionReader.getString("Input");
        String sortingType = OptionReader.getString("Order");
        String wordFiltering = OptionReader.getString("WordFiltering");
        String trivialWords = OptionReader.getString("TrivialWords");
        
        AbstractFileParser fileParser = (AbstractFileParser) OptionReader.getObjectFromKey(inputObjString);

        SortingStrategy sortingStrategy;
        if ("Ascending".equals(sortingType)) {
            sortingStrategy = new AlphabeticSorter();
        } else if ("Descending".equals(sortingType)) {
            sortingStrategy = new ReverseAlphabeticSorter();
        } else {
            System.err.println("Unsupported sorting order");
            System.exit(1);
            return null;
        }

        boolean filterWords;
        if ("Yes".equals(wordFiltering)) {
            filterWords = true;
        } else if ("No".equals(wordFiltering)) {
            filterWords = false;
        } else {
            System.err.println("Unsupported word filtering choice");
            System.exit(1);
            return null;
        }
        InputStrategy inputStrategy;
        OutputStrategy outputStrategy;
        try {
            inputStrategy = new KWICInputStream(client.getInputStream());
            outputStrategy = new KWICOutputStream(client.getOutputStream(), loggingFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
        
        
        Set<String> trivialWordSet = new HashSet<>(Arrays.asList(trivialWords.split(",")));
        KWICDriver driver = new KWICDriver(inputFile, fileParser, inputStrategy, outputStrategy, sortingStrategy, filterWords, trivialWordSet, null);
        return driver;
    }
}