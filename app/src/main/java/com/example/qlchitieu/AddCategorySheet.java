package com.example.qlchitieu;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.LinearLayout;


import com.example.qlchitieu.controller.CategoryController;
import com.example.qlchitieu.controller.UserController;
import com.example.qlchitieu.data.db.firebase.BaseFirebase;
import com.example.qlchitieu.databinding.ActivityAddChitieuBinding;
import com.example.qlchitieu.databinding.AddCategorySheetBinding;
import com.example.qlchitieu.databinding.ItemCategoryBinding;
import com.example.qlchitieu.helpers.SharedPrefHelper;
import com.example.qlchitieu.model.Category;
import com.example.qlchitieu.model.User;

import java.util.List;

public class AddCategorySheet extends AppCompatActivity {

    private AddCategorySheetBinding binding;
    private CategoryController categoryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddCategorySheetBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        categoryController = new CategoryController(this);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.etCategoryName.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(AddCategorySheet.this, "Vui lòng nhập loại chi tiêu", Toast.LENGTH_SHORT).show();
                    return;
                }

                categoryController.saveCategory(name, new BaseFirebase.DataCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        Toast.makeText(AddCategorySheet.this, data, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(AddCategorySheet.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}