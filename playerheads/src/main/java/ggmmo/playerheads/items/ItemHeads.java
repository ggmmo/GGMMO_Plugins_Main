package ggmmo.playerheads.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class ItemHeads {
    public ItemStack head_itemframe = new ItemStack(Material.SKULL_ITEM, 1, (short)3);

    public ItemHeads() {
        //Head_Itemframe
        ItemMeta head_itemframe_meta = head_itemframe.getItemMeta();
        head_itemframe_meta.setDisplayName(ChatColor.GREEN + "Item Frame");
//        head_itemframe_meta;
    }
}
