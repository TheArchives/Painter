package com.archivesmc.painter.loggers;

import com.archivesmc.painter.Painter;

import de.diddiz.LogBlock.Actor;
import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class LogblockLogger implements BlockLogger {

    private Consumer consumer = null;
    private final Painter plugin;

    public LogblockLogger(final Painter plugin) {
        this.plugin = plugin;
    }

    @Override
    public void blockPainted(final Player player, final BlockState oldBlockState,
                             final BlockState newBlockState, final Block block) {
        this.consumer.queueBlockReplace(Actor.actorFromEntity(player), oldBlockState, newBlockState);
    }

    @Override
    public String getPluginName() {
        return "LogBlock";
    }

    @Override
    public boolean setup() {
        final PluginManager pm = this.plugin.getServer().getPluginManager();
        final Plugin lbPlugin = pm.getPlugin("LogBlock");

        if (lbPlugin != null && lbPlugin.isEnabled()) {
            this.consumer = ((LogBlock) lbPlugin).getConsumer();
            return true;
        }
        return false;
    }
}
