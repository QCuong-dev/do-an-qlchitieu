package com.example.qlchitieu.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qlchitieu.model.Transaction;
import com.example.qlchitieu.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionDAO extends BaseDAO<Transaction> {
    public TransactionDAO(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected String getTableName() {
        return "Transaction";
    }

    @Override
    protected ContentValues getContentValues(Transaction transaction) {
        ContentValues values = new ContentValues();
        values.put("uuid", transaction.getUuid());
//        values.put("name",transaction.getName());
//        values.put("age",transaction.getAge());
//        values.put("email",transaction.getEmail());
//        values.put("username",transaction.getUsername());
//        values.put("password",transaction.getPassword());
        values.put("created_at", getCurrentDate());
        return values;
    }

    @Override
    protected Transaction parseCursor(Cursor cursor) {
        Transaction user = new Transaction();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        user.setUuid(cursor.getString(cursor.getColumnIndexOrThrow("uuid")));
//        user.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
//        user.setAge(cursor.getString(cursor.getColumnIndexOrThrow("age")));
//        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
//        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
//        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
//        user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("created_at")));
        return user;
    }

    public String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}