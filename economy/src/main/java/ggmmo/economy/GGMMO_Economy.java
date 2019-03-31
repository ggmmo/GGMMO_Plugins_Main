package ggmmo.economy;

import ggmmo.economy.commands.*;
import ggmmo.economy.events.EventJoin;
import ggmmo.economy.utils.MessageManager;
import ggmmo.economy.utils.MongoConnect;
import ggmmo.economy.utils.PlayerManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class GGMMO_Economy extends JavaPlugin {
    private static GGMMO_Economy plugin;
    public MongoConnect mongoConnect;
    public EconomyCore economyCore;


    public HashMap<UUID, PlayerManager> playerManagerHashMap = new HashMap<>();

    // TODO: Use a scoreboard or some kind of HUD thing to display balance
    // TODO: Use ActionBar to show the messages when users get paid
    // TODO: Add trading system -> /trade <player>

    @Override
    public void onEnable() {
        plugin = this;
        instanceClasses();

        // Setup EventHandlers
        plugin.getServer().getPluginManager().registerEvents(new EventJoin(), this);

        // Setup commands
        plugin.getCommand("createaccount").setExecutor(new CmdCreatePlayerAccount());
        plugin.getCommand("balance").setExecutor(new CmdBalance());
        plugin.getCommand("pay").setExecutor(new CmdPay());
        plugin.getCommand("addBal").setExecutor(new CmdAddBal());
        plugin.getCommand("subBal").setExecutor(new CmdSubBal());

        mongoConnect.connect();

        if (!setupEconomy()) {
            MessageManager.consoleBad("Economy could not be registered...Vault is missing!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Reload existing database information for any players connected in case of reload command issued
        for (Player player : getServer().getOnlinePlayers()) {
            MessageManager.consoleInfo("Checking user: " + player.getDisplayName());
            mongoConnect.addNewPlayer(player);
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
