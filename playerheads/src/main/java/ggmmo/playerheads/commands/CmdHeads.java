package ggmmo.playerheads.commands;

import ggmmo.playerheads.GGMMO_Playerheads;
import ggmmo.playerheads.inventorygui.InventoryHead;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdHeads implements CommandExecutor {
    private GGMMO_Playerheads plugin;

    public CmdHeads(GGMMO_Playerheads plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;

            InventoryHead inv = new InventoryHead(plugin);
            inv.createHeadInventory(player);

            player.performCommand("give @p skull 64 3 {display:{Name:\"Item Frame\"},SkullOwner:{Id:\"2e302f54-ce41-44af-90a2-f94e1af3c9d2\",Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODEyMmE1MDNkN2E2ZjU3ODAyYjAzYWY3NjI0MTk0YTRjNGY1MDc3YTk5YWUyMWRkMjc2Y2U3ZGI4OGJjMzhhZSJ9fX0=\"}]}}}");

        }
        return true;
    }
}
