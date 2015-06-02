package com.archivesmc.painter.integrations;

import com.archivesmc.archblock.api.ArchBlock;
import com.archivesmc.archblock.wrappers.bukkit.BukkitBlock;
import com.archivesmc.archblock.wrappers.bukkit.BukkitPlayer;
import com.archivesmc.painter.Painter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class ArchBlockIntegration implements Integration {
    private final Painter plugin;
    private ArchBlock archBlockApi;

    public ArchBlockIntegration(final Painter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canEdit(final Block block, final Player player) {
        return this.archBlockApi.canEditBlock(new BukkitBlock(block), new BukkitPlayer(player));
    }

    @Override
    public String getPluginName() {
        return "ArchBlock";
    }

    @Override
    public boolean setUp() {
        Plugin archBlockPlugin = this.plugin.getServer().getPluginManager().getPlugin("ArchBlock");

        if (archBlockPlugin != null && archBlockPlugin.isEnabled()) {
            if (archBlockPlugin.getDescription().getVersion().equals("0.0.1")) {
                this.plugin.getLogger().warning("ArchBlock integration requires ArchBlock version 0.0.2 or later");
                return false;
            }

            this.archBlockApi = ((com.archivesmc.archblock.wrappers.bukkit.BukkitPlugin) archBlockPlugin).getApi();
            return true;
        }

        return false;
    }

    @Override
    public void notifyNotAllowed(final Block block, final Player player) {
        UUID uuid = this.archBlockApi.getOwnerUUID(new BukkitBlock(block));

        if (uuid == null) {
            return;
        }

        HashMap <String, String> args = new HashMap<>();

        args.put(
                "name", this.archBlockApi.getUsernameForUuid(uuid)
        );

        this.plugin.sendMessage(player, "archblock_not_allowed", args);
    }

    @Override
    public void blockReplaced(final Block block, final Player player) {
        if (this.archBlockApi.getOwnerUUID(new BukkitBlock(block)) != null) {
            this.archBlockApi.removeOwner(new BukkitBlock(block));
        }

        this.archBlockApi.setOwnerUUID(new BukkitBlock(block), player.getUniqueId());
    }
}
