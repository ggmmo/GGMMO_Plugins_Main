package ggmmo.discordchat;

import ggmmo.discordchat.events.DiscordChatListener;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class GGMMO_DiscordChat extends JavaPlugin {
    public ConsoleCommandSender console;
    private DiscordChatListener discordClient;

    @Override
    public void onEnable() {
        console = getServer().getConsoleSender();
        console.sendMessage(ChatColor.GREEN + "[GGMMO] Discord Chat enabled");

        // Load configs
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        // Start the discord service
        discordClient = new DiscordChatListener(this);
    }

    @Override
    public void onDisable() {
        console.sendMessage(ChatColor.YELLOW + "[GGMMO] Discord Chat disabled");
        discordClient.stopBot();
    }
}
