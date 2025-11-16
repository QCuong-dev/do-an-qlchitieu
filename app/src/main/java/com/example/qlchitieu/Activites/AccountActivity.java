package com.example.qlchitieu.Activites;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlchitieu.R;
import com.example.qlchitieu.databinding.ActivityAccountBinding;
import com.google.android.material.textfield.TextInputEditText;

public class AccountActivity extends AppCompatActivity {

    private  ActivityAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo binding
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        // Set content view bằng getRoot() của binding
        setContentView(binding.getRoot());

        // 1. Cài đặt Toolbar
        setupToolbar();

        // 2. Tải dữ liệu người dùng (giả định)
        loadUserData();

        // 3. Xử lý sự kiện click
        binding.btnSave.setOnClickListener(v -> {
            saveChanges();
        });

    }
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    private void loadUserData() {
        // (Đây là nơi bạn tải dữ liệu từ API/Database)
        // Dữ liệu giả định:
        binding.etFullName.setText("Nguyễn Văn A");
        binding.etEmail.setText("nguyenvana@example.com");

        // Dùng Glide hoặc Picasso để tải ảnh đại diện vào binding.ivAvatar
        // Ví dụ:
        // Glide.with(this)
        //     .load(user.getAvatarUrl())
        //     .into(binding.ivAvatar);
    }

    private void saveChanges() {
        String fullName = binding.etFullName.getText().toString().trim();

        if (fullName.isEmpty()) {
            binding.tilFullName.setError("Họ tên không được để trống");
            return;
        } else {
            binding.tilFullName.setError(null);
        }

        // ... Logic gọi API/Firebase để lưu thay đổi ...

        Toast.makeText(this, "Lưu thay đổi thành công!", Toast.LENGTH_SHORT).show();
        finish(); // Đóng Activity sau khi lưu
    }
}