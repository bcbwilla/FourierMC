package xyz.benw.plugins.fouriermc.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.benw.plugins.fouriermc.FourierMC;
import xyz.benw.plugins.fouriermc.player.PlayerData;
import xyz.benw.plugins.fouriermc.violation.AggregatedViolation;
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
    @SuppressWarnings("deprecation")
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


            } else if(args.length == 2 && args[0].equalsIgnoreCase("info")) {

                String nameString = args[1];
                UUID targetID = Bukkit.getPlayer(nameString).getUniqueId(); // Is there a better way to do this?

                PlayerData playerData = plugin.getPlayerData(targetID);

                sender.sendMessage(ChatColor.DARK_PURPLE + "Violation Report for " + nameString);

                String msg = "";
                for(ViolationType vt : ViolationType.values()) {

                    double velocity = 0;
                    int timesFailed = 0;

                    List<AggregatedViolation> violationList = playerData.getAggregatedViolations(vt);

                    if(violationList != null) {
                        for(AggregatedViolation av : violationList) {
                            timesFailed += av.getTimesFailed();
                            velocity += av.getFailedVelocity();
                        }

                        msg += vt.name() + ": \n";
                        msg += "  Times Failed: " + Integer.toString(timesFailed) + "\n";
                        msg += "  Velocity: " + Double.toString(velocity) + "\n\n";

                    }
                }

                msg = (msg == "") ? ChatColor.DARK_GREEN + " No violations found." : msg;
                sender.sendMessage(msg);

                return true;

            }

        }
        return false;
    }

}
