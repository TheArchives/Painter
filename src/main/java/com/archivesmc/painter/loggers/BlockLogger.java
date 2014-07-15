package com.archivesmc.painter.loggers;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public interface BlockLogger {
    /**
     * Log the fact that a block has been painted.
     * In most cases, we want to log a break and a placement.
     *
     * @param player
     * The player that's painting the block
     *
     * @param oldBlockState
     * The block that's being painted
     *
     * @param newBlockState
     * The block that we're painting over it
     *
     * @param block
     * The final block
     */
    public void blockPainted(Player player, BlockState oldBlockState, BlockState newBlockState, Block block);
    public String getPluginName();
    public boolean setup();
}
