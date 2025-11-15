package com.example.qlchitieu.Activites;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlchitieu.R;
import com.example.qlchitieu.databinding.ActivityAddChitieuBinding;

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