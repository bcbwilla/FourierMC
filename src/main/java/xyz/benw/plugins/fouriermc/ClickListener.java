package xyz.benw.plugins.fouriermc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

/**
 * Created by ben on 15/01/15.
 */
public class ClickListener implements Listener {

    private FourierMC plugin;

    public ClickListener(FourierMC plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        UUID playerID = event.getPlayer().getUniqueId();

        if(plugin.clickLogger.containsKey(playerID)) {
            plugin.clickLogger.get(playerID).increment();
        }


    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        UUID playerID = event.getPlayer().getUniqueId();

        if(plugin.clickLogger.containsKey(playerID)) {
            plugin.clickLogger.remove(playerID);
        }
    }




}
