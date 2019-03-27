package ggmmo.economy.utils;

import ggmmo.economy.GGMMO_Economy;
import org.bson.Document;

public class PlayerManager {
    private GGMMO_Economy plugin = GGMMO_Economy.getPlugin();
    private MongoConnect mongoConnect = plugin.mongoConnect;

    private String uuid;
    private double balance;
    private double bankAccount;

    public PlayerManager(String uuid, double balance, double bankAccount) {
        this.uuid = uuid;
        this.balance = balance;
        this.bankAccount = bankAccount;

        Document document = new Document("uuid", uuid);
        if (mongoConnect.getPlayerDataCollection().find(document).first() == null) {
            document.append("balance", balance);
            document.append("bank-account", bankAccount);
            mongoConnect.getPlayerDataCollection().insertOne(document);
        }
    }

    public String getUuid() {
        return uuid;
    }

    public double getBalance() {
        return (double)mongoConnect.getPlayerDataDocument("balance", uuid);
    }

    public void setBalance(double balance) {
        this.balance = balance;
        mongoConnect.setPlayerDataDocument(balance,"balance", uuid);
    }

    public double getBankAccount() {
        return (double)mongoConnect.getPlayerDataDocument("bank-account", uuid);
    }

    public void setBankAccount(double bankAccount) {
        this.bankAccount = bankAccount;
        mongoConnect.setPlayerDataDocument(bankAccount,"bank-account", uuid);
    }
}