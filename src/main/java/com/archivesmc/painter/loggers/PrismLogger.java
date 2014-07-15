package com.archivesmc.painter.loggers;

import com.archivesmc.painter.Painter;

import me.botsko.prism.actionlibs.RecordingQueue;
import me.botsko.prism.actions.BlockChangeAction;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PrismLogger implements BlockLogger {

    private Painter plugin;

    public PrismLogger(Painter plugin) {
        this.plugin = plugin;
    }

    @Override
    public void blockPainted(Player player, BlockState oldBlockState, BlockState newBlockState, Block block) {
        BlockChangeAction breakAction = new BlockChangeAction();
        BlockChangeAction placeAction = new BlockChangeAction();

        breakAction.setActionType("block-break");
        breakAction.setLoc(block.getLocation());
        breakAction.setBlock(oldBlockState);
        breakAction.setPlayerName(player);

        placeAction.setActionType("block-place");
        placeAction.setLoc(block.getLocation());
        placeAction.setBlock(newBlockState);
        placeAction.setPlayerName(player);

        RecordingQueue.addToQueue(breakAction);
        RecordingQueue.addToQueue(placeAction);
    }

    @Override
    public String getPluginName() {
        return "Prism";
    }

    @Override
    public boolean setup() {
        Plugin pPlugin = this.plugin.getServer().getPluginManager().getPlugin("Prism");

        return pPlugin != null && pPlugin.isEnabled();
    }
}
