package xyz.benw.plugins.fouriermc.event.violation;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import xyz.benw.plugins.fouriermc.Violations.AggregatedViolation;

/**
 * Triggered when a player accumulates too many violations
 *
 * @author bcbwilla
 */
public class AggregatedViolationEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    private Player player;
    private AggregatedViolation aggregatedViolation;

    /**
     * Class constructor.
     * @param player player triggering event
     * @param aggregatedViolation aggregatedViolation
     */
    public AggregatedViolationEvent(Player player, AggregatedViolation aggregatedViolation) {
        this.player = player;
        this.aggregatedViolation = aggregatedViolation;
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

    public AggregatedViolation getAggregatedViolation() {
        return aggregatedViolation;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

}
