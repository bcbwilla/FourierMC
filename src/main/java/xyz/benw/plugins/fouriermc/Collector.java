package xyz.benw.plugins.fouriermc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Each sample period, Collector initializes a spot in each player's DataCollector object
 * for clicking data to be collected.
 *
 * @author bcbwilla
 */
public class Collector implements Runnable {

    private FourierMC plugin;

    /**
     * Class constructor
     * @param plugin instance of FourierMC plugin
     */
    public Collector(FourierMC plugin) {
        this.plugin = plugin;
    }

    /**
     * Run the collector
     */
    @Override
    public void run() {

        for(Player player : Bukkit.getOnlinePlayers()) {
            UUID playerId = player.getUniqueId();
            for(ClickData data : plugin.getPlayerData(playerId).getClickSignals()) {
                data.add(0);
            }
        }

    }
}


