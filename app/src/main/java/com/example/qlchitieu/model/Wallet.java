package com.example.qlchitieu.model;

import androidx.annotation.NonNull;

public class Wallet {
    private int id,balance;
    private String uuid,wallet_name,currency,user_uid;
    private int is_synced;

    public Wallet() {
    }

    public Wallet(int balance, String uuid, String user_uid, String wallet_name, String currency) {
        this.balance = balance;
        this.uuid = uuid;
        this.user_uid = user_uid;
        this.wallet_name = wallet_name;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWallet_name() {
        return wallet_name;
    }

    public void setWallet_name(String wallet_name) {
        this.wallet_name = wallet_name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getIs_synced() {
        return is_synced;
    }

    public void setIs_synced(int is_synced) {
        this.is_synced = is_synced;
    }

    @NonNull
    @Override
    public String toString() {
        return "Wallet: " +
                "id=" + id +
                ", balance=" + balance +
                ", user_uid=" + user_uid +
                ", uuid='" + uuid + '\'' +
                ", wallet_name='" + wallet_name + '\'' +
                ", currency='" + currency + '\'';
    }
}
