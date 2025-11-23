package com.example.qlchitieu.data.db.firebase;

import com.example.qlchitieu.model.Budget;
import com.example.qlchitieu.model.Transaction;

public class BudgetFirebase extends BaseFirebase<Budget>{
    public BudgetFirebase(){
        super();
    }

    protected String getCollectionName(){
        return "budget";
    }
}
