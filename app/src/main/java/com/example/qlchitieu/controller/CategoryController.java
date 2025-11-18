package com.example.qlchitieu.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.qlchitieu.data.db.DBHelper;
import com.example.qlchitieu.data.db.dao.CategoryDAO;
import com.example.qlchitieu.data.db.firebase.BaseFirebase;
import com.example.qlchitieu.data.db.firebase.CategoryFirebase;
import com.example.qlchitieu.helpers.SharedPrefHelper;
import com.example.qlchitieu.model.Category;

import java.util.UUID;

public class CategoryController extends BaseController<Category,CategoryDAO, CategoryFirebase> {
    private SharedPrefHelper sharedPrefHelper;
    public CategoryController(Context context){
        super(new CategoryDAO(DBHelper.getInstance(context).getWritableDatabase()),new CategoryFirebase());
        this.context = context;
        sharedPrefHelper = new SharedPrefHelper(context);
    }

    public void saveCategory(String name, BaseFirebase.DataCallback<String> callback){
        int idUser = sharedPrefHelper.getInt("idUser",0);
        if(idUser == 0){
            callback.onFailure("Không tìm thấy User đăng nhập");
            return;
        }

        if(dao.exist(idUser,"name",name)){
            callback.onFailure("Tên danh mục đã tồn tại");
            return;
        }

        String uuid = UUID.randomUUID().toString();

        Category category = new Category();
        category.setUuid(uuid);
        category.setUser_id(idUser);
        category.setName(name);

        long result = dao.insert(category);

        if(result <= 0){
            callback.onFailure("Lỗi khi lưu danh mục");
            return;
        }

        category.setId((int) result);

        // Firestore
        fBase.addDocument(uuid, category, new BaseFirebase.DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess("Thêm danh mục thành công");
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure(message);
            }
        });
    }

}
