package com.archivesmc.painter.restrictors;

import com.archivesmc.archblock.api.ArchBlock;
import com.archivesmc.painter.Painter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class ArchBlockRestrictor implements BuildRestrictor {
    private final Painter plugin;
    private ArchBlock archBlockApi;

    public ArchBlockRestrictor(Painter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canEdit(Block block, Player player) {
        return this.archBlockApi.canEditBlock(block, player);
    }

    @Override
    public String getPluginName() {
        return "ArchBlock";
    }

    @Override
    public boolean setUp() {
        Plugin archBlockPlugin = this.plugin.getServer().getPluginManager().getPlugin("ArchBlock");

        if (archBlockPlugin != null && archBlockPlugin.isEnabled()) {
            this.archBlockApi = ((com.archivesmc.archblock.Plugin) archBlockPlugin).getApi();
            return true;
        }

        return false;
    }

    @Override
    public void notifyNotAllowed(Block block, Player player) {
        HashMap <String, String> args = new HashMap<>();

        args.put(
                "name",

                this.archBlockApi.getUsernameForUuid(
                        this.archBlockApi.getOwnerUUID(block)
                )
        );

        this.plugin.sendMessage(player, "archblock_not_allowed", args);
    }
}
