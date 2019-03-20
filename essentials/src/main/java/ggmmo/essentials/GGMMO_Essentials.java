package ggmmo.essentials;

import ggmmo.essentials.ggmmo.essentials.events.EventClass;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class GGMMO_Essentials extends JavaPlugin implements Listener {
    public HashMap<UUID, PermissionAttachment> playerPermissions = new HashMap<UUID, PermissionAttachment>();
    public HashMap<UUID, String> playerPermissionGroups = new HashMap<UUID, String>();

    public static final String NAME_BLOCK = "NameBlock";
    public static final String SET_PERMISSION_GROUP = "SetPermissionGroup";

    @Override
    public void onEnable() {
        getLogger().info("Loaded EventHandler");
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        getServer().getConsoleSender().sendMessage(ChatColor.RED + "classpath: " + System.getProperty("java.classpath"));

        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new EventClass(), this);
    }

    @Override
    public void onDisable() {
        playerPermissions.clear();

        // Save permissions if server closes
        for (Player player : getServer().getOnlinePlayers()) {
            String permissionGroup = playerPermissionGroups.get(player.getUniqueId());
            getConfig().set("Users." + player.getUniqueId() + ".Group", permissionGroup);
            saveConfig();
        }
    }

    @SuppressWarnings("deprecation")
    public static void sendAuthorDebugMsg(Server server, String message) {
        Player author = server.getPlayer("Intrepid249");

        author.sendMessage(ChatColor.RED + "Debug Msg:");
        author.sendMessage(message);

        server.getConsoleSender().sendMessage(ChatColor.RED + "Debug Msg:");
        server.getConsoleSender().sendMessage(message);
    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent event) {
        // Setup and save permissions upon joining
        Player player = event.getPlayer();
        setupPermissions(player);
        String permissionGroup = playerPermissionGroups.get(player.getUniqueId());
        getConfig().set("Users." + player.getUniqueId() + ".Group", permissionGroup);
        saveConfig();
    }

    @EventHandler
    public void leaveEvent(PlayerQuitEvent event) {
        // Save permissions upon leaving
        Player player = event.getPlayer();
        String permissionGroup = playerPermissionGroups.get(player.getUniqueId());
        getConfig().set("Users." + player.getUniqueId() + ".Group", permissionGroup);
        saveConfig();
    }

    public void setupPermissions(Player player) {
        FileConfiguration config = this.getConfig();
        Set<String> uuidList = (config.getConfigurationSection("Users") != null) ? config.getConfigurationSection("Users").getKeys(false) : null;
        UUID uuid = null;
        if (uuidList != null)
            for (String playerUUID : uuidList) {
                sendAuthorDebugMsg(getServer(), ChatColor.AQUA + "UUID Loaded: " + playerUUID);
                sendAuthorDebugMsg(getServer(), ChatColor.DARK_BLUE + "Player UUID: " + player.getUniqueId());
                if (UUID.fromString(playerUUID).compareTo(player.getUniqueId()) == 0) {
                    uuid = UUID.fromString(playerUUID);
                    playerPermissionGroups.put(uuid, config.getString("Users." + uuid + ".Group"));
                    break;
                }
            }


        if (uuidList != null && uuid != null) {
            // If the player is already listed in the permission groups give them existing permissions
            sendAuthorDebugMsg(getServer(), "Loading existing user group data");
            sendAuthorDebugMsg(getServer(), "  Player: " + ChatColor.YELLOW + getServer().getPlayer(uuid).getDisplayName());
            sendAuthorDebugMsg(getServer(), "  Group: " + ChatColor.YELLOW + config.getString("Users." + uuid + ".Group"));

            setPlayerPrefixByPermission(player, playerPermissionGroups.get(player.getUniqueId()));
            permissionsSetter(player.getUniqueId(), playerPermissionGroups.get(player.getUniqueId()));
        } else {
            // Check each group for a `default: true` tag
            for (String group : config.getConfigurationSection("groups").getKeys(false)) {
                if (config.getBoolean("groups." + group + ".default")) {
                    sendAuthorDebugMsg(getServer(), " Player: " + ChatColor.YELLOW + player.getDisplayName());
                    sendAuthorDebugMsg(getServer(), " Group: " + ChatColor.YELLOW + group);
                    setPlayerPrefixByPermission(player, group);
                    permissionsSetter(player.getUniqueId(), group);
                }
            }
        }
    }

    private void setPlayerPrefixByPermission(Player player, String group) {
        FileConfiguration config = this.getConfig();
        String prefix = config.getString("groups." + group + ".info.prefix");
        prefix = (prefix != null) ? prefix : "";

        player.setDisplayName(prefix + player.getName());
    }

    private void permissionsSetter(UUID uuid, String group) {
        Player player = getServer().getPlayer(uuid);
        PermissionAttachment attachment = player.addAttachment(this);

        playerPermissions.put(player.getUniqueId(), attachment);
        FileConfiguration config = this.getConfig();

        // Store the user permission groups in the config
        if (!playerPermissionGroups.containsKey(uuid))
            playerPermissionGroups.put(uuid, group);
        else {
            // Update the stored data
            playerPermissionGroups.remove(uuid);
            playerPermissionGroups.put(uuid, group);
        }

        // set the permissions for the player's permission attachment according to their permission group
        for (String permission : config.getStringList("groups." + group + ".permissions")) {
            attachment.setPermission(permission, true);
        }

        setPlayerPrefixByPermission(player, group);
    }

    // COMMANDS
    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // SetPermissionGroup <Player> [GroupName = Member]
        if (command.getName().equalsIgnoreCase(SET_PERMISSION_GROUP) && sender instanceof Player) {
            Player targetPlayer;
            if (getServer().getPlayer(args[0]) != null) {
                targetPlayer = getServer().getPlayer(args[0]);
                if (args.length == 2 && args[1] != null) {
                    permissionsSetter(targetPlayer.getUniqueId(), args[1]);
                } else {
                    permissionsSetter(targetPlayer.getUniqueId(), "Player");
                }
                sender.sendMessage("Set permission group for " + ChatColor.YELLOW + args[0] + ChatColor.WHITE + " to " + ChatColor.YELLOW + args[1]);
                sender.sendMessage("Permissions enabled:");
                for (String permission : getConfig().getStringList("groups." + args[1] + ".permissions"))
                    sender.sendMessage(ChatColor.DARK_RED + permission);

                return true;
            } else {
                sender.sendMessage("Invalid argument.");
                sender.sendMessage(command.getUsage());
            }
        }

        if (command.getName().equalsIgnoreCase(NAME_BLOCK) && sender instanceof Player) {
            sender.sendMessage("Rename block");
        }

        return false;
    }
}
