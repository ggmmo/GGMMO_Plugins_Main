package ggmmo.serverutils.commands;

import ggmmo.serverutils.GGMMO_ServerUtils;
import ggmmo.serverutils.utils.MessageManager;
import net.dv8tion.jda.core.entities.MessageHistory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdPluginEnable implements CommandExecutor {
    private GGMMO_ServerUtils plugin = GGMMO_ServerUtils.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.consoleBad("Only a player can use this command!");
            return true;
        }

        Player player = (Player)sender;

        if (args.length != 1)
            return false;

        // Only Intrepid249 can use this command
        if (!plugin.isAuthorisedUser(player.getUniqueId())) {
            MessageManager.playerBad(player,"You are not authorised to issue this command!");
            return true;
        }

        if (Bukkit.getPluginManager().getPlugin(args[0]) == null) {
            MessageManager.playerBad(player, "Could not find specified plugin");
            return true;
        }

        Bukkit.getPluginManager().enablePlugin(Bukkit.getPluginManager().getPlugin(args[0]));
        Bukkit.broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "[GGMMO - Plugin enabled: " + args[0] + "]");

        return true;
    }
}
