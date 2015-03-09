package com.archivesmc.painter.integrations;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface Integration {
    public boolean canEdit(Block block, Player player);
    public String getPluginName();
    public boolean setUp();
    public void notifyNotAllowed(Block block, Player player);
    public void blockReplaced(Block block, Player player);
}
