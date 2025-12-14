package com.example.qlchitieu.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qlchitieu.model.Category;
import com.example.qlchitieu.model.User;

public class CategoryDAO extends BaseDAO<Category> {
    public CategoryDAO(SQLiteDatabase db){
        super(db);
    }

    @Override
    protected String getTableName() {
        return "Category";
    }

    @Override
    protected ContentValues getContentValues(Category category) {
        ContentValues values = new ContentValues();
        values.put("uuid",category.getUuid());
        values.put("name",category.getName());
        values.put("user_uid",category.getUser_uid());
        return values;
    }

    @Override
    protected Category parseCursor(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        category.setUuid(cursor.getString(cursor.getColumnIndexOrThrow("uuid")));
        category.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        category.setUser_uid(cursor.getString(cursor.getColumnIndexOrThrow("user_uid")));
        return category;
    }
}
