package ggmmo.cinematics.util;

import ggmmo.cinematics.Cinematics;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleFunctions {
    private static final Cinematics plugin = Cinematics.getPlugin();

    public static void createHelix(Player player, EnumParticle particleType, double radius, double maxHeight, double particleSpeed,
                                   float xOffset, float yOffset, float zOffset, float data, int count) {

        new BukkitRunnable() {
            @Override
            public void run() {
                Location loc = player.getLocation();

                for ( double y = 0; y < maxHeight; y += particleSpeed)

                {
                    double x = radius * Math.cos(y);
                    double z = radius * Math.sin(y);

                    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particleType, true,
                            (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z),
                            xOffset, yOffset, zOffset, data, count);

                    for (Player online : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                    }
                }
            }
        }.runTask(plugin);
    }
}
