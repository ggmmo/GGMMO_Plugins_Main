package ggmmo.playerheads.events;

import ggmmo.playerheads.GGMMO_Playerheads;
import org.bukkit.event.Listener;

public class EventClass implements Listener {
    private GGMMO_Playerheads plugin;

    public EventClass(GGMMO_Playerheads plugin) {
        this.plugin = plugin;
    }
}
