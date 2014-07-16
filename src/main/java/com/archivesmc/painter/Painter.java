package com.archivesmc.painter;

import com.archivesmc.painter.listeners.BlockBreakListener;
import com.archivesmc.painter.listeners.CommandRunner;
import com.archivesmc.painter.loggers.*;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Painter extends JavaPlugin {

    Material tool;
    String toolString;

    BlockLogger blockLogger;
    String loggerString;

    boolean useLogger = false;
    boolean useTool = false;

    public Set<UUID> painters;

    private BlockBreakListener breakListener;
    private CommandRunner commands;

    public Permission permissions;

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist, and reload it in case the plugin's been reloaded
        this.saveDefaultConfig();
        this.reloadConfig();

        // Assign painters set here in case we're reloading, instead of in the class definition
        this.painters = new HashSet<>();

        // First, let's start with the paint tool material
        this.toolString = this.getConfig().getString("paint_tool", null);

        if (this.toolString != null) {
            // Returns null if the entry doesn't exist
            this.tool = Material.matchMaterial(this.toolString);

            if (this.tool == null) {
                // Couldn't find the specified material
                this.getLogger().warning(String.format(
                        "Unknown paint tool item: %s",
                        this.toolString
                ));
            } else if (this.tool.isBlock()) {
                // Paint tool has to be an item
                this.getLogger().warning(String.format(
                        "Paint tool item %s is a block - blocks can't be used!",
                        this.toolString
                ));
            } else {
                // We're all good, we'll set up the tool later
                this.useTool = true;
            }
        } else {
            // Might've been intentional, but log it anyway
            this.getLogger().info("No paint tool material found, paint tool will not be available.");
        }

        // Now, let's look at the logger
        this.loggerString = this.getConfig().getString("logger", null);

        if (this.loggerString != null) {
            // Returns null if the entry doesn't exist
            this.loggerString = this.loggerString.toLowerCase();
            switch (this.loggerString) {
                // We do manual plugin checking here to avoid problems that go with importing classes
                case "logblock":
                    this.setupLogBlock();
                    break;
                case "coreprotect":
                    this.setupCoreProtect();
                    break;
                case "prism":
                    this.setupPrism();
                    break;
                case "hawkeye":
                    this.setupHawkeye();
                    break;
                default:
                    this.getLogger().warning(String.format("Unknown block logging plugin: '%s'", this.loggerString));
                    break;
            }

            if (this.useLogger) {
                if (! this.blockLogger.setup()) {
                    this.useLogger = false;
                    this.getLogger().warning(String.format(
                            "Unable to set up logging handler for plugin '%s'. Is it enabled?",
                            this.blockLogger.getPluginName()
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
        this.breakListener = new BlockBreakListener(this);
        this.commands = new CommandRunner(this);

        this.getServer().getPluginManager().registerEvents(breakListener, this);
        getCommand("painter").setExecutor(this.commands);
    }

    private void setupLogBlock() {
        if (this.getServer().getPluginManager().getPlugin("LogBlock") == null) {
            this.getLogger().warning("Unable to find logging plugin 'LogBlock'. Block logging will not be available.");
            return;
        }

        this.blockLogger = new LogblockLogger(this);
        this.useLogger = true;
    }

    private void setupCoreProtect() {
        if (this.getServer().getPluginManager().getPlugin("CoreProtect") == null) {
            this.getLogger().warning("Unable to find logging plugin 'CoreProtect'. Block logging will not be available.");
            return;
        }

        this.blockLogger = new CoreprotectLogger(this);
        this.useLogger = true;
    }

    private void setupPrism() {
        if (this.getServer().getPluginManager().getPlugin("Prism") == null) {
            this.getLogger().warning("Unable to find logging plugin 'Prism'. Block logging will not be available.");
            return;
        }

        this.blockLogger = new PrismLogger(this);
        this.useLogger = true;
    }

    private void setupHawkeye() {
        if (this.getServer().getPluginManager().getPlugin("HawkEye") == null) {
            this.getLogger().warning("Unable to find logging plugin 'HawkEye'. Block logging will not be available.");
            return;
        }

        this.blockLogger = new HawkEyeLogger(this);
        this.useLogger = true;
    }

    public void blockPainted(Player player, BlockState oldBlockState, BlockState newBlockState, Block block) {
        if (this.useLogger) {
            this.blockLogger.blockPainted(player, oldBlockState, newBlockState, block);
        }
    }

    public void sendMessage(CommandSender player, String message, Map<String, String> args) {
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

        if (args != null) {
            // Not all messages have tokens
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
        return permissions != null;
    }
}
