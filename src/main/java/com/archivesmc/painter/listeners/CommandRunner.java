package com.archivesmc.painter.listeners;

import com.archivesmc.painter.Painter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Gareth Coles
 *
 * This is the command runner for this plugin. We define all our
 * commands here.
 */
public class CommandRunner implements CommandExecutor {
    private final Painter plugin;

    public CommandRunner(Painter plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles a command.
     * @param commandSender Player or Console that ran the command
     * @param command String as defined in the plugin.yml naming the command
     * @param s String representing the entire command
     * @param strings Array of strings representing the arguments to the command
     * @return Whether this plugin has implemented the command or not
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if ("painter".equalsIgnoreCase(command.getLabel())) {
            if (!(commandSender instanceof Player)) {
                Map<String, String> args = new HashMap<>();
                args.put("name", commandSender.getName());

                this.plugin.sendMessage(commandSender, "command_player_only", args);
            } else {
                UUID id = ((Player) commandSender).getUniqueId();
                if (strings.length < 1 || "toggle".equalsIgnoreCase(strings[0])) {
                    if (this.plugin.hasPermission(commandSender, "painter.replace")) {
                        if (this.plugin.isPainter(id)) {
                            this.plugin.setPainter(id, false);

                            Map<String, String> args = new HashMap<>();
                            args.put("name", commandSender.getName());

                            this.plugin.sendMessage(commandSender, "replace_mode_disable", args);
                        } else {
                            this.plugin.setPainter(id, true);

                            Map<String, String> args = new HashMap<>();
                            args.put("name", commandSender.getName());

                            this.plugin.sendMessage(commandSender, "replace_mode_enable", args);
                        }
                    } else {
                        Map<String, String> args = new HashMap<>();
                        args.put("permission", "painter.replace");
                        args.put("name", commandSender.getName());

                        this.plugin.sendMessage(commandSender, "command_replace_no_permission", args);
                    }
                } else if ("range".equalsIgnoreCase(strings[0])) {
                    if (this.plugin.hasPermission(commandSender, "painter.replace.range")) {
                        if (this.plugin.isRangePainter(id)) {
                            this.plugin.setRangePainter(id, false);

                            Map<String, String> args = new HashMap<>();
                            args.put("name", commandSender.getName());

                            this.plugin.sendMessage(commandSender, "range_replace_mode_disable", args);
                        } else {
                            this.plugin.setRangePainter(id, true);

                            Map<String, String> args = new HashMap<>();
                            args.put("name", commandSender.getName());

                            this.plugin.sendMessage(commandSender, "range_replace_mode_enable", args);
                        }
                    } else {
                        Map<String, String> args = new HashMap<>();
                        args.put("permission", "painter.replace.range");
                        args.put("name", commandSender.getName());

                        this.plugin.sendMessage(commandSender, "command_range_replace_no_permission", args);
                    }
                }
            }

            return true;
        }
        return false;
    }
}
