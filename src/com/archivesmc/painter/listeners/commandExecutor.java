package com.archivesmc.painter.listeners;

import com.archivesmc.painter.Painter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class commandExecutor implements CommandExecutor {
    private Painter plugin;

    public commandExecutor(Painter plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getLabel().equalsIgnoreCase("painter")) {
            if (!(commandSender instanceof Player)) {
                this.plugin.sendMessage(commandSender, "command_player_only", null);
            } else {
                UUID id = ((Player) commandSender).getUniqueId();
                if (strings.length < 1) {
                    if (this.plugin.permissions.has(commandSender, "painter.replace")) {
                        if (this.plugin.painters.contains(id)) {
                            this.plugin.painters.remove(id);
                            this.plugin.sendMessage(commandSender, "replace_mode_disable", null);
                        } else {
                            this.plugin.painters.add(id);
                            this.plugin.sendMessage(commandSender, "replace_mode_enable", null);
                        }
                    } else {
                        this.plugin.sendMessage(commandSender, "command_replace_no_permission", null);
                    }
                } else {
                    // TODO: Paint tool
                    commandSender.sendMessage("Paint tool isn't implemented yet!");
                }
            }

            return true;
        }
        return false;
    }
}
