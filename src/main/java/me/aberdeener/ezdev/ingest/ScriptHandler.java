package me.aberdeener.ezdev.ingest;

import lombok.Getter;
import me.aberdeener.ezdev.ezDev;
import me.aberdeener.ezdev.utils.Exceptions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ScriptHandler {

    private static Logger logger = ezDev.getInstance().getLogger();
    @Getter
    public static HashMap<Integer, String> script_lines = new HashMap<>();

    public static void handle(File script) {
        // Loop all lines, add to hashmap. cmdListener class will then loop and split into seperated words
        try {
            Scanner scanner = new Scanner(script);
            int line_number = 1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isBlank()) continue;
                script_lines.put(line_number, line.trim());
                line_number++;
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        /*
         Identify headers from tokens, then pass to creation class
         We don't care about anything else as the creation classes will handle that
         */
        for (int line_position : script_lines.keySet()) {
            String line = script_lines.get(line_position);
            String token;
            try {
                token = line.substring(0, line.indexOf(" "));
            } catch (StringIndexOutOfBoundsException e) {
                continue;
            }
            if (verifyHeader(line_position)) {
                switch (token) {
                    case "command":
                        CommandCreator.createCommand(line_position);
                        break;
                    case "listener":
                        // ListenerCreator.createListener(line_position);
                        break;
                    case "repeat":
                        // RunnableCreator.createRunnable(token);
                        break;
                    default:
                        logger.severe("Invalid action " + token);
                        return;
                }
            }
        }
        ezDev.getActiveScripts().add(script.getName());
    }

    private static List<String> headers = Arrays.asList("command", "listener", "repeat");

    // Verifies the line starts with one of the three header types, and that the line ends with a colon and that the last line is "end"
    private static boolean verifyHeader(int position) {
        // Get line from pos
        String line = script_lines.get(position);
        // Get first word -> header
        String header = line.substring(0, line.indexOf(" "));
        // Get next line
        String nextLine = script_lines.get(position + 1);
        // TODO: Check if last token is "end" or last token is null.
        // If the next line does not exist, our header, even if valid, serves no purpose.
        if (nextLine == null) return false;
        // Make sure the header is one of the three, that the header line ends with a colon
        if (headers.contains(header) && line.endsWith(":")) return true;
        return false;
    }
}
