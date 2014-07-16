package com.archivesmc.painter.loggers;

import com.archivesmc.painter.Painter;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.entry.BlockChangeEntry;
import uk.co.oliwali.HawkEye.entry.BlockEntry;
import uk.co.oliwali.HawkEye.util.HawkEyeAPI;

public class HawkeyeLogger implements BlockLogger {

    private Painter plugin;

    public HawkeyeLogger(Painter plugin) {
        this.plugin = plugin;
    }

    @Override
    public void blockPainted(Player player, BlockState oldBlockState, BlockState newBlockState, Block block) {
        HawkEyeAPI.addEntry(
                this.plugin,
                new BlockEntry(
                        player, DataType.BLOCK_BREAK, block
                ));
        HawkEyeAPI.addEntry(
                this.plugin,
                new BlockChangeEntry(
                    player, DataType.BLOCK_PLACE,
                    block.getLocation(), oldBlockState,
                    newBlockState
                ));
    }

    @Override
    public String getPluginName() {
        return "Hawkeye";
    }

    @Override
    public boolean setup() {
        final PluginManager pm = this.plugin.getServer().getPluginManager();
        final Plugin hawkPlugin = pm.getPlugin("Hawkeye");

        return hawkPlugin != null && hawkPlugin.isEnabled();
    }
}
