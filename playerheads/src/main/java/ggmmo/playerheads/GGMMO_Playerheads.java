package ggmmo.playerheads;

import ggmmo.playerheads.commands.CmdHeads;
import ggmmo.playerheads.events.EventClass;
import org.bukkit.plugin.java.JavaPlugin;

public class GGMMO_Playerheads extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage("[GGMMO] Playerheads enabled");
        getServer().getPluginManager().registerEvents(new EventClass(this), this);
        this.getCommand("heads").setExecutor(new CmdHeads(this));
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage("[GGMMO] Playerheads disabled");
    }
}
