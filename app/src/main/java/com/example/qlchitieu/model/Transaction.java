package com.example.qlchitieu.model;

public class Transaction {
    private int id,iconResId;
    private String uuid,note,date,type,created_at,wallet_uid,category_uid;
    private float amount;
    private int is_synced;

    // JOIN TABLE Category
    private String category_name;
    // Some other
    private String dayOfWeek;

    public Transaction(){}

    public Transaction(String wallet_uid, String category_uid, String uuid, String note, String date, String type, float amount) {
        this.wallet_uid = wallet_uid;
        this.category_uid = category_uid;
        this.uuid = uuid;
        this.note = note;
        this.date = date;
        this.type = type;
        this.amount = amount;
    }

    public Transaction(String dayOfWeek, String date, String category_name, String note,float amount, String type, int iconResId) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.category_name = category_name;
        this.note = note;
        this.amount = amount;
        this.type = type;
        this.iconResId = iconResId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWallet_uid() {
        return wallet_uid;
    }

    public void setWallet_uid(String wallet_uid) {
        this.wallet_uid = wallet_uid;
    }

    public String getCategory_uid() {
        return category_uid;
    }

    public void setCategory_uid(String category_uid) {
        this.category_uid = category_uid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getIs_synced() {
        return is_synced;
    }

    public void setIs_synced(int is_synced) {
        this.is_synced = is_synced;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
