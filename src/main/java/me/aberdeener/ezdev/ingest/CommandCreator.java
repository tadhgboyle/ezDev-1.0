package me.aberdeener.ezdev.ingest;

import lombok.Getter;
import me.aberdeener.ezdev.ezDev;
import me.aberdeener.ezdev.listeners.CommandListener;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandCreator {

    private static Logger logger = ezDev.getInstance().getLogger();
    private static HashMap<Integer, String> tokens = ScriptHandler.getTokens();
    @Getter
    public static HashMap<String, Integer> commands = new HashMap<>();

    static Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");

    public static void createCommand(int position) {
        String label = tokens.get(position + 1).substring(0, (tokens.get(position + 1).length() - 1)).toLowerCase();
        Matcher matcher = pattern.matcher(label);
        if (matcher.matches()) {
            logger.info("Creating command: /" + label);
            if (!commands.containsKey(label)) {
                commands.put(label, position);
                Bukkit.getServer().getCommandMap().register(label, new CommandListener(label));
                // TODO: Inject to bukkit command map for tab completion
            } else {
                logger.severe("Command previously registered: " + label + ".");
            }
        } else {
            logger.severe("Command label contains invalid characters: " + label + ".");
        }
    }
}
