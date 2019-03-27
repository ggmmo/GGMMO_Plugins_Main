package ggmmo.playerheads.inventorygui;

import ggmmo.playerheads.GGMMO_Playerheads;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryHead {
    private GGMMO_Playerheads plugin;

    public InventoryHead(GGMMO_Playerheads plugin) {
        this.plugin = plugin;
    }

    public void createHeadInventory(Player player) {
        Inventory headInventory = plugin.getServer().createInventory(null, 27, "Custom Heads");
        player.openInventory(headInventory);
    }

}
