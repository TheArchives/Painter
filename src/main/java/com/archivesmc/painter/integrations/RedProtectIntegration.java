package com.archivesmc.painter.integrations;

import com.OverCaste.plugin.RedProtect.API.RedProtectAPI;
import com.OverCaste.plugin.RedProtect.Region;
import com.archivesmc.painter.Painter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class RedProtectIntegration implements Integration {
    private final Painter plugin;

    public RedProtectIntegration(final Painter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canEdit(final Block block, final Player player) {
        Region region = RedProtectAPI.getRegion(block.getLocation());

        if (region == null) {
            return true;
        }

        return region.canBuild(player);
    }

    @Override
    public String getPluginName() {
        return "RedProtect";
    }

    @Override
    public boolean setUp() {
        Plugin redProtectPlugin = this.plugin.getServer().getPluginManager().getPlugin("RedProtect");

        return redProtectPlugin != null && redProtectPlugin.isEnabled();
    }

    @Override
    public void notifyNotAllowed(final Block block, final Player player) {
        Region region = RedProtectAPI.getRegion(block.getLocation());

        Map<String, String> args = new HashMap<>();

        args.put("name", region.getName());
        args.put("creator", region.getCreator());

        this.plugin.sendMessage(player, "redprotect_not_allowed", args);
    }

    @Override
    public void blockReplaced(final Block block, final Player player) {
        // Honey badger don't care
    }
}
