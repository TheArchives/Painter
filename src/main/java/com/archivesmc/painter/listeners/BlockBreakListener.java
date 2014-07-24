package com.archivesmc.painter.listeners;

import com.archivesmc.painter.Painter;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gareth Coles
 *
 * This is an event listener for block breakages. It's used for painting over blocks
 * when a painter breaks them.
 */
public class BlockBreakListener implements Listener {

    private Painter plugin;

    public BlockBreakListener(Painter plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the blockBreakEvent for painting blocks in nearby-painting mode.
     * @param event The BlockBreakEvent to handle
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if(! event.isCancelled()) {
            Player player = event.getPlayer();

            if (this.plugin.isPainter(player.getUniqueId())) {
                if (! this.plugin.hasPermission(player, "painter.replace")) {
                    this.plugin.setPainter(player.getUniqueId(), false);

                    Map<String, String> args = new HashMap<>();
                    args.put("permission", "painter.replace");
                    args.put("name", player.getName());

                    this.plugin.sendMessage(player, "replace_perm_lost", args);
                    return;
                }

                ItemStack items = player.getItemInHand();
                Material heldMat = items.getType();

                if (heldMat.isBlock()) {
                    Block block = event.getBlock();
                    BlockState oldBlockState = block.getState();

                    block.setType(heldMat);
                    block.setData(items.getData().getData());

                    event.setCancelled(true);

                    // Log it if it's being logged
                    this.plugin.blockPainted(player, oldBlockState, block.getState(), block);
                }
            }
        }
    }
}
