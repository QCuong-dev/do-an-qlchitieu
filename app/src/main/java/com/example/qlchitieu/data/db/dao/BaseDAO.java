package com.example.qlchitieu.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T> {
    protected SQLiteDatabase db;

    public BaseDAO(SQLiteDatabase db){
        this.db = db;
    }

    // Insert
    public long insert(T entity){
        ContentValues values = getContentValues(entity);
        return db.insert(getTableName(),null,values);
    }
    // Update
    public int update(T entity,String whereClause,String[] whereArgs){
        ContentValues values = getContentValues(entity);
        return db.update(getTableName(),values,whereClause,whereArgs);
    }
    // Delete
    public int delete(String whereClause,String[] whereArgs){
        return db.delete(getTableName(),whereClause,whereArgs);
    }
    // Get all
    public List<T> getAll(){
        List<T> list = new ArrayList<>();
        Cursor cursor = db.query(getTableName(),null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                list.add(parseCursor(cursor));
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    // Get by id
    public T getById(int id){
        Cursor cursor = db.query(getTableName(),null,"id = ?",new String[]{String.valueOf(id)},null,null,null);
        if(cursor.moveToFirst()){
            T obj = parseCursor(cursor);
            cursor.close();
            return obj;
        }

        cursor.close();
        return null;
    }

    // Hàm bắt buộc
    protected abstract String getTableName();
    protected abstract ContentValues getContentValues(T entity);
    protected abstract T parseCursor(Cursor cursor);
}
