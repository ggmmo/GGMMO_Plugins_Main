package ggmmo.npcfactory;

import ggmmo.npcfactory.commands.CmdNpcWand;
import org.bukkit.plugin.java.JavaPlugin;

public final class NpcFactory extends JavaPlugin {
    private static NpcFactory plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getCommand("npcwand").setExecutor(new CmdNpcWand());
    }

    @Override
    public void onDisable() {

    }

    public static NpcFactory getPlugin() {
        return plugin;
    }
}
