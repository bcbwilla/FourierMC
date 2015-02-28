package xyz.benw.plugins.fouriermc.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.benw.plugins.fouriermc.FourierMC;

import java.util.Map;

/**
 * Basic config related commands
 *
 * @author bcbwilla
 */
public class ConfigCommands implements CommandExecutor {

    private FourierMC plugin;
    private FileConfiguration config;

    public ConfigCommands(FourierMC plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fmc")) {

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
           }
        }
        return false;
    }

}
