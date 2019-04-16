package ggmmo.serverutils.commands;

import ggmmo.serverutils.GGMMO_ServerUtils;
import ggmmo.serverutils.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdGetUuid implements CommandExecutor {
    private GGMMO_ServerUtils plugin = GGMMO_ServerUtils.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.consoleBad("This can only be executed by a player");
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

        Player target = Bukkit.getPlayer(args[0]);
        MessageManager.playerInfo(player, target.getUniqueId().toString());

        return true;
    }
}
