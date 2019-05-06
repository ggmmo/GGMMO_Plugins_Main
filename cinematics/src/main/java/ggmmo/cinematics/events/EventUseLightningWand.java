package ggmmo.cinematics.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventUseLightningWand implements Listener {
    @EventHandler
    public void onUseLightningWand(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && player.getInventory() != null) {
            if (player.getInventory().getItemInMainHand() != null) {
                String itemName = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();

                if (itemName != null && itemName.equals("Lightning Wand")) {
                    world.strikeLightning(player.getTargetBlock(null, 50).getLocation());
                }

            }
        }
    }
}
