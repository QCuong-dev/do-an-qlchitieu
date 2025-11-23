package com.example.qlchitieu.data.db.firebase;

import com.example.qlchitieu.model.Category;
import com.example.qlchitieu.model.User;

public class UserFirebase extends BaseFirebase<User>{
    public UserFirebase(){
        super();
    }

    protected String getCollectionName(){
        return "users";
    }
}