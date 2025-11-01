package com.example.qlchitieu.controller;

import com.example.qlchitieu.data.db.dao.BaseDAO;

import java.util.List;

public abstract class BaseController<T> {
    protected BaseDAO<T> dao;

    public BaseController(BaseDAO<T> dao){
        this.dao = dao;
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
}
