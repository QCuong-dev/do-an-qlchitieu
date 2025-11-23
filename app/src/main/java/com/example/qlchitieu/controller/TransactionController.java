package com.example.qlchitieu.controller;

import android.content.Context;

import com.example.qlchitieu.data.db.DBHelper;
import com.example.qlchitieu.data.db.dao.TransactionDAO;
import com.example.qlchitieu.data.db.dao.UserDAO;
import com.example.qlchitieu.data.db.firebase.TransactionFirebase;
import com.example.qlchitieu.data.db.firebase.UserFirebase;
import com.example.qlchitieu.model.Transaction;

public class TransactionController extends BaseController<Transaction, TransactionDAO, TransactionFirebase> {
    public TransactionController(Context context) {
        super(new TransactionDAO(DBHelper.getInstance(context).getWritableDatabase()), new TransactionFirebase());
    }
}
