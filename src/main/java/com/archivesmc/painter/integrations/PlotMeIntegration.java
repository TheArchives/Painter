package com.archivesmc.painter.integrations;

import com.archivesmc.painter.Painter;
import com.google.common.base.Optional;
import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.Location;
import com.worldcretornica.plotme_core.api.Vector;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
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
        PlotMe_CorePlugin plugin = (PlotMe_CorePlugin) this.plugin.getServer().getPluginManager().getPlugin("PlotMe");

        if (! manager.isPlotWorld(new BukkitWorld(block.getWorld()))) {
            // Not a plot world
            return true;
        }

        if (player.hasPermission(PermissionNames.ADMIN_BUILDANYWHERE)) {
            // They can build anywhere
            return true;
        }

        if (player.hasPermission("plotme.admin.buildanywhere")) {
            // Allowed to build anywhere
            return true;
        }

        Location location = new Location(new BukkitWorld(block.getWorld()), new Vector(block.getX(), block.getY(), block.getZ()));
        PlotId plotId = manager.getPlotId(location);

        if (plotId == null) {
            // On the road
            return false;
        }

        Plot plot = manager.getPlotById(plotId, new BukkitWorld(block.getWorld()));

        if (plot == null) {
            // On the road also
            return false;
        }

        // Next we need to emulate the checks PlotMe makes since the plotme api is heavily broken

        if (plot.getOwnerId().equals(player.getUniqueId())) {
            // They own the plot
            return true;
        }

        Optional<Plot.AccessLevel> member = plot.isMember(player.getUniqueId());

        if (member.isPresent()) {
            // They're a member of the plot
            if (member.get().equals(Plot.AccessLevel.TRUSTED) && !this.plugin.getServer().getOfflinePlayer(plot.getOwnerId()).isOnline()) {
                // If they're a trusted member and the owner is offline.. Nope.
                return false;
            } else if (plugin.getAPI().isPlotLocked(plot.getId())) {
                // Plot is locked.. Nope.
                return false;
            }

            return true;
        }

        return false;
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

        Location location = new Location(new BukkitWorld(block.getWorld()), new Vector(block.getX(), block.getY(), block.getZ()));
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
