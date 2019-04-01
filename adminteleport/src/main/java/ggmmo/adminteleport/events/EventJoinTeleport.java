package ggmmo.adminteleport.events;

import ggmmo.adminteleport.GGMMO_AdminTeleport;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventJoinTeleport implements Listener {
    GGMMO_AdminTeleport plugin = GGMMO_AdminTeleport.getPlugin();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Server server = plugin.getServer();
        FileConfiguration config = plugin.getConfig();

        World world = server.getWorld(config.getString("joinspawn.world"));
        int x = config.getInt("joinspawn.x");
        int y = config.getInt("joinspawn.y");
        int z = config.getInt("joinspawn.z");
        int yaw = config.getInt("joinspawn.yaw");
        int pitch = config.getInt("joinspawn.pitch");
        Location location = new Location(world, x, y, z, yaw, pitch);

        player.teleport(location);
    }
}
