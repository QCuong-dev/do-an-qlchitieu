package com.example.qlchitieu.data.db.firebase;

import com.example.qlchitieu.model.Transaction;
import com.example.qlchitieu.model.Wallet;

public class WalletFirebase extends BaseFirebase<Wallet>{
    public WalletFirebase(){
        super();
    }

    protected String getCollectionName(){
        return "wallet";
    }
}
