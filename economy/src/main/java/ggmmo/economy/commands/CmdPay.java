package ggmmo.economy.commands;

import ggmmo.economy.GGMMO_Economy;
import ggmmo.economy.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdPay implements CommandExecutor {
    private GGMMO_Economy plugin = GGMMO_Economy.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Pay <player> <amount>
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (args.length == 0 || args.length > 2) return false;

            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer != null) {
                Double amount = Double.parseDouble(args[1]);
                plugin.economyCore.depositPlayer(player.getUniqueId().toString(), targetPlayer.getUniqueId().toString(), amount);
                return true;
            } else {
                MessageManager.playerBad(player, "Couldn't find user: " + args[0]);
                return true;
            }
        }
        MessageManager.consoleBad("Command can only be executed by a player!");
        return true;
    }
}
