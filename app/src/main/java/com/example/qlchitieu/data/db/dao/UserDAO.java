package com.example.qlchitieu.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.qlchitieu.model.User;


public class UserDAO extends BaseDAO<User> {
    public UserDAO(SQLiteDatabase db){
        super(db);
    }

    @Override
    protected String getTableName() {
        return "User";
    }

    @Override
    protected ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put("name",user.getName());
        values.put("age",user.getAge());
        values.put("email",user.getEmail());
        values.put("username",user.getUsername());
        values.put("password",user.getPassword());
        return values;
    }

    @Override
    protected User parseCursor(Cursor cursor) {
        User user = new User();
        user.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        user.setAge(cursor.getString(cursor.getColumnIndexOrThrow("age")));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
        return user;
    }
}
