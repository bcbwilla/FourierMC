package xyz.benw.plugins.fouriermc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

/**
 * General event listener for plugin
 *
 * @author bcbwilla
 */
public class ClickListener implements Listener {

    private FourierMC plugin;

    /**
     * Class constructor
     * @param plugin instance of FouirerMC plugin
     */
    public ClickListener(FourierMC plugin) {
        this.plugin = plugin;
    }


    /**
     * Logs left click events and puts data in user's ClickData object
     * @param event player interacting event
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        UUID playerID = event.getPlayer().getUniqueId();
        Action action = event.getAction();
        
        /* Ignore physical and right click actions from increasing the click logger. */
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            /* Each click increments the value corresponding to the current sample period. */
            if (plugin.clickLogger.containsKey(playerID)) {
                plugin.clickLogger.get(playerID).increment();
            }
        }
    }
    
    /**
     * Logs block place events and puts data in user's ClickData object
     * @param event player block place event
     */
    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent event) {
    	UUID playerID = event.getPlayer().getUniqueId();
    	
        /* Each click increments the value corresponding to the current sample period. */
        if (plugin.clickLogger.containsKey(playerID)) {
        	plugin.clickLogger.get(playerID).increment();
        }
    }

    /**
     * Handles player data when player leaves server
     * @param event player quit event
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID playerID = event.getPlayer().getUniqueId();

        if (plugin.clickLogger.containsKey(playerID) || plugin.violationLogger.containsKey(playerID)) {
            plugin.clickLogger.remove(playerID);
        }
    }

}
