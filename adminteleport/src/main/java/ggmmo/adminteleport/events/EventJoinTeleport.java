package ggmmo.adminteleport.events;

import ggmmo.adminteleport.GGMMO_AdminTeleports;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventJoinTeleport implements Listener, CommandExecutor {
    private GGMMO_AdminTeleports plugin;

    public EventJoinTeleport(GGMMO_AdminTeleports plugin){
        this.plugin = plugin;
        this.plugin.getCommand("setjoinspawn").setExecutor(this);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        Server s = plugin.getServer();

        World world = s.getWorld(plugin.getConfig().getString("joinspawn.world"));
        int x = (plugin.getConfig().getInt("joinspawn.x"));
        int y = (plugin.getConfig().getInt("joinspawn.y"));
        int z = (plugin.getConfig().getInt("joinspawn.z"));
        int yaw = (plugin.getConfig().getInt("joinspawn.yaw"));
        int pitch = (plugin.getConfig().getInt("joinspawn.pitch"));

        Location location = new Location(world,x,y,z,yaw,pitch);

        player.teleport(location);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player)sender;
            sender.sendMessage("You set the join spawn");
            plugin.getConfig().set("joinspawn.world", player.getLocation().getWorld().getName());
            plugin.getConfig().set("joinspawn.x", player.getLocation().getBlockX());
            plugin.getConfig().set("joinspawn.y", player.getLocation().getBlockY());
            plugin.getConfig().set("joinspawn.z", player.getLocation().getBlockZ());
            plugin.getConfig().set("joinspawn.yaw", player.getLocation().getYaw());
            plugin.getConfig().set("joinspawn.pitch", player.getLocation().getPitch());
            plugin.saveConfig();
        }
        return true;
    }
}
