package com.archivesmc.painter.listeners;

import com.archivesmc.painter.Painter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerInteractListener implements Listener {
    Painter plugin;

    public PlayerInteractListener(Painter plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
//        if(! event.isCancelled()) {
            Player player = event.getPlayer();

            if (this.plugin.range_painters.contains(player.getUniqueId()) &&
                    event.getAction() == Action.LEFT_CLICK_AIR
            ) {
                this.plugin.getLogger().info("Event!");
                if (! this.plugin.permissions.has(player, "painter.replace.range")) {
                    this.plugin.range_painters.remove(player.getUniqueId());

                    Map<String, String> args = new HashMap<>();
                    args.put("permission", "painter.replace.range");
                    args.put("name", player.getName());

                    this.plugin.sendMessage(player, "range_replace_perm_lost", args);
                    return;
                }

                ItemStack items = player.getItemInHand();
                Material heldMat = items.getType();

                if (heldMat.isBlock()) {
                    this.plugin.getLogger().info("It's a block!");
                    Block block = player.getTargetBlock(null, 100);
                    BlockState oldBlockState = block.getState();

                    block.setType(heldMat);
                    block.setData(items.getData().getData());

                    event.setCancelled(true);

                    // Log it if it's being logged
                    this.plugin.blockPainted(player, oldBlockState, block.getState(), block);
                }
            }
//        }
    }
}
