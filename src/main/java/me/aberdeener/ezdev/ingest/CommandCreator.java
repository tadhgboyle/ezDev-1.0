package me.aberdeener.ezdev.ingest;

import lombok.Getter;
import me.aberdeener.ezdev.ezDev;
import me.aberdeener.ezdev.listeners.CommandListener;
import me.aberdeener.ezdev.utils.Regex;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;

public class CommandCreator {

    private static Logger logger = ezDev.getInstance().getLogger();
    private static HashMap<Integer, String> tokens = ScriptHandler.getTokens();
    @Getter
    public static HashMap<String, Integer> commands = new HashMap<>();


    public static void createCommand(int position) {
        String label = tokens.get(position + 1).substring(0, (tokens.get(position + 1).length() - 1)).toLowerCase();
        Matcher matcher = Regex.ALPHA_NUM.matcher(label);
        if (matcher.matches()) {
            logger.info("Creating command: /" + label);
            if (!commands.containsKey(label)) {
                commands.put(label, position);
                // TODO: Set namespace in bukkit commandmap so that each command isnt hi:hi but rather ezdev:hi
                Bukkit.getServer().getCommandMap().register(label, new CommandListener(label));
            } else {
                logger.severe("Command previously registered: " + label + ".");
            }
        } else {
            logger.severe("Command label contains invalid characters: " + label + ".");
        }
    }
}
