package com.example.qlchitieu.model;

public class Category {
    private int id;
    private String uuid;
    private String user_uid;
    private String name;
    private int icon;
    private int is_synced;

    public Category() {
    }

    public Category(String user_uid, String name) {
        this.user_uid = user_uid;
        this.name = name;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getIs_synced() {
        return is_synced;
    }

    public void setIs_synced(int is_synced) {
        this.is_synced = is_synced;
    }
}
