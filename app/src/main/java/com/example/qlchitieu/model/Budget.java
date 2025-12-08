package com.example.qlchitieu.model;

public class Budget {
    private int id,amount_limit;
    private String uuid,start_date,end_date,user_uid,category_uid;
    private int is_synced;

    public Budget() {
    }

    public Budget(String user_uid, String category_uid, int amount_limit, String uuid, String start_date, String end_date) {
        this.user_uid = user_uid;
        this.category_uid = category_uid;
        this.amount_limit = amount_limit;
        this.uuid = uuid;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getCategory_uid() {
        return category_uid;
    }

    public void setCategory_uid(String category_uid) {
        this.category_uid = category_uid;
    }

    public int getAmount_limit() {
        return amount_limit;
    }

    public void setAmount_limit(int amount_limit) {
        this.amount_limit = amount_limit;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getIs_synced() {
        return is_synced;
    }

    public void setIs_synced(int is_synced) {
        this.is_synced = is_synced;
    }
}
