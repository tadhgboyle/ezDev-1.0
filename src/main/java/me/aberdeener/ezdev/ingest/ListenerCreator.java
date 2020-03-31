package me.aberdeener.ezdev.ingest;

import me.aberdeener.ezdev.ezDev;

import java.util.HashMap;
import java.util.logging.Logger;

public class ListenerCreator {

    private static Logger logger = ezDev.getInstance().getLogger();
    private static HashMap<Integer, String> tokens = ScriptHandler.getTokens();

    public static void createListener(int position) {
        String event = tokens.get(position + 1);
        switch (event) {
            case "join":
                logger.info("Join Listener event is valid: " + event);
                break;
            case "leave":
                logger.info("Leave Listener event is valid: " + event);
                break;
            case "chat":
                logger.info("Chat Listener event is valid: " + event);
                break;
            default:
                logger.warning("Listener event is not valid: " + event);
        }
    }
}
