package com.example.qlchitieu.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlchitieu.R;
import com.example.qlchitieu.databinding.ActivityAddChitieuBinding;

public class AddCategorySheetActivity extends AppCompatActivity {


    private Button saveCategory;
    private TextView CategoryNameAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category_sheet);


        init();
        saveCategory.setOnClickListener(this::saveCategory);

    }

    private  void init(){
        CategoryNameAdd = (TextView) findViewById(R.id.etCategoryName);
        saveCategory = (Button) findViewById(R.id.btnSave);

    }
    private void backToAddChitieu(View view){
        finish();
    }


    private void saveCategory(View v){

        String name = CategoryNameAdd.getText().toString();
        Toast.makeText(AddCategorySheetActivity.this, "Thêm mới Category voi ten "+name+" thành công", Toast.LENGTH_SHORT).show();
        CategoryNameAdd.setText("");
    }
}