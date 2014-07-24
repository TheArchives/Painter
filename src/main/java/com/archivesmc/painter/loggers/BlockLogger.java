package com.archivesmc.painter.loggers;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

public interface BlockLogger {
    /**
     * Log the fact that a block has been painted.
     *
     * In most cases, we want to log a break and a placement.
     *
     * @param player The Player that painted the block
     * @param oldBlockState The BlockState of the block before it was painted
     * @param newBlockState The BlockState of the block after it was painted
     * @param block The Block itself, after it's been painted
     */
    public void blockPainted(Player player, BlockState oldBlockState, BlockState newBlockState, Block block);

    /**
     * Return the name of the block logging plugin in a proper, human-readable format.
     *
     * For example, "Prism", not "prism" or "prism-v123"
     *
     * @return The name of the logging plugin
     */
    public String getPluginName();

    /**
     * Set up the block logging plugin and prepare the API so that it's ready to be
     * used for logging.
     *
     * @return Whether the plugin was set up successfully.
     */
    public boolean setup();
}
