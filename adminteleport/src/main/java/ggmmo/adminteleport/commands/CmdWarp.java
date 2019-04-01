package ggmmo.adminteleport.commands;

import ggmmo.adminteleport.GGMMO_AdminTeleport;
import ggmmo.adminteleport.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.security.MessageDigest;

public class CmdWarp implements CommandExecutor {
    GGMMO_AdminTeleport plugin = GGMMO_AdminTeleport.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player)sender;
            Server server = plugin.getServer();
            FileConfiguration config = plugin.getConfig();

            //TODO: give error message when a warp name isn't in the config
            //TODO: have clickable text to makes the process easier
                if (args.length == 1) {
                    World world = server.getWorld(config.getString("warps." + args[0] + ".world"));
                    int x = config.getInt("warps." + args[0] + ".x");
                    int y = config.getInt("warps." + args[0] + ".y");
                    int z = config.getInt("warps." + args[0] + ".z");
                    int yaw = config.getInt("warps." + args[0] + ".yaw");
                    int pitch = config.getInt("warps." + args[0] + ".pitch");
                    Location location = new Location(world, x, y, z, yaw, pitch);

                    player.teleport(location);
                    MessageManager.playerInfo(player, "You teleported to: " + ChatColor.AQUA + args[0]);
                }

                if (args.length == 0) {
                    String output = "Warps: " + ChatColor.AQUA;
                    String[] warpList = config.getConfigurationSection("warps").getKeys(false).toArray(new String[0]);
                    for(String warp : warpList)
                    {
                        boolean isLastItem = warp.equalsIgnoreCase(warpList[warpList.length - 1]);
                        output += (isLastItem) ? warp : warp + ", ";
                    }
                    MessageManager.playerInfo(player,output);
                }

        }
        return true;
    }
}
