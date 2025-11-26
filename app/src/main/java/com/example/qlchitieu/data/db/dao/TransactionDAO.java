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
        return "`Transaction`";
    }

    @Override
    protected ContentValues getContentValues(Transaction transaction) {
        ContentValues values = new ContentValues();
        values.put("uuid", transaction.getUuid());
        values.put("wallet_id",transaction.getWallet_id());
        values.put("category_id",transaction.getWallet_id());
        values.put("amount",transaction.getAmount());
        values.put("note",transaction.getNote());
        values.put("date",transaction.getDate());
        values.put("type", transaction.getType());
        values.put("created_at",getCurrentDate());
        return values;
    }

    @Override
    protected Transaction parseCursor(Cursor cursor) {
        Transaction transaction = new Transaction();
        transaction.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        transaction.setUuid(cursor.getString(cursor.getColumnIndexOrThrow("uuid")));
        transaction.setWallet_id(cursor.getInt(cursor.getColumnIndexOrThrow("wallet_id")));
        transaction.setCategory_id(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
        transaction.setAmount(cursor.getFloat(cursor.getColumnIndexOrThrow("amount")));
        transaction.setNote(cursor.getString(cursor.getColumnIndexOrThrow("note")));
        transaction.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        transaction.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
        transaction.setCreated_at(cursor.getString(cursor.getColumnIndexOrThrow("created_at")));
        return transaction;
    }

    public String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }
}