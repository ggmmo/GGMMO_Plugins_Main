package ggmmo.npceditor;

import ggmmo.npceditor.commands.CmdNpc;
import org.bukkit.plugin.java.JavaPlugin;

public class GGMMO_NpcEditor extends JavaPlugin {
    private static GGMMO_NpcEditor pluginInstance;

    public final NpcManager npcManager = new NpcManager();

    @Override
    public void onEnable() {
        pluginInstance = this;

        initCommands();
    }

    @Override
    public void onDisable() {

    }

    public static GGMMO_NpcEditor getInstance() {
        return pluginInstance;
    }

    /**
     * Setup the CommandExecutors for plugin commands
     */
    private void initCommands() {
        getCommand("npc").setExecutor(new CmdNpc());
    }
}
