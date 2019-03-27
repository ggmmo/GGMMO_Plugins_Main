package ggmmo.economy;

import ggmmo.economy.commands.CmdCreatePlayerAccount;
import ggmmo.economy.utils.MessageManager;
import ggmmo.economy.utils.MongoConnect;
import ggmmo.economy.utils.PlayerManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import sun.plugin2.message.Message;

import java.sql.Connection;
import java.util.HashMap;
import java.util.UUID;

public class GGMMO_Economy extends JavaPlugin {
    private static GGMMO_Economy plugin;
    public MongoConnect mongoConnect;
    public EconomyCore economyCore;


    public HashMap<UUID, PlayerManager> playerManagerHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        instanceClasses();

        // Setup commands
        plugin.getCommand("createaccount").setExecutor(new CmdCreatePlayerAccount());

        mongoConnect.connect();

        if (!setupEconomy()) {
            MessageManager.consoleBad("Economy could not be registered...Vault is missing!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        MessageManager.consoleGood("Economy successfully loaded...");
    }

    @Override
    public void onDisable() {

    }

    private void instanceClasses() {
        mongoConnect = new MongoConnect();
        economyCore = new EconomyCore();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        getServer().getServicesManager().register(Economy.class, economyCore, this, ServicePriority.Highest);
        MessageManager.consoleGood("Economy has been registered...");
        return true;
    }

    public static GGMMO_Economy getPlugin() { return plugin; }
}
