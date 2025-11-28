package com.example.qlchitieu.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qlchitieu.model.Transaction;
import com.example.qlchitieu.model.User;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        values.put("category_id",transaction.getCategory_id());
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

    protected Transaction parseCursorJoinCategory(Cursor cursor) {
        Transaction transaction = new Transaction();
        transaction.setId(cursor.getInt(cursor.getColumnIndexOrThrow("transaction_id")));
        transaction.setUuid(cursor.getString(cursor.getColumnIndexOrThrow("uuid")));
        transaction.setWallet_id(cursor.getInt(cursor.getColumnIndexOrThrow("wallet_id")));
        transaction.setCategory_id(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
        transaction.setAmount(cursor.getFloat(cursor.getColumnIndexOrThrow("amount")));
        transaction.setNote(cursor.getString(cursor.getColumnIndexOrThrow("note")));
        transaction.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
        transaction.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
        transaction.setCreated_at(cursor.getString(cursor.getColumnIndexOrThrow("created_at")));
        transaction.setCategory_name(cursor.getString(cursor.getColumnIndexOrThrow("category_name")));
        return transaction;
    }

    public String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public List<Transaction> getListByDate(String date, int idWallet){
        Cursor cursor = db.rawQuery("SELECT T.id AS transaction_id, T.uuid, T.wallet_id, T.category_id, T.amount, T.note, T.date, T.type, T.created_at, C.name AS category_name FROM `Transaction` T JOIN `Category` C ON T.category_id = C.id WHERE SUBSTR(T.date, 1, 10) = ? AND wallet_id = ?", new String[]{date,String.valueOf(idWallet)});
        List<Transaction> list = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                list.add(parseCursorJoinCategory(cursor));
            }while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public List<Transaction> getListByIdWallet(int idWallet) {
        Cursor cursor = db.query(getTableName(),null,"wallet_id = ?",new String[]{String.valueOf(idWallet)},null,null,null,null);
        List<Transaction> list = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                list.add(parseCursor(cursor));
            }while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    public List<Transaction> getListByMonth(String date,boolean isGroup){
        String monthString = date.split("-")[1];
        String yearString = date.split("-")[0];
        String groupBy = isGroup ? "GROUP BY category_name":"";

        Cursor cursor = db.rawQuery("SELECT T.id AS transaction_id, T.uuid, T.wallet_id, T.category_id, T.amount, T.note, T.date, T.type, T.created_at, C.name AS category_name " +
                "FROM `Transaction` T " +
                "JOIN `Category` C ON T.category_id = C.id " +
                "WHERE strftime('%Y-%m', SUBSTR(T.date,1,10)) = ? " +
                groupBy, new String[]{yearString + "-" + monthString});
        List<Transaction> list = new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                list.add(parseCursorJoinCategory(cursor));
            }while (cursor.moveToNext());
        }

//        cursor.close();
        return list;
    }
}