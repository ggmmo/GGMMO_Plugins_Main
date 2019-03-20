package ggmmo.essentials.ggmmo.essentials.events;

import ggmmo.essentials.GGMMO_Essentials;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;

public class EventClass implements Listener {
    private GGMMO_Essentials plugin = GGMMO_Essentials.getPlugin(GGMMO_Essentials.class);

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
    }

    @EventHandler
    public void playerInteraction(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();

        if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
            Block block = event.getClickedBlock();

            // Disable Workbench
            if (block.getType().equals(Material.WORKBENCH)) {
                event.setCancelled(true);
            }
            // Disable regular Chest
            if (block.getType().equals(Material.CHEST)) {
                event.setCancelled(true);
            }
            // Disable Anvil
            if (block.getType().equals(Material.ANVIL)) {
                event.setCancelled(true);
            }
            // Disable Furnace
            if (block.getType().equals(Material.FURNACE)) {
                event.setCancelled(true);
            }
            // Disable Enchanting table
            if (block.getType().equals(Material.ENCHANTMENT_TABLE)) {
                event.setCancelled(true);
            }
            // Disable Beds
            if (block.getType().equals(Material.BED) || block.getType().equals(Material.BED_BLOCK)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void blockPlacement(BlockPlaceEvent event)  {
        Player player = event.getPlayer();

        if (!player.hasPermission(new Permission("ggmmo.essentials.placeblock"))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void blockBreaking(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("ggmmo.essentials.breakblock ")) {
            event.setCancelled(true);
        }
    }
}
