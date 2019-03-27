package ggmmo.economy.commands;

import ggmmo.economy.GGMMO_Economy;
import ggmmo.economy.utils.ISubCommand;
import ggmmo.economy.utils.MessageManager;
import ggmmo.economy.utils.PlayerManager;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdCreatePlayerAccount implements CommandExecutor {
    private GGMMO_Economy plugin = GGMMO_Economy.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player player = (Player)sender;
//                if (!player.hasPermission("ggmmo.economy.createaccount")) {
//                    MessageManager.playerBad(player, "You don't have permission");
//                    return true;
//                }

                Document doc = new Document("uuid", player.getUniqueId().toString());
                if (plugin.mongoConnect.getPlayerDataCollection().find(doc).first() == null) { // If the user doesn't already exist
                    plugin.playerManagerHashMap.put(player.getUniqueId(), new PlayerManager(player.getUniqueId().toString(), 0, 0));
                    MessageManager.playerGood(player, "Your account has been created!");
                    return true;
                } else {
                    MessageManager.playerBad(player, "Account already exists!");
                    return true;
                }
            } else {
                MessageManager.consoleBad("Only a player can execute this command without arguments");
                command.setUsage(command.getUsage() + " <User>");
                return false;
            }
        }

        // If command was sent from console... or an admin creating an account for a user
        if (args[0] != null) {
            Player targetPlayer = plugin.getServer().getPlayer(args[0]);
            if (targetPlayer != null) {
                Document doc = new Document("uuid", targetPlayer.getUniqueId().toString());
                if (plugin.mongoConnect.getPlayerDataCollection().find(doc).first() == null) { // If the user doesn't already exist
                    plugin.playerManagerHashMap.put(targetPlayer.getUniqueId(), new PlayerManager(targetPlayer.getUniqueId().toString(), 0, 0));
                    MessageManager.consoleGood("Account for " + targetPlayer.getDisplayName() + " has been created!");
                    if (sender instanceof Player)
                        MessageManager.playerGood((Player)sender,"Account for " + targetPlayer.getDisplayName() + " has been created!");
                } else {
                    MessageManager.consoleBad("Account for " + targetPlayer.getDisplayName() + " already exists!");
                    if (sender instanceof Player)
                        MessageManager.playerBad((Player)sender, "Account for " + targetPlayer.getDisplayName() + " already exists!");
                }
            }
        }

        return true;
    }
}
