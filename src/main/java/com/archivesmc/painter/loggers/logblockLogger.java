package com.archivesmc.painter.loggers;

import com.archivesmc.painter.Painter;

import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.LogBlock;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class logblockLogger implements Logger {

    private Consumer consumer = null;
    private Painter plugin;

    public logblockLogger(Painter plugin) {
        this.plugin = plugin;
    }

    @Override
    public void blockPainted(Player player, BlockState oldBlockState, BlockState newBlockState, Block block) {
        this.consumer.queueBlockReplace(player.getName(), oldBlockState, newBlockState);
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
