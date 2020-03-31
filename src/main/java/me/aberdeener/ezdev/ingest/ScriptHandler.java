package me.aberdeener.ezdev.ingest;

import lombok.Getter;
import me.aberdeener.ezdev.ezDev;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class ScriptHandler {

    private static Logger logger = ezDev.getInstance().getLogger();
    @Getter
    public static HashMap<Integer, String> tokens = new HashMap<>();

    public static void handle(File script) {
        // Loop all lines, add text to tokens then go to next line and repeat
        try {
            BufferedReader reader = new BufferedReader(new FileReader(script));
            String line = reader.readLine();
            int position = 1;
            while (line != null) {
                for (String token : line.split(" ")) {
                    if (token.isBlank()) continue;
                    tokens.put(position, token.trim());
                    position++;
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         Identify headers from tokens, then pass to creation class
         We don't care about anything else as the creation classes will handle that
         */
        for (int position : tokens.keySet()) {
            String token = tokens.get(position);
            if (verifyHeader(position)) {
                switch (token) {
                    case "command":
                        CommandCreator.createCommand(position);
                        break;
                    case "listener":
                        ListenerCreator.createListener(position);
                        break;
                    case "repeat":
                        RunnableCreator.createRunnable(token);
                        break;
                }
            }
        }
    }

    private static List<String> headers = Arrays.asList("command", "listener", "repeat");

    // Verifies the token is one of the three header types, and that the next token ends with a colon
    private static boolean verifyHeader(int position) {
        String token = tokens.get(position);
        String nextToken = tokens.get(position + 1);
        if (nextToken == null) return false;
        if (nextToken.endsWith(":") && headers.contains(token)) {
            logger.info("Valid header: " + token + " -> " + nextToken);
            return true;
        }
        return false;
    }
}
