package me.aberdeener.ezdev.listeners;

import me.aberdeener.ezdev.ezDev;
import me.aberdeener.ezdev.ingest.CommandCreator;
import me.aberdeener.ezdev.ingest.ScriptHandler;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.logging.Logger;

public class CommandListener extends Command implements Listener {

    HashMap<String, Integer> commands = CommandCreator.getCommands();
    Logger logger = ezDev.getInstance().getLogger();
    HashMap<Integer, String> tokens = ScriptHandler.getTokens();
    TreeBidiMap<Integer, String> currentCommand = new TreeBidiMap<>();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().substring(1);
        if (commands.containsKey(command)) {
            logger.info(event.getPlayer().getName() + " ran ezDev command " + command);
            execute(event.getPlayer(), command, null);
        }
    }

    public CommandListener(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String command, String[] args) {
        /*
         Now that we have a command, we loop through our tokens HashMap starting from the command until we find "end"
         Then we will go by each token in our currentCommand HashMap and process it.
        */
        Player sender = (Player) commandSender;
        int position = commands.get(command) + 1;
        int currentPosition = 1;
        String nextToken = "";
        while (!nextToken.equals("end")) {
            nextToken = tokens.get(position);
            currentCommand.put(currentPosition, nextToken);
            currentPosition++;
            position++;
        }
        String action = currentCommand.get(2);
        String target = currentCommand.get(3);
        StringBuilder sb = new StringBuilder();
        // Now we create a string builder and assemble the value.
        for (String token : currentCommand.values()) {
            // TODO: See what will happen with duplicate values/tokens
            if (currentCommand.getKey(token) > 3 && !token.equals("end")) {
                sb.append(token + " ");
            } else if (token.equals("end")) continue;
        }
        String value = sb.toString();

        /*
        logger.info("action: " + action);
        logger.info("target: " + target);
        logger.info("value: " + value);
        */
        switch (action) {
            case "permission":
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    // TODO: Remove quotes from value!
                    value = value.substring(0, value.length() - 1);
                    if (sender.hasPermission(value)) logger.info(sender.getName() + " has permission " + value);
                    else sender.sendMessage(Bukkit.getPermissionMessage().toString());
                } else logger.severe("Value for " + command + " does not start or end with \".");
                break;
            case "tell":
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    if (target.equals("sender")) sender.sendMessage(value.substring(0, value.length() - 1));
                    else if (target.equals("all")) for (Player players : Bukkit.getOnlinePlayers())
                        players.sendMessage(value.substring(0, value.length() - 1));
                    else logger.severe("Invalid target for command " + command + ".");
                }
                break;
            case "give":
                ////
                break;
            default:
                // TODO: Custom Exceptions when action/etc isnt within the values (use switch "default")
                break;
        }
        currentCommand.clear();
        return true;
    }
}
