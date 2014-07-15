package com.archivesmc.painter.loggers;

import com.archivesmc.painter.Painter;

import me.botsko.prism.Prism;
import me.botsko.prism.actionlibs.ActionFactory;
import me.botsko.prism.actionlibs.ActionType;
import me.botsko.prism.actionlibs.RecordingQueue;

import me.botsko.prism.actions.Handler;
import me.botsko.prism.exceptions.InvalidActionException;
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
        Handler paintAction = ActionFactory.createBlockChange("painter-block-paint",
                oldBlockState.getLocation(), oldBlockState.getTypeId(), oldBlockState.getRawData(),
                newBlockState.getTypeId(), newBlockState.getRawData(), player.getName()
        );

        RecordingQueue.addToQueue(paintAction);
    }

    @Override
    public String getPluginName() {
        return "Prism";
    }

    @Override
    public boolean setup() {
        Plugin pPlugin = this.plugin.getServer().getPluginManager().getPlugin("Prism");

        if (pPlugin != null && pPlugin.isEnabled()) {
            try {
                Prism.getActionRegistry().registerCustomAction(this.plugin,
                        new ActionType("painter-block-paint", true, true, true, "BlockChangeAction", "painted")
                );
            } catch (InvalidActionException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        return false;
    }
}
