package ggmmo.economy.events;

import ggmmo.economy.GGMMO_Economy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventJoin implements Listener {
    private GGMMO_Economy plugin;

    public EventJoin(GGMMO_Economy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

    }
}
