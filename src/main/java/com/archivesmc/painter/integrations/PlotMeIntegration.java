package com.archivesmc.painter.integrations;

import com.archivesmc.painter.Painter;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitBlock;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class PlotMeIntegration implements Integration {
    private final Painter plugin;

    public PlotMeIntegration(final Painter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canEdit(final Block block, final Player player) {
        // This API is pretty silly
        PlotMeCoreManager manager = PlotMeCoreManager.getInstance();

        if (! manager.isPlotWorld(new BukkitBlock(block))) {
            // Not a plot world
            return true;
        }

        if (player.hasPermission("plotme.admin.buildanywhere")) {
            // Allowed to build anywhere
            return true;
        }

        ILocation location = new BukkitLocation(block.getLocation());
        PlotId plotId = manager.getPlotId(location);

        if (plotId == null) {
            // On the road
            return false;
        }

        Plot plot = manager.getPlotById(plotId, new BukkitWorld(block.getWorld()));

        // On the road also
        return plot != null && plot.isAllowed(player.getUniqueId());
    }

    @Override
    public String getPluginName() {
        return "PlotMe";
    }

    @Override
    public boolean setUp() {
        Plugin plotMePlugin = this.plugin.getServer().getPluginManager().getPlugin("PlotMe");

        return plotMePlugin != null && plotMePlugin.isEnabled();

    }

    @Override
    public void notifyNotAllowed(final Block block, final Player player) {
        // This API is pretty silly
        PlotMeCoreManager manager = PlotMeCoreManager.getInstance();

        ILocation location = new BukkitLocation(block.getLocation());
        PlotId plotId = manager.getPlotId(location);

        if (plotId == null) {
            // On the road
            return;
        }

        Plot plot = manager.getPlotById(plotId, new BukkitWorld(block.getWorld()));

        if (plot == null) {
            // On the road
            return;
        }

        Map<String, String> args = new HashMap<>();

        args.put("name", plot.getPlotName());
        args.put("owner", plot.getOwner());

        this.plugin.sendMessage(player, "plotme_not_allowed", args);
    }

    @Override
    public void blockReplaced(final Block block, final Player player) {
        // Honey badger don't care
    }
}
