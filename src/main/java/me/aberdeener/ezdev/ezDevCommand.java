package me.aberdeener.ezdev;

import me.aberdeener.ezdev.variables.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ezDevCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getLabel().equalsIgnoreCase("ezDev")) {
            if (!sender.hasPermission("ezdev.admin")) {
                sender.sendMessage(Bukkit.getPermissionMessage());
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(ChatColor.DARK_GREEN + "--== [ezDev] ==--");
                sender.sendMessage(ChatColor.YELLOW + "Active Scripts: (" + ChatColor.DARK_GREEN + ezDev.getActiveScripts().size() + ChatColor.YELLOW + ")");
                for (String activeScript : ezDev.getActiveScripts()) {
                    sender.sendMessage(ChatColor.GOLD + activeScript);
                }
            }
            if (args.length == 1 && args[0].equals("reload")) {
                sender.sendMessage(ChatColor.YELLOW + "Reloading user variables...");
                VariableManager.initVariables();
                sender.sendMessage(ChatColor.GREEN + "Reloaded user variables!");
            }
        }
        return true;
    }
}
