package ggmmo.economy;

import ggmmo.economy.utils.MessageManager;
import ggmmo.economy.utils.PlayerManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("Deprecation")
public class EconomyCore implements Economy {
    private GGMMO_Economy plugin = GGMMO_Economy.getPlugin();

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String uuid) {
        return plugin.mongoConnect.getPlayerDataCollection().find(new Document("uuid", uuid)) != null;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return plugin.mongoConnect.getPlayerDataCollection().find(new Document("uuid", offlinePlayer.getUniqueId().toString())) != null;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @Override
    public double getBalance(String uuid) {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if (player != null && plugin.economyCore.hasAccount(uuid)) {
            if (plugin.playerManagerHashMap.containsKey(UUID.fromString(uuid))) {
                PlayerManager playerManager = plugin.playerManagerHashMap.get(UUID.fromString(uuid));
                return playerManager.getBalance();
            }
        } else {
            MessageManager.playerBad(player,"You do not have an account");
        }

        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return 0;
    }

    @Override
    public double getBalance(String s, String s1) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return 0;
    }

    @Override
    public boolean has(String s, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return false;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String uuid, double amount) {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if (hasAccount(uuid)) {
            if (checkHashMap(uuid)) {
                PlayerManager playerManager = getHashMap(uuid);
                double balance = playerManager.getBalance();

                if (amount != -1 && balance > amount) {
                    playerManager.setBalance(balance - amount);
                    MessageManager.consoleGood("Deducted $" + amount + " from " + ChatColor.AQUA + player.getDisplayName());
                    MessageManager.playerBad(player, "$"+ amount + " has been removed from your account");
                    return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "Deducted $" + amount);
                }
                if (amount == -1) {
                    playerManager.setBalance(0);
                    MessageManager.consoleGood("Reset account for: " + player.getDisplayName());
                    MessageManager.playerBad(player, "Your account has been reset");
                    return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "Reset account for: " + player.getDisplayName());
                }
            }
        }
        MessageManager.consoleBad(player.getDisplayName() + " doesn't have an account!");
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "You don't have an account!");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String uuid, double amount) {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if (hasAccount(uuid)) {
            if (checkHashMap(uuid)) {
                PlayerManager playerManager = getHashMap(uuid);
                double balance = playerManager.getBalance();
                playerManager.setBalance(balance + amount);
                MessageManager.playerGood(player, "You have been paid $" + amount);
                return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, "You have been paid $" + amount);
            }
        }
        MessageManager.playerBad(player,"Someone tried to pay you $" + amount + ", but you don't have an account!");
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "You don't have an account!");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String senderUUID, String receiverUUID, double amount) {
        PlayerManager senderManager = getHashMap(senderUUID);
        PlayerManager receiverMenager = getHashMap(receiverUUID);
        Player sender = Bukkit.getPlayer(UUID.fromString(senderUUID));
        Player receiver = Bukkit.getPlayer(UUID.fromString(receiverUUID));

        if (hasAccount(senderUUID) && hasAccount(receiverUUID)) {
            if (checkHashMap(senderUUID) && checkHashMap(receiverUUID)) {
                double senderBal = senderManager.getBalance();
                double receiverBal = receiverMenager.getBalance();

                if (senderBal >= amount) {
                    senderManager.setBalance(senderBal - amount);
                    receiverMenager.setBalance(receiverBal + amount);

                    MessageManager.playerGood(sender, "You have paid " + ChatColor.AQUA + receiver.getDisplayName() + ChatColor.GREEN + " $" + amount);
                    MessageManager.playerGood(receiver, ChatColor.AQUA + sender.getDisplayName() + ChatColor.GREEN + " has paid you $" + amount);
                    MessageManager.consoleInfo(sender.getDisplayName() + " has paid " + receiver.getDisplayName() + " $" + amount);
                    return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.SUCCESS, sender.getDisplayName() + " has paid " + receiver.getDisplayName() + " $" + amount);
                }
            }
        }
        MessageManager.playerBad(sender, "Both players must have an account!");
        return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, "Both players mush have an account!");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    public boolean checkHashMap(String uuid) {
        return plugin.playerManagerHashMap.containsKey(UUID.fromString(uuid));
    }

    public PlayerManager getHashMap(String uuid) {
        return plugin.playerManagerHashMap.get(UUID.fromString(uuid));
    }
}
