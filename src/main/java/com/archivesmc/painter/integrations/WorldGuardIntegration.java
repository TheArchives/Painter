package com.archivesmc.painter.integrations;

import com.archivesmc.painter.Painter;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.BukkitUtil;
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

    public WorldGuardIntegration(Painter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canEdit(Block block, Player player) {
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
    public void notifyNotAllowed(Block block, Player player) {
        HashMap <String, String> args = new HashMap<>();

        args.put(
                "name", player.getDisplayName()
        );

        this.plugin.sendMessage(player, "worldguard_not_allowed", args);
    }

    @Override
    public void blockReplaced(Block block, Player player) {
        // Honey badger don't care
    }
}
