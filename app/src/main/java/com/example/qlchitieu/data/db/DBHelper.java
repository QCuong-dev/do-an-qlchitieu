package com.example.qlchitieu.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "QLCT.db";
    public static final int DB_VERSION = 1;
    private static DBHelper instance;
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context){
        if(instance == null) instance = new DBHelper(context.getApplicationContext());
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // Bật hỗ trợ Foreign Key
        db.execSQL("PRAGMA foreign_keys = ON;");
        // Tạo bảng
        db.execSQL("CREATE TABLE IF NOT EXISTS User (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    name TEXT NOT NULL," +
                "    age TEXT NOT NULL," +
                "    email TEXT NOT NULL," +
                "    username TEXT NOT NULL UNIQUE," +
                "    password TEXT NOT NULL," +
                "    created_at TEXT NOT NULL" +
                ");"); // USER
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }
}
