package ggmmo.discordchat.events;

import ggmmo.discordchat.GGMMO_DiscordChat;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class DiscordChatListener extends ListenerAdapter implements Listener {
    public JDA jda;
    private GGMMO_DiscordChat plugin;
    private TextChannel chatTextChannel, userLoginChannel;
    private Category minecraftCategory;

    private int onlineMinecraftUsers = 0;

    public DiscordChatListener(GGMMO_DiscordChat plugin) {
        this.plugin = plugin;
        startBot();

        this.minecraftCategory = jda.getCategoryById(plugin.getConfig().getLong("Server.CategoryID"));

        this.chatTextChannel = jda.getTextChannelById(plugin.getConfig().getLong("Server.ChannelID.Chat")); // #general-ggmmo
        this.userLoginChannel = jda.getTextChannelById(plugin.getConfig().getLong("Server.ChannelID.UserLogin")); // #online-users

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        jda.addEventListener(this);
    }

    public void startBot() {
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(plugin.getConfig().getString("Bot.Token"))
                    .build().awaitReady();


        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopBot() {
        jda.shutdown();
    }

    @EventHandler
    public void minecraftChatEvent(AsyncPlayerChatEvent event) {
        String msg = event.getMessage();
        MessageEmbed embed = new EmbedBuilder()
                .setTitle(event.getPlayer().getDisplayName())
                .setDescription(msg)
                .setColor(Color.red)
                .build();
        userLoginChannel.sendMessage(embed);
        this.chatTextChannel.sendMessage("**" + event.getPlayer().getDisplayName() + ":** " + msg).queue();
    }

    @EventHandler
    public void minecraftJoinEvent(PlayerJoinEvent event) {
        String player = event.getPlayer().getDisplayName();

        MessageEmbed embed = new EmbedBuilder()
                                .setTitle("User Joined")
                                .setDescription("User: " + player)
                                .setColor(Color.green)
                                .build();
        userLoginChannel.sendMessage(embed).queue();

        // Update the category title to reflect number of online users
        onlineMinecraftUsers++;
        String categoryName = "Minecraft Server - ";
        minecraftCategory.getManager().setName(categoryName + onlineMinecraftUsers + " online").queue();
    }

    @EventHandler
    public void minecraftQuitEvent(PlayerQuitEvent event) {
        String player = event.getPlayer().getDisplayName();

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("User Disconnected")
                .setDescription("User: " + player)
                .setColor(Color.red)
                .build();
        userLoginChannel.sendMessage(embed).queue();

        // Update the category title to reflect number of online users
        onlineMinecraftUsers = Math.max(0, --onlineMinecraftUsers);
        String categoryName = "Minecraft Server";
        String newName = (onlineMinecraftUsers == 0) ? categoryName : (categoryName + " - " + onlineMinecraftUsers + " online");
        minecraftCategory.getManager().setName(newName).queue();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isFake() || event.isWebhookMessage() || event.getChannel().compareTo(chatTextChannel) != 0) return;

        String message = event.getMessage().getContentRaw();
        // don't broadcast any tidy up commands that are executed in the listened channel
        if (isBotPrefix(message)) return;

        Member user = event.getMember();

        Bukkit.broadcastMessage("<" + ChatColor.GOLD + user.getNickname() + "> " +  ChatColor.RESET + message);
    }

    public boolean isBotPrefix(String string) {
        return (string.startsWith(plugin.getConfig().getString("Bot.Prefix")));
    }
}
