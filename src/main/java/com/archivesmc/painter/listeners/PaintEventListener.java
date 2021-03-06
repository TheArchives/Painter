package com.archivesmc.painter.listeners;

import com.archivesmc.painter.Painter;
import com.archivesmc.painter.events.PaintEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * @author Gareth Coles
 *
 * Listens for a PaintEvent and paints a block if it isn't cancelled.
 */
public class PaintEventListener implements Listener {

    private final Painter plugin;

    public PaintEventListener(final Painter plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void paintEvent(final PaintEvent event) {
        Player player = event.getPlayer();
        ItemStack items = event.getItem();
        Material heldMat = items.getType();

        if (heldMat.isBlock()) {
            Block block = event.getBlock();
            BlockState oldBlockState = block.getState();

            block.setType(heldMat);

            BlockState newBlockState = block.getState();
            newBlockState.setData(items.getData());
            newBlockState.update();

            // Log it if it's being logged
            this.plugin.blockPainted(player, oldBlockState, newBlockState, block);
        }
    }
}
