package me.aberdeener.ezdev.listeners;

import me.aberdeener.ezdev.ezDev;
import me.aberdeener.ezdev.ingest.CommandCreator;
import me.aberdeener.ezdev.ingest.ScriptHandler;
import me.aberdeener.ezdev.utils.Exceptions;
import me.aberdeener.ezdev.utils.Regex;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;

public class CommandListener extends Command implements Listener {

    HashMap<String, Integer> commands = CommandCreator.getCommands();
    Logger logger = ezDev.getInstance().getLogger();
    HashMap<Integer, String> script_lines = ScriptHandler.getScript_lines();
    TreeBidiMap<Integer, String> currentCommandLines = new TreeBidiMap<>();
    TreeBidiMap<Integer, String> currentLineTokens = new TreeBidiMap<>();

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
         Now that we have a command, we get the line it is on and split the words into a new "token" hashmap for further processing
         Then we will go by each token in our currentCommand HashMap and process it.
        */
        Player sender;
        if (commandSender instanceof ConsoleCommandSender) {
            logger.severe("ezDev only supports players right now.");
            return false;
        } else sender = (Player) commandSender;

        int line_position = commands.get(command);
        // currentPosition tracks the order of the lines that are in the command
        int currentPosition = 1;
        String nextLine = "";
        while (!nextLine.equals("end")) {
            nextLine = script_lines.get(line_position);
            // We manually add a semicolon here so that the next for() loop can detect
            currentCommandLines.put(currentPosition, nextLine);
            currentPosition++;
            line_position++;
        }

        for (int i : currentCommandLines.keySet()) {
            String line = currentCommandLines.get(i + 1);

            if (line.equals("end")) return true;

            int position = 1;
            for (String token : line.split(" ")) {
                currentLineTokens.put(position, token);
                position++;
            }

            // Main work here
            String action = currentLineTokens.get(1);
            String target = currentLineTokens.get(2);
            StringBuilder value_assembler = new StringBuilder();
            // Now assemble the value. We need everything after the action
            for (String token : currentLineTokens.values()) {
                // Check what happens with duplicate tokens (tell sender "hello there there sir"
                if (currentLineTokens.getKey(token) >= 3) {
                    value_assembler.append(token + " ");
                }
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
                        if (sender.hasPermission(value)) {
                            logger.info(sender.getName() + " has permission " + value);
                        } else {
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
                        else if (target.equals("all"))
                            for (Player players : Bukkit.getOnlinePlayers()) players.sendMessage(value);
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
                    int quantity;
                    try {
                        quantity = Integer.valueOf(currentLineTokens.get(3));
                    } catch (Exception e) {
                        // Thrown when quantity is not a #
                        Exceptions.invalidValue(sender, "give", currentLineTokens.get(3), command);
                        return false;
                    }
                    String item = currentLineTokens.get(4);
                    if (target.equals("sender"))
                        sender.getInventory().addItem(new ItemStack(Material.getMaterial(item), quantity));
                    else if (target.equals("all")) for (Player players : Bukkit.getOnlinePlayers())
                        sender.getInventory().addItem(new ItemStack(Material.getMaterial(item), quantity));
                    else {
                        Exceptions.invalidTarget(sender, "command", "give", value);
                        return false;
                    }
                    break;
                default:
                    Exceptions.invalidAction(sender, action, "command");
                    break;
            }
            currentLineTokens.clear();
        }
        return true;
    }
}
