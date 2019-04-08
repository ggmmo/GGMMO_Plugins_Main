package ggmmo.discordchat;

import ggmmo.discordchat.events.DiscordChatListener;
import ggmmo.discordchat.utils.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class GGMMO_DiscordChat extends JavaPlugin {
    public ConsoleCommandSender console;
    public File data;
    public FileConfiguration playerData;
    private DiscordChatListener discordClient;

    @Override
    public void onEnable() {
        console = getServer().getConsoleSender();
        console.sendMessage(ChatColor.GREEN + "[GGMMO] Discord Chat enabled");

        // Load configs
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        createDataConfig();

        // Start the discord service
        discordClient = new DiscordChatListener(this);

        // Update number of users online (In case of reload
        discordClient.updateOnlineUsers();

        for (Player player : getServer().getOnlinePlayers()) {
            // Update verified users
            if (playerData.contains("Data." + player.getUniqueId().toString())) {
                discordClient.verifiedMembers.add(player.getUniqueId());
            }
        }
    }

    private void createDataConfig() {
        data = new File(getDataFolder() + File.separator + "data.yml");
        if (!data.exists()) {
            MessageManager.consoleGood("Creating file data.yml");
            this.saveResource("data.yml", false);
        }

        playerData = new YamlConfiguration();
        try {
            playerData.load(data);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        console.sendMessage(ChatColor.YELLOW + "[GGMMO] Discord Chat disabled");
        discordClient.stopBot();
    }
}
