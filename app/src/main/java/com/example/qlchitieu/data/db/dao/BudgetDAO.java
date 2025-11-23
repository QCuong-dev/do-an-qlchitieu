package com.example.qlchitieu.data.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.qlchitieu.model.Budget;
import com.example.qlchitieu.model.Wallet;

public class BudgetDAO extends BaseDAO<Budget> {
    public BudgetDAO(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected String getTableName() {
        return "Budget";
    }

    @Override
    protected ContentValues getContentValues(Budget budget) {
        ContentValues values = new ContentValues();
        values.put("uuid", budget.getUuid());
        values.put("user_id",budget.getUser_id());
        values.put("category_id",budget.getCategory_id());
        values.put("amount_limit",budget.getAmount_limit());
        values.put("start_date",budget.getStart_date());
        values.put("end_date",budget.getEnd_date());
        return values;
    }

    @Override
    protected Budget parseCursor(Cursor cursor) {
        Budget budget = new Budget();
        budget.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        budget.setUuid(cursor.getString(cursor.getColumnIndexOrThrow("uuid")));
        budget.setUser_id(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
        budget.setCategory_id(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
        budget.setAmount_limit(cursor.getInt(cursor.getColumnIndexOrThrow("amount_limit")));
        budget.setStart_date(cursor.getString(cursor.getColumnIndexOrThrow("start_date")));
        budget.setEnd_date(cursor.getString(cursor.getColumnIndexOrThrow("end_date")));
        return budget;
    }
}