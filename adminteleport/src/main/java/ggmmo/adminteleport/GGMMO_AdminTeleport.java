package ggmmo.adminteleport;

import ggmmo.adminteleport.commands.CmdSetJoinSpawn;
import ggmmo.adminteleport.commands.CmdSetWarp;
import ggmmo.adminteleport.commands.CmdWarp;
import ggmmo.adminteleport.events.EventJoinTeleport;
import ggmmo.adminteleport.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
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
        getCommand("setwarp").setExecutor(new CmdSetWarp());
        getCommand("warp").setExecutor(new CmdWarp());

        loadConfig();

        if(getConfig().getString("joinspawn") == null){
            String w = getServer().getWorlds().stream().filter(world -> (!world.getName().contains("the_end")) && !world.getName().contains("nether")).findFirst().get().getName();
            SetSpawnConfig(w,0,0,0,0,0);
            MessageManager.consoleInfo("Default world set to: " + w);
        }
    }

    @Override
    public void onDisable() {
        getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Plugin: AdminTeleport disabled");
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void SetSpawnConfig(String world, int x, int y, int z, int yaw, int pitch){
        getConfig().set("joinspawn.world", world);
        getConfig().set("joinspawn.x", x);
        getConfig().set("joinspawn.y", y);
        getConfig().set("joinspawn.z", z);
        getConfig().set("joinspawn.yaw", yaw);
        getConfig().set("joinspawn.pitch", pitch);
        saveConfig();
    }

    public static GGMMO_AdminTeleport getPlugin() {
        return plugin;
    }
}