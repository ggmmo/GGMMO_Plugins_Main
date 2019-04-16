package ggmmo.serverutils;

import ggmmo.serverutils.commands.CmdGetUuid;
import ggmmo.serverutils.commands.CmdPluginDisable;
import ggmmo.serverutils.commands.CmdPluginEnable;
import ggmmo.serverutils.utils.MessageManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class GGMMO_ServerUtils extends JavaPlugin {
    private static GGMMO_ServerUtils plugin;

    private ArrayList<String> authorisedUsers = new ArrayList<>();

    @Override
    public void onEnable() {
        plugin = this;

        authorisedUsers.add("ff980a11-7feb-4aa6-a49e-111f8e704738"); // Intrepid249

        cmdInit();
    }

    @Override
    public void onDisable() {

    }

    private void cmdInit() {
        getCommand("ggdisable").setExecutor(new CmdPluginDisable());
        getCommand("ggenable").setExecutor(new CmdPluginEnable());
        getCommand("getuuid").setExecutor(new CmdGetUuid());
    }

    public static GGMMO_ServerUtils getInstance() { return plugin; }

    public boolean isAuthorisedUser(UUID uuid) {
        if (plugin.authorisedUsers.contains(uuid.toString()))
            return true;
        return false;
    }
}
