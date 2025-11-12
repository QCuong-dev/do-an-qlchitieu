package com.example.qlchitieu;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlchitieu.databinding.ActivityAddChitieuBinding;
import com.example.qlchitieu.databinding.ActivitySigninBinding;

public class AddChitieuActivity extends AppCompatActivity {

    private ActivityAddChitieuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddChitieuBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        binding.chipThemMoi.setOnClickListener(v -> clickAdd());
    }

    private void clickAdd() {
        setContentView(R.layout.add_category_sheet  );
    }
}