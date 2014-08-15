package com.archivesmc.painter.listeners;

import com.archivesmc.painter.Painter;
import com.archivesmc.painter.events.PaintEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gareth Coles
 *
 * This is an event listener for the PlayerInteractEvent. This event is finnicky and
 * is cancelled by default when the player is left-clicking air, but we need it for
 * ranged-painting mode.
 */
public class PlayerInteractListener implements Listener {
    private final Painter plugin;

    public PlayerInteractListener(Painter plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the PlayerInteractEvent, which is used for ranged-painting mode.
     *
     * @param event The PlayerInteractEvent to handle
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (this.plugin.isRangePainter(player.getUniqueId()) &&
                event.getAction() == Action.LEFT_CLICK_AIR
        ) {
            if (! this.plugin.hasPermission(player, "painter.replace.range")) {
                this.plugin.setRangePainter(player.getUniqueId(), false);

                Map<String, String> args = new HashMap<>();
                args.put("permission", "painter.replace.range");
                args.put("name", player.getName());

                this.plugin.sendMessage(player, "range_replace_perm_lost", args);
                return;
            }

            ItemStack items = player.getItemInHand();
            Material heldMat = items.getType();

            if (heldMat.isBlock()) {
                Block block = player.getTargetBlock(null, 100);

                if (block == null) {
                    return;
                }

                event.setCancelled(true);

                PaintEvent paintEvent = new PaintEvent(event.getPlayer(), items, block);
                this.plugin.getServer().getPluginManager().callEvent(paintEvent);
            }
        }
    }
}
