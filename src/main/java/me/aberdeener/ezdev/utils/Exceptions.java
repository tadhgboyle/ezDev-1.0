package me.aberdeener.ezdev.utils;

import me.aberdeener.ezdev.ezDev;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Logger;

public class Exceptions {

    static Logger logger = ezDev.getInstance().getLogger();

    public static void invalidAction(CommandSender sender, String header, String action) {
        // Invalid action (telport) for header listener.
        String message = "Invalid action (" + action + "), with header " + header + "";
        logger.severe(message);
        sender.sendMessage(ChatColor.DARK_RED + message);
    }
    public static void invalidTarget(CommandSender sender, String header, String action, String target) {
        // Invalid target (blah) for action tell, with header command.
        String message = "Invalid target (" + target + ") for action " + action + ", with header " + header + ".";
        logger.severe(message);
        sender.sendMessage(ChatColor.DARK_RED + message);
    }
    public static void invalidValue(CommandSender sender, String action, String value, String header) {
        // Value for permission "%43_sv_games" for command /hi does not start or end with ".
        String message = "Value for " + action + " " + value + ", for " + header + " does not start or end with \"";
        logger.severe(message);
        sender.sendMessage(ChatColor.DARK_RED + message);
    }
    public static void invalidNumber(CommandSender sender, String action, String value, String header) {
        String message = "Number for " + action + " " + value + ", for " + header + " is not valid.";
        logger.severe(message);
        sender.sendMessage(ChatColor.DARK_RED + message);
    }
}
