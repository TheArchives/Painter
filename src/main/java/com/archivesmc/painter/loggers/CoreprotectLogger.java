package com.archivesmc.painter.loggers;

import com.archivesmc.painter.Painter;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CoreprotectLogger implements BlockLogger {

    private final Painter plugin;
    private CoreProtectAPI api;

    public CoreprotectLogger(final Painter plugin) {
        this.plugin = plugin;
    }

    @Override
    public void blockPainted(final Player player, final BlockState oldBlockState,
                             final BlockState newBlockState, final Block block) {
        this.api.logRemoval(player.getName(), oldBlockState.getLocation(), oldBlockState.getTypeId(), oldBlockState.getData().getData());
        this.api.logPlacement(player.getName(), newBlockState.getLocation(), newBlockState.getTypeId(), newBlockState.getData().getData());
    }

    @Override
    public String getPluginName() {
        return "CoreProtect";
    }

    @Override
    public boolean setup() {
        Plugin cpPlugin = this.plugin.getServer().getPluginManager().getPlugin("CoreProtect");

        if (cpPlugin != null && cpPlugin.isEnabled()) {
            this.api = ((CoreProtect) cpPlugin).getAPI();
            return true;
        }
        return false;
    }
}
