package org.example;
import org.example.CommandProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static Map<String, Runway<Airplane>> runways = new HashMap<>();
    private static Map<String, Airplane> airplanes = new HashMap<>();

    public static void main(String[] args) {
        // I check if the test folder is missing from the command line
        if (args.length < 1) {
            System.err.println("Missing test folder argument.");
            return;
        }
        // I add the path
        String testFolder = "src/main/resources/" + args[0];
        String inputPath = testFolder + "/input.in";
        CommandProcessor commandProcessor = new CommandProcessor(runways, airplanes, testFolder);
        // Reading from the input file the command then processing it in CommandProcessor class
        try (BufferedReader br = new BufferedReader(new FileReader(inputPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                commandProcessor.processCommand(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }
}
