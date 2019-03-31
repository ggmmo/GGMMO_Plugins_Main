package ggmmo.economy.events;

import ggmmo.economy.GGMMO_Economy;
import ggmmo.economy.utils.MessageManager;
import ggmmo.economy.utils.PlayerManager;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventJoin implements Listener {
    private GGMMO_Economy plugin = GGMMO_Economy.getPlugin();

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // If the player account doesn't exist
        if (!plugin.economyCore.hasAccount(player.getUniqueId().toString())) {
            plugin.mongoConnect.addNewPlayer(player);
            return;
        } else {
            plugin.mongoConnect.loadPlayerData(player);
            return;
        }
    }
}
