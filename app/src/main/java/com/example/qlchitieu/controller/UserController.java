package com.example.qlchitieu.controller;

import android.content.Context;

import com.example.qlchitieu.data.db.DBHelper;
import com.example.qlchitieu.data.db.dao.UserDAO;
import com.example.qlchitieu.model.User;

import java.util.List;

public class UserController extends BaseController<User> {
    public UserController(Context context){
        super(new UserDAO(DBHelper.getInstance(context).getWritableDatabase()));
    }

    // Handle Login
    public User login(String username,String password){
        List<User> users = dao.getAll();
        for(User u : users){
            if(u.getUsername().equals(username) && u.getPassword().equals(password)) return u;
        }
        return null;
    }

    // Check email exists
    public boolean isEmailExists(String email){
        List<User> users = dao.getAll();
        for(User u : users){
            if(u.getEmail().equals(email)) return true;
        }
        return false;
    }
}
