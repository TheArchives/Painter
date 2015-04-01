package com.archivesmc.painter.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * @author Gareth Coles
 *
 * Paint event. Fire this if you want to paint something.
 */
public class PaintEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Player player;
    private Block block;
    private ItemStack item;

    public PaintEvent(final Player player, final ItemStack item, final Block block) {
        super();

        this.player = player;
        this.item = item;
        this.block = block;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    // Getters and setters for custom data

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(final ItemStack item) {
        this.item = item;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(final Block block) {
        this.block = block;
    }
}
