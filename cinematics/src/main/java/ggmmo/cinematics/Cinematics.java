package ggmmo.cinematics;

import ggmmo.cinematics.commands.CmdLightningWand;
import ggmmo.cinematics.commands.CmdPlayTitle;
import ggmmo.cinematics.events.EventRespawn;
import ggmmo.cinematics.events.EventUseLightningWand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Cinematics extends JavaPlugin {
    private static Cinematics plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getCommand("lightningWand").setExecutor(new CmdLightningWand());
        getCommand("playtitle").setExecutor(new CmdPlayTitle());
        getServer().getPluginManager().registerEvents(new EventUseLightningWand(), this);
        getServer().getPluginManager().registerEvents(new EventRespawn(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Cinematics getPlugin() {
        return plugin;
    }
}
