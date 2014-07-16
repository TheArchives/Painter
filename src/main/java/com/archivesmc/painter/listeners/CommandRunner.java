package com.archivesmc.painter.listeners;

import com.archivesmc.painter.Painter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandRunner implements CommandExecutor {
    private Painter plugin;

    public CommandRunner(Painter plugin) {
        this.plugin = plugin;
    }
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
                    if (this.plugin.permissions.has(commandSender, "painter.replace")) {
                        if (this.plugin.painters.contains(id)) {
                            this.plugin.painters.remove(id);

                            Map<String, String> args = new HashMap<>();
                            args.put("name", commandSender.getName());

                            this.plugin.sendMessage(commandSender, "replace_mode_disable", args);
                        } else {
                            this.plugin.painters.add(id);

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
                    if (this.plugin.permissions.has(commandSender, "painter.replace.range")) {
                        if (this.plugin.range_painters.contains(id)) {
                            this.plugin.range_painters.remove(id);

                            Map<String, String> args = new HashMap<>();
                            args.put("name", commandSender.getName());

                            this.plugin.sendMessage(commandSender, "range_replace_mode_disable", args);
                        } else {
                            this.plugin.range_painters.add(id);

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
