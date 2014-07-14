package com.archivesmc.painter;

import com.archivesmc.painter.listeners.blockBreakListener;
import com.archivesmc.painter.listeners.commandExecutor;
import com.archivesmc.painter.loggers.Logger;
import com.archivesmc.painter.loggers.coreprotectLogger;
import com.archivesmc.painter.loggers.logblockLogger;
import com.archivesmc.painter.loggers.prismLogger;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Painter extends JavaPlugin {

    Material tool;
    String tool_string;

    Logger logger;
    String logger_string;

    boolean use_logger = false;
    boolean use_tool = false;

    public HashSet<UUID> painters;

    private blockBreakListener breakListener;
    private commandExecutor commands;

    public Permission permissions;

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist, and reload it in case the plugin's been reloaded
        this.saveDefaultConfig();
        this.reloadConfig();

        // Assign painters set here in case we're reloading, instead of in the class definition
        this.painters = new HashSet<>();

        // First, let's start with the paint tool material
        this.tool_string = this.getConfig().getString("paint_tool", null);

        if (this.tool_string != null) { // Returns null if the entry doesn't exist
            this.tool = Material.matchMaterial(this.tool_string);

            if (this.tool == null) {
                // Couldn't find the specified material
                this.getLogger().warning(String.format(
                        "Unknown paint tool item: %s",
                        this.tool_string
                ));
            } else if (this.tool.isBlock()) {
                // Paint tool has to be an item
                this.getLogger().warning(String.format(
                        "Paint tool item %s is a block - blocks can't be used!",
                        this.tool_string
                ));
            } else {
                // We're all good, we'll set up the tool later
                this.use_tool = true;
            }
        } else {
            // Might've been intentional, but log it anyway
            this.getLogger().info("No paint tool material found, paint tool will not be available.");
        }

        // Now, let's look at the logger
        this.logger_string = this.getConfig().getString("logger", null);

        if (this.logger_string != null) { // Returns null if the entry doesn't exist
            this.logger_string = this.logger_string.toLowerCase();
            switch (this.logger_string) {
                // We do manual plugin checking here to avoid problems that go with importing classes
                case "logblock":
                    if (this.getServer().getPluginManager().getPlugin("LogBlock") == null) {
                        this.getLogger().warning("Unable to find logging plugin 'LogBlock'. Block logging will not be available.");
                        break;
                    }
                    this.logger = new logblockLogger(this);
                    this.use_logger = true;
                    break;
                case "coreprotect":
                    if (this.getServer().getPluginManager().getPlugin("CoreProtect") == null) {
                        this.getLogger().warning("Unable to find logging plugin 'CoreProtect'. Block logging will not be available.");
                        break;
                    }
                    this.logger = new coreprotectLogger(this);
                    this.use_logger = true;
                    break;
                case "prism":
                    if (this.getServer().getPluginManager().getPlugin("Prism") == null) {
                        this.getLogger().warning("Unable to find logging plugin 'Prism'. Block logging will not be available.");
                        break;
                    }
                    this.logger = new prismLogger(this);
                    this.use_logger = true;
                    break;
                default:
                    this.getLogger().warning(String.format("Unknown block logging plugin: '%s'", this.logger_string));
                    break;
            }

            if (this.use_logger) {
                if (! this.logger.setup()) {
                    this.use_logger = false;
                    this.getLogger().warning(String.format(
                            "Unable to set up logging handler for plugin '%s'. Is it enabled?",
                            this.logger.getPluginName()
                    ));
                } else {
                    this.getLogger().info("Block logging set up successfully.");
                }
            }
        } else {
            // Might've been intentional, but log it anyway
            this.getLogger().info("No block logging is configured, so we won't be logging paints.");
        }

        // Next, let's set up permissions
        if (! this.setupPermissions()) {
            this.getLogger().warning("Unable to load permissions. Please make sure Vault is installed!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Now that that's done, let's register events and commands
        this.breakListener = new blockBreakListener(this);
        this.commands = new commandExecutor(this);

        this.getServer().getPluginManager().registerEvents(breakListener, this);
        getCommand("painter").setExecutor(this.commands);
    }

    public void blockPainted(Player player, BlockState oldBlockState, BlockState newBlockState, Block block) {
        if (this.use_logger) {
            this.logger.blockPainted(player, oldBlockState, newBlockState, block);
        }
    }

    public void sendMessage(CommandSender player, String message, HashMap<String, String> args) {
        String msg = this.getConfig().getString("messages.".concat(message));

        if (msg == null) {
            player.sendMessage(String.format("Unknown or missing message: %s", message));
            player.sendMessage("Please notify the server owner so that they may fix this.");
            player.sendMessage("If you just ran a command, this message doesn't necessarily mean it didn't complete!");

            this.getLogger().warning(String.format("Unknown or missing message: %s", message));
            this.getLogger().warning(String.format("Please check your configuration and make sure messages.%s exists!", message));
            this.getLogger().warning("If you're sure you configured Painter properly, then please report this to the BukkitDev page.");
            return;
        }

        if (args != null) { // Not all messages have tokens
            for (String key : args.keySet()) {
                String origKey = key;
                key = key.toUpperCase();
                key = String.format("{%s}", key);

                msg = msg.replace(key, args.get(origKey));
            }
        }
        msg = translateAlternateColorCodes('&', msg);
        player.sendMessage(msg);
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permissions = permissionProvider.getProvider();
        }
        return (permissions != null);
    }
}
