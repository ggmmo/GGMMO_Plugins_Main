package ggmmo.cinematics.commands;

import ggmmo.cinematics.Cinematics;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CmdPlayTitle implements CommandExecutor {
    Cinematics plugin = Cinematics.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // <command> <player> <message> [fadeInTime] [displayDuration] [fadeOutTime] [ChatColor]
        if (sender instanceof Player) {
            int fadeInTime = 40, displayDuration = 20, fadeOutTime = 20;
            String message = "", chatColor = "§a";

            if (args.length >= 2) {
                if (args.length >= 3)
                    fadeInTime = Integer.parseInt(args[2]);
                if (args.length >= 4)
                    displayDuration = Integer.parseInt(args[3]);
                if (args.length >= 5)
                    displayDuration = Integer.parseInt(args[4]);
                if (args.length == 6)
                    chatColor = args[5];

                message = args[1].replaceAll("_"," ");


                PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(EnumTitleAction.TITLE,
                        ChatSerializer.a("{\"text\":\"" + chatColor + message + "\"}"), fadeInTime, displayDuration, fadeOutTime);

                Player targetPlayer;
                if (!args[0].equals("@a")) {
                    targetPlayer = plugin.getServer().getPlayer(args[0]);
                    if (targetPlayer != null)
                        ((CraftPlayer)targetPlayer).getHandle().playerConnection.sendPacket(titlePacket);
                } else {
                    for (Player p : plugin.getServer().getOnlinePlayers()) {
                        targetPlayer = p;
                        if (targetPlayer != null)
                            ((CraftPlayer)targetPlayer).getHandle().playerConnection.sendPacket(titlePacket);
                    }
                }
            }
        }

        return true;
    }
}
