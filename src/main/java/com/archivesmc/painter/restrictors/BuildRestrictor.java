package com.archivesmc.painter.restrictors;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Created by Gareth on 09/03/2015.
 */
public interface BuildRestrictor {
    public boolean canEdit(Block block, Player player);
    public String getPluginName();
    public boolean setUp();
    public void notifyNotAllowed(Block block, Player player);
}
