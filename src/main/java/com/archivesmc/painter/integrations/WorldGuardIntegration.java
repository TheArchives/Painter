package com.archivesmc.painter.integrations;

import com.archivesmc.painter.Painter;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

import static com.sk89q.worldguard.bukkit.BukkitUtil.toVector;

public class WorldGuardIntegration implements Integration {
    private final Painter plugin;
    private WorldGuardPlugin worldGuardPlugin;

    public WorldGuardIntegration(final Painter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canEdit(final Block block, final Player player) {
        Vector point = toVector(block);
        RegionManager regionManager = this.worldGuardPlugin.getRegionManager(block.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(point);

        return set.canBuild(this.worldGuardPlugin.wrapPlayer(player));
    }

    @Override
    public String getPluginName() {
        return "WorldGuard";
    }

    @Override
    public boolean setUp() {
        Plugin worldGuardPlugin = this.plugin.getServer().getPluginManager().getPlugin("WorldGuard");

        if (worldGuardPlugin != null && worldGuardPlugin.isEnabled()) {
            this.worldGuardPlugin = (WorldGuardPlugin) worldGuardPlugin;
            return true;
        }

        return false;
    }

    @Override
    public void notifyNotAllowed(final Block block, final Player player) {
        HashMap <String, String> args = new HashMap<>();

        args.put(
                "name", player.getDisplayName()
        );

        this.plugin.sendMessage(player, "worldguard_not_allowed", args);
    }

    @Override
    public void blockReplaced(final Block block, final Player player) {
        // Honey badger don't care
    }
}
