package com.example.qlchitieu.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlchitieu.AddCategorySheet;
import com.example.qlchitieu.R;
import com.example.qlchitieu.controller.CategoryController;
import com.example.qlchitieu.data.db.firebase.BaseFirebase;
import com.example.qlchitieu.databinding.ActivityAddChitieuBinding;
import com.example.qlchitieu.databinding.AddCategorySheetBinding;
import com.example.qlchitieu.databinding.ItemCategoryBinding;
import com.example.qlchitieu.model.Category;

import java.util.List;

public class AddCategorySheetActivity extends AppCompatActivity {
    private AddCategorySheetBinding binding;
    private CategoryController categoryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AddCategorySheetBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_category_sheet);
        setContentView(binding.getRoot());
        categoryController = new CategoryController(this);

        renderCategory();
        binding.btnSave.setOnClickListener(this::saveCategory);
        binding.ivBack.setOnClickListener(this::backToAddChitieu);

    }
    private void backToAddChitieu(View view){
        finish();
    }
    private void renderCategory(){
        // Remove all views
        binding.llManageList.removeAllViews();

        // Render Category
        List<Category> categoryList = categoryController.getAll();
        for(Category c : categoryList){
            addCategoryToLayout(c);
        }
    }
    private void addCategoryToLayout(Category category) {

        // Lấy layout chứa danh sách
        LinearLayout listCategory = binding.llManageList;

        // Inflate item bằng ViewBinding
        ItemCategoryBinding itemBinding =
                ItemCategoryBinding.inflate(getLayoutInflater(), listCategory, false);

        // Set dữ liệu
        itemBinding.tvCategoryName1.setText(category.getName());

        // Click delete
        itemBinding.ibDelete1.setOnClickListener(v -> {
            categoryController.deleteCategory(category.getId(), new BaseFirebase.DataCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    listCategory.removeView(itemBinding.getRoot());
                    Toast.makeText(AddCategorySheetActivity.this, s, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(AddCategorySheetActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Thêm view vào danh sách
        listCategory.addView(itemBinding.getRoot());
    }

    private void saveCategory(View v){

        String name = binding.etCategoryName.getText().toString();
        if(name.isEmpty()){
            Toast.makeText(this, "Vui lòng nhập loại chi tiêu", Toast.LENGTH_SHORT).show();
            return;
        }

        categoryController.saveCategory(name, new BaseFirebase.DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                renderCategory();
                binding.etCategoryName.setText("");
                Toast.makeText(AddCategorySheetActivity.this, data, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(AddCategorySheetActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}