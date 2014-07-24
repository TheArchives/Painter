package com.archivesmc.painter;

import com.archivesmc.painter.listeners.BlockBreakListener;
import com.archivesmc.painter.listeners.CommandRunner;
import com.archivesmc.painter.listeners.PlayerInteractListener;
import com.archivesmc.painter.loggers.*;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

/**
 * @author Gareth Coles
 */
public class Painter extends JavaPlugin {

    private BlockLogger blockLogger;
    private String loggerString;

    private boolean useLogger = false;

    private Set<UUID> painters;
    private Set<UUID> rangePainters;
    private Permission permissions;

    /**
     * Called by Bukkit when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        // Save default config if it doesn't exist, and reload it in case the plugin's been reloaded
        this.saveDefaultConfig();
        this.reloadConfig();

        // Assign painters set here in case we're reloading, instead of in the class definition
        this.painters = new HashSet<>();
        this.rangePainters = new HashSet<>();

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
        BlockBreakListener breakListener = new BlockBreakListener(this);
        PlayerInteractListener interactListener = new PlayerInteractListener(this);
        CommandRunner commands = new CommandRunner(this);

        this.getServer().getPluginManager().registerEvents(breakListener, this);
        this.getServer().getPluginManager().registerEvents(interactListener, this);
        getCommand("painter").setExecutor(commands);
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

    /**
     * Log that a block has been painted, using the configured block logger.
     * Does nothing if there isn't one configured.
     *
     * @param player The Player that painted the block
     * @param oldBlockState The BlockState of the block before it was painted
     * @param newBlockState The BlockState of the block after it was painted
     * @param block The Block itself, after it's been painted
     */
    public void blockPainted(Player player, BlockState oldBlockState, BlockState newBlockState, Block block) {
        if (this.useLogger) {
            this.blockLogger.blockPainted(player, oldBlockState, newBlockState, block);
        }
    }

    /**
     * Send a message to a player, where the message has been defined in the
     * plugin's <code>config.yml</code>.
     *
     * <p>
     *
     * The <code>args</code> map is used to replace tokens in the message. For example..
     *
     * <p>
     *
     * <code>
     *     Map<String, String> args = new HashMap<>();<br/>
     *     args.put("PERMISSION", "my.awesome.permission");
     * </code>
     *
     * <p>
     *
     * This will replace <code>"{PERMISSION}"</code> in the message with
     * <code>"my.awesome.permission"</code>.
     *
     * @param player The CommandSender (Console or Player) to send the message to.
     * @param message The name of the message, as configured in <code>config.yml</code>.
     * @param args A Map of arguments that will be replacing tokens in the message itself.
     */
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

    /**
     * Returns whether the player is in nearby-painting mode
     * @param player The UUID of the player to check
     * @return Whether the player is in nearby-painting mode
     */
    public boolean isPainter(UUID player) {
        return this.painters.contains(player);
    }

    /**
     * Set whether a player is in nearby-painting mode or not
     * @param player The UUID of the player to check
     * @param set Whether the player should be set to nearby-painting mode or not
     */
    public void setPainter(UUID player, boolean set) {
        if (set) {
            if (! this.painters.contains(player)) {
                this.painters.add(player);
            }
        } else {
            if (this.painters.contains(player)) {
                this.painters.remove(player);
            }
        }
    }

    /**
     * Returns whether the player is in range-painting mode
     * @param player The UUID of the player to check
     * @return Whether the player is in range-painting mode
     */
    public boolean isRangePainter(UUID player) {
        return this.rangePainters.contains(player);
    }

    /**
     * Set whether a player is in range-painting mode or not
     * @param player The UUID of the player to check
     * @param set Whether the player should be set to range-painting mode or not
     */
    public void setRangePainter(UUID player, boolean set) {
        if (set) {
            if (! this.rangePainters.contains(player)) {
                this.rangePainters.add(player);
            }
        } else {
            if (this.rangePainters.contains(player)) {
                this.rangePainters.remove(player);
            }
        }
    }

    /**
     * Uses vault to check whether a CommandSender has a certain permission
     * @param sender The Player or other CommandSender to check permissions for
     * @param permission The permissions to check for
     * @return Whether the CommandSender has the specified permission
     */
    public boolean hasPermission(CommandSender sender, String permission) {
        return this.permissions.has(sender, permission);
    }
}
