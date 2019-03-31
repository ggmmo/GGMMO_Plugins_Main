package ggmmo.economy.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ggmmo.economy.GGMMO_Economy;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;

public class MongoConnect {
    private MongoDatabase database;
    private static MongoCollection playerData;

    private GGMMO_Economy plugin = GGMMO_Economy.getPlugin();

    public void connect() {
        String connectionString = plugin.getConfig().getString("uri");
        MongoClient client = MongoClients.create(connectionString);
        setDatabase(client.getDatabase("GGMMO"));
        setPlayerData(database.getCollection("PlayerData"));
        MessageManager.consoleGood(String.format("Database connected!"));
    }

    public void setPlayerDataDocument(Object value, String identifier, String uuid) {
        Document document = new Document("uuid", uuid);
        Bson newValue = new Document(identifier, value);
        Bson updateOperation = new Document("$set", newValue);
        playerData.updateOne(document, updateOperation);
    }

    public Object getPlayerDataDocument(String identifier, String uuid) {
        Document document = new Document("uuid", uuid);
        if (playerData.find(document).first() != null) {
            Document found = (Document)playerData.find(document).first();
            return found.get(identifier);
        }

        MessageManager.consoleBad("Document is null for uuid: " + uuid);
        return null;
    }

    public void loadPlayerData(Player player) {
        MessageManager.playerInfo(player,"Loaded data for " + player.getDisplayName());
        Document playerData = (Document)plugin.mongoConnect.getPlayerDataCollection().find(new Document("uuid",player.getUniqueId().toString())).first();
        double balance = playerData.getDouble("balance");
        double bankAccount = playerData.getDouble("bank-account");

        plugin.playerManagerHashMap.put(player.getUniqueId(), new PlayerManager(player.getUniqueId().toString(), balance, bankAccount));
    }

    public void addNewPlayer(Player player) {
        if (getPlayerDataCollection().find(new Document("uuid", player.getUniqueId().toString())).first() == null) {
            plugin.playerManagerHashMap.put(player.getUniqueId(), new PlayerManager(player.getUniqueId().toString(), 0, 0));
            MessageManager.playerGood(player, "Your account has been created!");
            MessageManager.consoleInfo("Account created for " + player.getDisplayName());
        } else {
            MessageManager.consoleInfo("Loading info for " + player.getDisplayName());
            loadPlayerData(player);
        }
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

    public static MongoCollection getPlayerData() {
        return playerData;
    }

    public static void setPlayerData(MongoCollection playerData) {
        MongoConnect.playerData = playerData;
    }

    public MongoCollection getPlayerDataCollection() {
        return playerData;
    }
}
