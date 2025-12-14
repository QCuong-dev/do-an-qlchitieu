package com.example.qlchitieu.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "QLCT.db";
    public static final int DB_VERSION = 3;
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

        // ===== TẠO BẢNG USER =====
        db.execSQL("CREATE TABLE IF NOT EXISTS User (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uuid TEXT DEFAULT NULL UNIQUE, " +
                "name TEXT NOT NULL, " +
                "age TEXT NOT NULL, " +
                "email TEXT NOT NULL UNIQUE, " +
                "username TEXT, " +
                "password TEXT, " +
                "created_at TEXT DEFAULT CURRENT_DATE, " +
                "is_synced INTEGER DEFAULT 0" +
                ");");

        // ===== TẠO BẢNG WALLET =====
        db.execSQL("CREATE TABLE IF NOT EXISTS Wallet (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uuid TEXT DEFAULT NULL UNIQUE, " +
                "user_uid TEXT NOT NULL, " +
                "wallet_name TEXT NOT NULL, " +
                "balance REAL NOT NULL DEFAULT 0, " +
                "currency TEXT NOT NULL DEFAULT 'VND', " +
                "FOREIGN KEY(user_uid) REFERENCES User(uuid) ON DELETE CASCADE" +
                ");");

        // ===== TẠO BẢNG CATEGORY =====
        db.execSQL("CREATE TABLE IF NOT EXISTS Category (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uuid TEXT DEFAULT NULL UNIQUE, " +
                "user_uid TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "icon INTEGER, " +
                "FOREIGN KEY(user_uid) REFERENCES User(uuid) ON DELETE CASCADE" +
                ");");

        // ===== TẠO BẢNG TRANSACTION =====
        db.execSQL("CREATE TABLE IF NOT EXISTS `Transaction` (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uuid TEXT DEFAULT NULL UNIQUE, " +
                "wallet_uid TEXT NOT NULL, " +
                "category_uid TEXT NOT NULL, " +
                "amount REAL NOT NULL, " +
                "note TEXT, " +
                "date TEXT NOT NULL, " +
                "type TEXT CHECK (type IN ('income', 'expense')) NOT NULL, " +
                "created_at TEXT DEFAULT CURRENT_DATE, " +
                "FOREIGN KEY(wallet_uid) REFERENCES Wallet(uuid) ON DELETE CASCADE, " +
                "FOREIGN KEY(category_uid) REFERENCES Category(uuid) ON DELETE CASCADE" +
                ");");

        // ===== TẠO BẢNG BUDGET =====
        db.execSQL("CREATE TABLE IF NOT EXISTS Budget (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uuid TEXT DEFAULT NULL UNIQUE, " +
                "user_uid TEXT NOT NULL, " +
                "category_uid TEXT NOT NULL, " +
                "amount_limit REAL NOT NULL, " +
                "start_date TEXT NOT NULL, " +
                "end_date TEXT NOT NULL, " +
                "FOREIGN KEY(user_uid) REFERENCES User(uuid) ON DELETE CASCADE, " +
                "FOREIGN KEY(category_uid) REFERENCES Category(uuid) ON DELETE CASCADE" +
                ");");

        // ===== TẠO BẢNG ATTACHMENT =====
        db.execSQL("CREATE TABLE IF NOT EXISTS Attachment (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uuid TEXT DEFAULT NULL UNIQUE, " +
                "transaction_uid TEXT NOT NULL, " +
                "file_path TEXT NOT NULL, " +
                "created_at TEXT DEFAULT CURRENT_DATE, " +
                "FOREIGN KEY(transaction_uid) REFERENCES `Transaction`(uuid) ON DELETE CASCADE" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Attachment;");
        db.execSQL("DROP TABLE IF EXISTS Budget;");
        db.execSQL("DROP TABLE IF EXISTS `Transaction`;");
        db.execSQL("DROP TABLE IF EXISTS Category;");
        db.execSQL("DROP TABLE IF EXISTS Wallet;");
        db.execSQL("DROP TABLE IF EXISTS User;");
        onCreate(db);
    }
}
