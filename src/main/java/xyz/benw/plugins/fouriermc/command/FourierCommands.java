package xyz.benw.plugins.fouriermc.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.benw.plugins.fouriermc.FourierMC;
import xyz.benw.plugins.fouriermc.violation.Violation;
import xyz.benw.plugins.fouriermc.violation.ViolationType;

import java.util.*;

/**
 * Basic config related commands
 *
 * @author bcbwilla
 */
public class FourierCommands implements CommandExecutor {

    private FourierMC plugin;
    private FileConfiguration config;

    public FourierCommands(FourierMC plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fmc")) {

            if (!sender.hasPermission("fmc")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
                Map<String, Object> configParams = config.getValues(true);

                sender.sendMessage(ChatColor.DARK_AQUA + "FourierMC Config Settings:");
                for (Map.Entry<String, Object> entry : configParams.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if(!(value instanceof MemorySection)) {
                        sender.sendMessage(" " + key + ": " + value.toString());
                    }
                }
                return true;

            } else if(args.length == 2 && args[0].equalsIgnoreCase("get")) {
                sender.sendMessage(args[1] + ": " + config.get(args[1]));
                return true;

            } else if(args.length == 1 && args[0].equalsIgnoreCase("reloadconfig")) {
                plugin.reloadConfig();
                sender.sendMessage("Reloaded config for FourierMC.");
                return true;
            }

        }
        return false;
    }

}
