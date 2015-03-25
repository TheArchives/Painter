package com.archivesmc.painter.integrations;

import com.archivesmc.painter.Painter;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class PlotMeIntegration implements Integration {
    private final Painter plugin;

    public PlotMeIntegration(Painter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canEdit(Block block, Player player) {
        // This API is pretty silly
        PlotMeCoreManager manager = PlotMeCoreManager.getInstance();
        ILocation location = new BukkitLocation(block.getLocation());
        Plot plot = manager.getPlotById(manager.getPlotId(location), new BukkitWorld(block.getWorld()));

        return plot.isAllowed(player.getUniqueId());
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
    public void notifyNotAllowed(Block block, Player player) {
        // This API is pretty silly
        PlotMeCoreManager manager = PlotMeCoreManager.getInstance();
        ILocation location = new BukkitLocation(block.getLocation());
        Plot plot = manager.getPlotById(manager.getPlotId(location), new BukkitWorld(block.getWorld()));

        Map<String, String> args = new HashMap<>();

        args.put("name", plot.getPlotName());
        args.put("owner", plot.getOwner());

        this.plugin.sendMessage(player, "plotme_not_allowed", args);
    }

    @Override
    public void blockReplaced(Block block, Player player) {
        // Honey badger don't care
    }
}
