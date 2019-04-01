package ggmmo.adminteleport;

import ggmmo.adminteleport.commands.CmdSetJoinSpawn;
import ggmmo.adminteleport.events.EventJoinTeleport;
import ggmmo.adminteleport.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class GGMMO_AdminTeleport extends JavaPlugin {
    private static GGMMO_AdminTeleport plugin;

    @Override
    public void onEnable() {
        // Create a singleton instance of the plugin
        plugin = this;

        MessageManager.consoleInfo("Plugin: AdminTeleport enabled");

        // Register plugin events
        getServer().getPluginManager().registerEvents(new EventJoinTeleport(), this);

        // Register plugin commands
        getCommand("setjoinspawn").setExecutor(new CmdSetJoinSpawn());

        loadConfig();
    }

    @Override
    public void onDisable() {
        getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Plugin: AdminTeleport disabled");
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public static GGMMO_AdminTeleport getPlugin() {
        return plugin;
    }
}