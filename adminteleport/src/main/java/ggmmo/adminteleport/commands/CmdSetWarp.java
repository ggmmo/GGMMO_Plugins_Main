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

public class CmdSetWarp implements CommandExecutor {
    GGMMO_AdminTeleport plugin = GGMMO_AdminTeleport.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            // /SetWarp <name>
            // does the command contain one argument
            if (args.length != 1) return false;

            Player player = (Player)sender;
            Location loc = player.getLocation();
            FileConfiguration config = plugin.getConfig();

            //TODO: add confirmation prompt if a player is going to override an existing warp
            //if(config.contains("warps."+args[0])){
            //    MessageManager.playerBad(player,"Warp aleady exists. [insert prompt to confirm override");
            //    return false;
            //}

            config.set("warps."+ args[0]+".world",loc.getWorld().getName());
            config.set("warps."+ args[0]+".x", loc.getBlockX());
            config.set("warps."+ args[0]+".y", loc.getBlockY());
            config.set("warps."+ args[0]+".z", loc.getBlockZ());
            config.set("warps."+ args[0]+".yaw", loc.getYaw());
            config.set("warps."+ args[0]+".pitch", loc.getPitch());
            plugin.saveConfig();

            MessageManager.playerInfo(player, "Warp set: " + ChatColor.AQUA + args[0]);
            return true;
        }

        return true;
    }
}
