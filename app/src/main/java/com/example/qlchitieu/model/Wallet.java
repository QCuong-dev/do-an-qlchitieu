package com.example.qlchitieu.model;

import androidx.annotation.NonNull;

public class Wallet {
    private int id,balance,user_id;
    private String uuid,wallet_name,currency;

    public Wallet() {
    }

    public Wallet(int balance, String uuid, int user_id, String wallet_name, String currency) {
        this.balance = balance;
        this.uuid = uuid;
        this.user_id = user_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    @NonNull
    @Override
    public String toString() {
        return "Wallet: " +
                "id=" + id +
                ", balance=" + balance +
                ", user_id=" + user_id +
                ", uuid='" + uuid + '\'' +
                ", wallet_name='" + wallet_name + '\'' +
                ", currency='" + currency + '\'';
    }
}
