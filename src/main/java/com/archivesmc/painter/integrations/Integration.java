package com.archivesmc.painter.integrations;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface Integration {
    public boolean canEdit(final Block block, final Player player);
    public String getPluginName();
    public boolean setUp();
    public void notifyNotAllowed(final Block block, final Player player);
    public void blockReplaced(final Block block, final Player player);
}
