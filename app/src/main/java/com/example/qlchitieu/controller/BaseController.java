package com.example.qlchitieu.controller;

import android.content.Context;

import com.example.qlchitieu.data.db.dao.BaseDAO;
import com.example.qlchitieu.data.db.firebase.BaseFirebase;
import com.example.qlchitieu.helpers.Helpers;
import com.example.qlchitieu.helpers.SharedPrefHelper;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public abstract class BaseController<T,DAO extends BaseDAO<T>, FBase extends BaseFirebase<T>> {
    protected DAO dao;
    protected FBase fBase;
    protected FirebaseFirestore firebaseDB;
    protected SharedPrefHelper sharedPrefHelper;
    protected Helpers helper;

    public BaseController(Context context, DAO dao){
        this.dao = dao;
        this.firebaseDB = FirebaseFirestore.getInstance();
        this.sharedPrefHelper = new SharedPrefHelper(context);
        this.helper = new Helpers(context);
    }

    public BaseController(Context context, DAO dao, FBase fBase){
        this.dao = dao;
        this.firebaseDB = FirebaseFirestore.getInstance();
        this.fBase = fBase;
        this.sharedPrefHelper = new SharedPrefHelper(context);
        this.helper = new Helpers(context);
    }

    public long insert(T entity){
        return dao.insert(entity);
    }
    public int update(T entity,String whereClause,String[] whereArgs){
        return dao.update(entity,whereClause,whereArgs);
    }
    public int delete(String whereClause,String[] whereArgs){
        return dao.delete(whereClause,whereArgs);
    }
    public List<T> getAll(){
        return dao.getAll();
    }
    public T getById(int id){
        return dao.getById(id);
    }

    public void close(){

    }

    public boolean exist(String column,String field){
        return dao.exist(column,field);
    }
}
