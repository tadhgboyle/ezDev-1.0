package me.aberdeener.ezdev.ingest;

import me.aberdeener.ezdev.ezDev;

import java.util.HashMap;
import java.util.logging.Logger;

public class RunnableCreator {

    private static Logger logger = ezDev.getInstance().getLogger();
    private static HashMap<Integer, String> tokens = ScriptHandler.getTokens();

    public static void createRunnable(String token) {
        String timeout = tokens.get(tokens.get(token) + 1);
        if (timeout.matches("/[0-9]+[s|m|h]/")) {
            logger.info("Timeout valid: " + timeout);
        } else {
            logger.warning("Timeout is invalid: " + timeout);
        }
    }
}
