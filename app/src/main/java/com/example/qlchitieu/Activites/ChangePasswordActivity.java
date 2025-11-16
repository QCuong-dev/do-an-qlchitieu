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
import com.example.qlchitieu.databinding.ActivityChangePasswordBinding;
import com.google.android.material.textfield.TextInputEditText;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        // Set content view bằng getRoot() của binding
        setContentView(binding.getRoot());

        // 1. Cài đặt Toolbar
        setupToolbar();

        // 2. Xử lý sự kiện click cho nút
        binding.btnChangePassword.setOnClickListener(v -> {
            handleChangePassword();
        });
    }

    private void setupToolbar() {
        // Đặt toolbar làm SupportActionBar
        setSupportActionBar(binding.toolbar);

        // Hiển thị nút "Back" (và kiểm tra null)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    // Xử lý sự kiện khi nhấn nút "Back" trên toolbar
    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }

    private void handleChangePassword() {
        // Lấy text từ các trường nhập liệu qua binding
        String currentPass = binding.etCurrentPassword.getText().toString().trim();
        String newPass = binding.etNewPassword.getText().toString().trim();
        String confirmPass = binding.etConfirmPassword.getText().toString().trim();

        // (Thêm logic xác thực của bạn ở đây)
        // Ví dụ:
        if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            binding.tilConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            return;
        } else {
            binding.tilConfirmPassword.setError(null); // Xóa lỗi
        }

        // ... Logic gọi API/Firebase để đổi mật khẩu ...

        Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
        finish(); // Đóng Activity sau khi thành công
    }
}