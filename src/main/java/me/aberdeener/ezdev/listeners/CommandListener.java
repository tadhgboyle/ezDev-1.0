package me.aberdeener.ezdev.listeners;

import me.aberdeener.ezdev.ezDev;
import me.aberdeener.ezdev.ingest.CommandCreator;
import me.aberdeener.ezdev.ingest.ScriptHandler;
import me.aberdeener.ezdev.utils.Exceptions;
import me.aberdeener.ezdev.utils.Regex;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;

public class CommandListener extends Command implements Listener {

    HashMap<String, Integer> commands = CommandCreator.getCommands();
    Logger logger = ezDev.getInstance().getLogger();
    HashMap<Integer, String> tokens = ScriptHandler.getTokens();
    TreeBidiMap<Integer, String> currentCommand = new TreeBidiMap<>();

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().substring(1);
        if (commands.containsKey(command)) {
            event.setCancelled(true);
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
        Player sender = null;
        if (commandSender instanceof ConsoleCommandSender) {
            logger.severe("ezDev only supports players right now.");
            return false;
        } else sender = (Player) commandSender;

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
        StringBuilder value_assembler = new StringBuilder();
        // TODO: This doesnt understand new lines, so everything until "end" will be counted as the value in next step. Need to either use a semicolon or stop at linebreak
        for (String token : currentCommand.values()) {
            // TODO: See what will happen with duplicate values/tokens
            if (currentCommand.getKey(token) > 3 && !token.equals("end")) {
                value_assembler.append(token + " ");
            } else if (token.equals("end")) continue;
        }
        String value = value_assembler.toString();

        /* Debugging:
        logger.info("action: " + action);
        logger.info("target: " + target);
        logger.info("value: " + value);
        */

        Matcher matcher = Regex.ACTION_QUOTES.matcher(value);

        String substring = ChatColor.translateAlternateColorCodes('&', value.substring(1, value.length() - 2));
        switch (action) {
            case "permission":
                if (matcher.matches()) {
                    value = substring;
                    if (sender.hasPermission(value)) logger.info(sender.getName() + " has permission " + value);
                    else {
                        sender.sendMessage(Bukkit.getPermissionMessage());
                        return false;
                    }
                } else {
                    Exceptions.invalidValue(sender, "permission", value, command);
                    return false;
                }
                break;
            case "tell":
                if (matcher.matches()) {
                    value = substring;
                    if (target.equals("sender")) sender.sendMessage(value);
                    else if (target.equals("all")) for (Player players : Bukkit.getOnlinePlayers()) players.sendMessage(value);
                    else {
                        Exceptions.invalidTarget(sender, "command", "tell", value);
                        return false;
                    }
                } else {
                    Exceptions.invalidValue(sender, "tell", value, command);
                    return false;
                }
                break;
            case "give":
                ////
                break;
            default:
                Exceptions.invalidAction(sender, action, "command");
                break;
        }
        currentCommand.clear();
        return true;
    }
}
