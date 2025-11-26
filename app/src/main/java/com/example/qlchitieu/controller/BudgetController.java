package com.example.qlchitieu.controller;

import android.content.Context;

import com.example.qlchitieu.data.db.DBHelper;
import com.example.qlchitieu.data.db.dao.BudgetDAO;
import com.example.qlchitieu.data.db.dao.TransactionDAO;
import com.example.qlchitieu.data.db.firebase.BudgetFirebase;
import com.example.qlchitieu.data.db.firebase.TransactionFirebase;
import com.example.qlchitieu.model.Budget;
import com.example.qlchitieu.model.Transaction;

public class BudgetController extends BaseController<Budget, BudgetDAO, BudgetFirebase> {
    public BudgetController(Context context) {
        super(context,new BudgetDAO(DBHelper.getInstance(context).getWritableDatabase()), new BudgetFirebase());
    }
}
