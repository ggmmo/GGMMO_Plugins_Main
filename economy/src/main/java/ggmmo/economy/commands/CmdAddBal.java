package ggmmo.economy.commands;

import ggmmo.economy.GGMMO_Economy;
import ggmmo.economy.utils.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdAddBal implements CommandExecutor {
    GGMMO_Economy plugin = GGMMO_Economy.getPlugin();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // addBal <player> <amouont>
        if (args.length == 0 || args.length > 2) return false;

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer != null) {
            Double amount = Double.parseDouble(args[1]);
            if (sender instanceof Player)
                MessageManager.playerGood((Player)sender,"Added $" + amount + " to " + targetPlayer.getDisplayName() + "'s account");
            else
                MessageManager.consoleGood("Added $" + amount + " to " + targetPlayer.getDisplayName() + "'s account");

            plugin.economyCore.depositPlayer(targetPlayer.getUniqueId().toString(), amount);
            return true;
        } else {
            String errMsg = "Couldn't find user: " + args[0];
            if (sender instanceof Player)
                MessageManager.playerBad((Player)sender, errMsg);
            MessageManager.consoleBad(errMsg);
            return true;
        }
    }
}
