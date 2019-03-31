package ggmmo.economy.commands;

import ggmmo.economy.GGMMO_Economy;
import ggmmo.economy.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdBalance implements CommandExecutor {
    private GGMMO_Economy plugin = GGMMO_Economy.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            MessageManager.playerInfo(player,"Balance: " + ChatColor.GREEN + "$" + plugin.economyCore.getBalance(player.getUniqueId().toString()));
        } else {
            MessageManager.consoleBad("Only players can use this command");
        }
        return true;
    }
}
