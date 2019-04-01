package ggmmo.adminteleport.utils;

import ggmmo.adminteleport.GGMMO_AdminTeleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageManager {
    static GGMMO_AdminTeleport plugin = GGMMO_AdminTeleport.getPlugin();
    private static String prefix = ChatColor.GRAY + String.format("[%s] ",plugin.getName()) + ChatColor.RESET;

    public static void consoleGood(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.GREEN + message);
    }

    public static void consoleBad(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + message);
    }

    public static void consoleInfo(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.YELLOW + message);
    }

    public static void playerGood(Player player, String message) {
        player.sendMessage(prefix + ChatColor.GREEN + message);
    }

    public static void playerBad(Player player, String message) {
        player.sendMessage(prefix + ChatColor.RED + message);
    }

    public static void playerInfo(Player player, String message) {
        player.sendMessage(prefix + ChatColor.YELLOW + message);
    }
}
