package ggmmo.npceditor.commands;

import ggmmo.npceditor.GGMMO_NpcEditor;
import ggmmo.npceditor.utils.MessageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdNpc implements CommandExecutor {
    GGMMO_NpcEditor plugin = GGMMO_NpcEditor.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.consoleBad("This command can only be used by players!");
            return true;
        }

        // /npc <create|select|remove> <name>
        if (args.length != 2) return false;

        Player player = (Player)sender;

        if (args[0].equalsIgnoreCase("create")) {
            String npcName = args[1];
            plugin.npcManager.createNPC(player, npcName);
        }

        return true;
    }
}
