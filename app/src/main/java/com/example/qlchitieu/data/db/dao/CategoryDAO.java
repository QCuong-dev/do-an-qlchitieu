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
        values.put("user_id",category.getUser_id());
        values.put("is_synced",category.getIs_synced());
        return values;
    }

    @Override
    protected Category parseCursor(Cursor cursor) {
        Category category = new Category();
        category.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        category.setUuid(cursor.getString(cursor.getColumnIndexOrThrow("uuid")));
        category.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        category.setUser_id(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
        category.setIs_synced(cursor.getInt(cursor.getColumnIndexOrThrow("is_synced")));
        return category;
    }

    // Check category exist
    public boolean exist(int idUser, String column,String value){
        String selection = column + " = ? AND user_id = ?";
        String[] selectionArgs = { value,String.valueOf(idUser) };

        Cursor cursor = db.query(getTableName(),null,selection,selectionArgs,null,null,null);
        if(cursor.moveToFirst()){
            Log.d("DONVAU::CATEGORY_EXISTS","Exist");
            cursor.close();
            return true;
        }

        Log.d("DONVAU::CATEGORY_EXISTS","No Exist");
        cursor.close();
        return false;
    }
}
