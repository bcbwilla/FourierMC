package xyz.benw.plugins.fouriermc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by ben on 16/01/15.
 */
public class Collector implements Runnable {

    private FourierMC plugin;

    public Collector(FourierMC plugin) {
        this.plugin = plugin;
    }


    @Override
    public void run() {

        for(Player player : Bukkit.getOnlinePlayers()) {
            UUID playerID = player.getUniqueId();

            // Initialize spot to count clicks.
            // Each click before the next interval will
            // increment the value.
            if(plugin.clickLogger.containsKey(playerID)) {
                ClickData data = plugin.clickLogger.get(playerID);
                data.add(0);

            } else {
                plugin.clickLogger.put(playerID, new ClickData(plugin.getSamplePeriod()));
            }
        }

    }
}


