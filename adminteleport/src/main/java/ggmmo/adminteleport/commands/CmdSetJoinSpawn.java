package ggmmo.adminteleport.commands;

import ggmmo.adminteleport.GGMMO_AdminTeleport;
import ggmmo.adminteleport.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CmdSetJoinSpawn implements CommandExecutor {
    GGMMO_AdminTeleport plugin = GGMMO_AdminTeleport.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            // /setJoinSpawn
            if (args.length != 0) return false;

            Player player = (Player)sender;
            Location loc = player.getLocation();
            FileConfiguration config = plugin.getConfig();
            config.set("joinspawn.world", loc.getWorld().getName());
            config.set("joinspawn.x", loc.getBlockX());
            config.set("joinspawn.y", loc.getBlockY());
            config.set("joinspawn.z", loc.getBlockZ());
            config.set("joinspawn.yaw", loc.getYaw());
            config.set("joinspawn.pitch", loc.getPitch());
            plugin.saveConfig();

            MessageManager.playerGood(player, "Join spawn set to: " + ChatColor.AQUA + loc.getBlockX() +", " + loc.getBlockY() + ", " + loc.getBlockZ());
            return true;
        }

        MessageManager.consoleBad("Only players can issue this command!");
        return true;
    }
}
