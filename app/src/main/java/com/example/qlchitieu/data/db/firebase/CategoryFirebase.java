package com.example.qlchitieu.data.db.firebase;

import com.example.qlchitieu.model.Category;

public class CategoryFirebase extends BaseFirebase<Category>{
    public CategoryFirebase(){
        super();
    }

    protected String getCollectionName(){
        return "categories";
    }
}
