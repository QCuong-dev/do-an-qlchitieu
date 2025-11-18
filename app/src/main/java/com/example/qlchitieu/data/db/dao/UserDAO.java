package com.example.qlchitieu.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.qlchitieu.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class UserDAO extends BaseDAO<User> {
    public UserDAO(SQLiteDatabase db){
        super(db);
    }

    public User getUserByEmail(String email){
        List<User> users = getAll();
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    @Override
    protected String getTableName() {
        return "User";
    }

    @Override
    protected ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put("uuid",user.getUuid());
        values.put("name",user.getName());
        values.put("age",user.getAge());
        values.put("email",user.getEmail());
        values.put("username",user.getUsername());
        values.put("password",user.getPassword());
        values.put("created_at",getCurrentDate());
        return values;
    }

    @Override
    protected User parseCursor(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        user.setUuid(cursor.getString(cursor.getColumnIndexOrThrow("uuid")));
        user.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        user.setAge(cursor.getString(cursor.getColumnIndexOrThrow("age")));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
        user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("created_at")));
        return user;
    }

    public String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}
