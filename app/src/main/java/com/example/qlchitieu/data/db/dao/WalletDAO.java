package com.example.qlchitieu.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qlchitieu.model.Transaction;
import com.example.qlchitieu.model.Wallet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WalletDAO extends BaseDAO<Wallet> {
    public WalletDAO(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected String getTableName() {
        return "Wallet";
    }

    @Override
    protected ContentValues getContentValues(Wallet wallet) {
        ContentValues values = new ContentValues();
        values.put("uuid", wallet.getUuid());
        values.put("user_uid",wallet.getUser_uid());
        values.put("wallet_name",wallet.getWallet_name());
        values.put("balance",wallet.getBalance());
        values.put("currency",wallet.getCurrency());
        return values;
    }

    @Override
    protected Wallet parseCursor(Cursor cursor) {
        Wallet wallet = new Wallet();
        wallet.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        wallet.setUuid(cursor.getString(cursor.getColumnIndexOrThrow("uuid")));
        wallet.setUser_uid(cursor.getString(cursor.getColumnIndexOrThrow("user_uid")));
        wallet.setWallet_name(cursor.getString(cursor.getColumnIndexOrThrow("wallet_name")));
        wallet.setBalance(cursor.getInt(cursor.getColumnIndexOrThrow("balance")));
        wallet.setCurrency(cursor.getString(cursor.getColumnIndexOrThrow("currency")));
        return wallet;
    }

    public Wallet getWalletByUserId(String uidUser){
        Cursor cursor = db.query(getTableName(),null,"user_uid = ?",new String[]{uidUser},null,null,null,null);
        if(cursor.moveToFirst()){
            return parseCursor(cursor);
        }

        return null;
    }
}