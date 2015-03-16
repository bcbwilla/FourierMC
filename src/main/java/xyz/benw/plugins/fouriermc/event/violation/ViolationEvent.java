package xyz.benw.plugins.fouriermc.event.violation;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.benw.plugins.fouriermc.Violations.Violation;


/**
 * Triggered when a player fails a clicking test
 *
 * @author bcbwilla
 */
public class ViolationEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private Player player;
    private Violation violation;

    /**
     * Class constructor.
     * @param player player triggering event
     * @param violation violation
     */
    public ViolationEvent(Player player, Violation violation) {
        this.player = player;
        this.violation = violation;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Violation getViolation() {
        return violation;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}
