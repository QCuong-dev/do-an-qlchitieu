package com.example.qlchitieu.model;

public class Budget {
    private int id, user_id,category_id,amount_limit;
    private String uuid,start_date,end_date;
    private int is_synced;

    public Budget() {
    }

    public Budget(int user_id, int category_id, int amount_limit, String uuid, String start_date, String end_date) {
        this.user_id = user_id;
        this.category_id = category_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
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
