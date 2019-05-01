package ggmmo.npcfactory.commands;

import ggmmo.npcfactory.NpcFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CmdNpcWand implements CommandExecutor {
    NpcFactory plugin = NpcFactory.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 0) return false;

        if (!(sender instanceof Player)) {
            plugin.getServer().getConsoleSender().sendMessage("This command can only be used by a player");
            return true;
        }

        ItemStack npcWand = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = npcWand.getItemMeta();

        meta.setDisplayName(ChatColor.WHITE + "NPC Wand");
        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.AQUA + "Shift" + ChatColor.GRAY + " + " + ChatColor.GOLD + "Right Mouse" + ChatColor.GRAY +
                " to open the wand menu");
        meta.setLore(lore);
        npcWand.setItemMeta(meta);

        ((Player) sender).getInventory().setItemInMainHand(npcWand);

        return true;
    }
}
