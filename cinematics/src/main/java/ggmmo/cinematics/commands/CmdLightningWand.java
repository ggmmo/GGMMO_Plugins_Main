package ggmmo.cinematics.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CmdLightningWand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            ItemStack lightningWand = new ItemStack(Material.STICK);
            ItemMeta meta = lightningWand.getItemMeta();

            meta.setDisplayName("Lightning Wand");
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            lightningWand.setItemMeta(meta);

            Player player = (Player)sender;
            player.getInventory().setItemInMainHand(lightningWand);
            return true;
        }
        return true;
    }
}
