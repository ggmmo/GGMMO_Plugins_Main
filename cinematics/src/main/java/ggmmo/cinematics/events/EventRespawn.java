package ggmmo.cinematics.events;

import ggmmo.cinematics.util.ParticleFunctions;
import net.minecraft.server.v1_12_R1.EnumParticle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EventRespawn implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        ParticleFunctions.createHelix(player, EnumParticle.REDSTONE, 1, 5,
                0.00025d, 0, 0, 0, 1, 1);
    }
}
