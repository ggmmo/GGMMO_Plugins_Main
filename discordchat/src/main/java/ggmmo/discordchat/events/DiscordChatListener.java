package ggmmo.discordchat.events;

import ggmmo.discordchat.GGMMO_DiscordChat;
import ggmmo.discordchat.utils.MessageManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DiscordChatListener extends ListenerAdapter implements Listener, CommandExecutor {
    public JDA jda;
    private GGMMO_DiscordChat plugin;
    private TextChannel chatTextChannel, userLoginChannel, verifyTextChannel;
    private Category minecraftCategory;
    private Guild guild;

    // Minecraft - Discord verification
    public HashMap<UUID, String> uuidCodeMap = new HashMap<>();
    public HashMap<UUID, String> uuidIDMap = new HashMap<>();
    public List<UUID> verifiedMembers = new ArrayList<>();

    private int onlineMinecraftUsers = 0;
    private final String discordErrorPrefix = ":x: **|**", discordSuccessPrefix = ":white_check_mark: **|**";

    public DiscordChatListener(GGMMO_DiscordChat plugin) {
        this.plugin = plugin;
        startBot();

        this.minecraftCategory = jda.getCategoryById(plugin.getConfig().getLong("Server.CategoryID"));

        this.chatTextChannel = jda.getTextChannelById(plugin.getConfig().getLong("Server.ChannelID.Chat")); // #general-ggmmo
        this.userLoginChannel = jda.getTextChannelById(plugin.getConfig().getLong("Server.ChannelID.UserLogin")); // #online-users
        this.verifyTextChannel = jda.getTextChannelById(plugin.getConfig().getLong("Server.ChannelID.AccountLink")); // #bot-commands

        this.guild = jda.getGuildById(plugin.getConfig().getLong("Server.GuildID")); // GGMMO

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        jda.addEventListener(this);

        plugin.getCommand("verify").setExecutor(this);
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
        minecraftCategory.getManager().setName("Minecraft Server").queue();
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
        Player player = event.getPlayer();
        String playerName = player.getDisplayName();

        MessageEmbed embed = new EmbedBuilder()
                                .setTitle("User Joined")
                                .setDescription("User: " + playerName)
                                .setColor(Color.green)
                                .build();
        userLoginChannel.sendMessage(embed).queue();

        // Update the category title to reflect number of online users
        updateOnlineUsers();

        // Update verified users
        if (plugin.playerData.contains("Data." + player.getUniqueId().toString())) {
            verifiedMembers.add(player.getUniqueId());
        }
    }

    @EventHandler
    public void minecraftQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getDisplayName();

        MessageEmbed embed = new EmbedBuilder()
                .setTitle("User Disconnected")
                .setDescription("User: " + playerName)
                .setColor(Color.red)
                .build();
        userLoginChannel.sendMessage(embed).queue();

        // Update the category title to reflect number of online users
        updateOnlineUsers();

        // Update verified users
        if (plugin.playerData.contains("Data." + player.getUniqueId().toString())) {
            verifiedMembers.remove(player.getUniqueId());
        }
    }

    public void updateOnlineUsers() {
        onlineMinecraftUsers = plugin.getServer().getOnlinePlayers().size();
        String categoryName = "Minecraft Server";
        String newName = (onlineMinecraftUsers == 0) ? categoryName : (categoryName + " - " + onlineMinecraftUsers + " online");
        minecraftCategory.getManager().setName(newName).queue();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isFake() || event.isWebhookMessage()) return;

        String message = event.getMessage().getContentRaw();

        if (isBotPrefix(message)) {
            FileConfiguration config = plugin.getConfig();

            // Only perform bot commands in #bot-commands
            if (event.getChannel().compareTo(verifyTextChannel) != 0) return;

            MessageHistory history = new MessageHistory(verifyTextChannel);
            List<Message> msgList;

            String[] args = event.getMessage().getContentRaw().split(" ");
            String command = args[0].replaceAll(getBotPrefix(), "");

            // !link <username>
            if (command.equalsIgnoreCase(config.getString("Bot.Commands.BotLink"))) {
                // Make sure we have enough parameters
                if (args.length != 2) {
                    event.getMessage().delete().complete();
                    verifyTextChannel.sendMessage(discordErrorPrefix + " Need to specify a player!").complete().delete().completeAfter(5, TimeUnit.SECONDS);
                    return;
                }

                // Check that the discord user isn't already verified
                if (event.getMember().getRoles().stream().filter(role -> role.getName().equalsIgnoreCase("verified")).findAny().orElse(null) != null) {
                    event.getMessage().delete().complete();
                    verifyTextChannel.sendMessage(discordErrorPrefix + " You are already verified!").complete().delete().completeAfter(5, TimeUnit.SECONDS);
                    return;
                }

                Player target = Bukkit.getPlayer(args[1]);

                // Check if the player is online
                if (target == null) {
                    event.getMessage().delete().complete();
                    verifyTextChannel.sendMessage(discordErrorPrefix + " Player: " + args[1] + " is not online!").complete().delete().completeAfter(5, TimeUnit.SECONDS);
                    return;
                }

                // Generate a new code if one has not been assigned
                if (!uuidCodeMap.containsKey(target.getUniqueId())) {
                    Random rand = new Random();
                    String randomCode = String.valueOf(rand.nextInt(800000) + 200000) + (char)(rand.nextInt(90) + 65);
                    uuidCodeMap.put(target.getUniqueId(), randomCode);
                    uuidIDMap.put(target.getUniqueId(), event.getAuthor().getId());
                }
                verifyTextChannel.sendMessage(discordSuccessPrefix + "Your verification code has been generated.\nPlease use this command in game: " +
                        "`/verify " + uuidCodeMap.get(target.getUniqueId()) + "`").queue();
            }
        } else {
            // Broadcast chat only if regular messages are issued in the minecraft chat channel
            if (event.getChannel().compareTo(chatTextChannel) != 0) return;

            Member user = event.getMember();

            Bukkit.broadcastMessage("<" + ChatColor.GOLD + user.getNickname() + ChatColor.RESET + "> " + message);
        }
    }

    public boolean isBotPrefix(String string) {
        return (string.startsWith(plugin.getConfig().getString("Bot.Prefix")));
    }

    public String getBotPrefix() {
        return plugin.getConfig().getString("Bot.Prefix");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageManager.consoleBad("Only players can issue this command");
            return true;
        }

        Player player = (Player)sender;
        if (args.length != 1) return false;

        if (plugin.playerData.contains("Data." + player.getUniqueId().toString())) {
            MessageManager.playerBad(player, "You have already linked your account!");
        }

        if (!uuidCodeMap.containsKey(player.getUniqueId())) {
            MessageManager.playerBad(player, "No pending verification process.");
            return true;
        }

        String actualCode = uuidCodeMap.get(player.getUniqueId());
        if (!actualCode.equals(args[0])) {
            MessageManager.playerBad(player, "Invalid verification code");
            return true;
        }

        Member target = guild.getMemberById(uuidIDMap.get(player.getUniqueId()));
        if (target != null) {
            Role verifiedRole = guild.getRolesByName("Verified", false).get(0);
            guild.getController().addSingleRoleToMember(target, verifiedRole).queue();
            MessageManager.playerGood(player, "Account has been verified and linked to discord account: " + target.getUser().getAsTag());
            verifyTextChannel.sendMessage(target.getAsMention() + " You have linked your account successfully").queue();

            plugin.playerData.set("Data." + player.getUniqueId(), uuidIDMap.get(player.getUniqueId()));
            try {
                plugin.playerData.save(plugin.data);
            } catch (IOException e) {
                e.printStackTrace();
            }

            uuidCodeMap.remove(player.getUniqueId());
            uuidIDMap.remove(player.getUniqueId());
            verifiedMembers.add(player.getUniqueId());
        }

        return true;
    }
}
