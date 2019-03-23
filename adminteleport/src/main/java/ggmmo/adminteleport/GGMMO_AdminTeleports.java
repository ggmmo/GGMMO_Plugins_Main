package ggmmo.adminteleport;

import ggmmo.adminteleport.events.EventJoinTeleport;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class GGMMO_AdminTeleports extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nPlugin: Init_Spawn Enabled\n\n");
        getServer().getPluginManager().registerEvents(new EventJoinTeleport(this), this);
        loadConfig();
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\nPlugin: Init_Spawn Disabled\n\n");
    }

    void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
