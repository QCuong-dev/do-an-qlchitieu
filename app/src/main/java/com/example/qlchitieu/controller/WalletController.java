package com.example.qlchitieu.controller;

import android.content.Context;

import com.example.qlchitieu.data.db.DBHelper;
import com.example.qlchitieu.data.db.dao.WalletDAO;
import com.example.qlchitieu.data.db.firebase.WalletFirebase;
import com.example.qlchitieu.model.Wallet;

public class WalletController extends BaseController<Wallet, WalletDAO, WalletFirebase> {
    public WalletController(Context context) {
        super(new WalletDAO(DBHelper.getInstance(context).getWritableDatabase()), new WalletFirebase());
    }
}
