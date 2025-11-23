package com.example.qlchitieu.data.db.firebase;

import com.example.qlchitieu.model.Category;
import com.example.qlchitieu.model.Transaction;

public class TransactionFirebase extends BaseFirebase<Transaction>{
    public TransactionFirebase(){
        super();
    }

    protected String getCollectionName(){
        return "transaction";
    }
}
