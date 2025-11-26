package com.example.qlchitieu.Activites;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.qlchitieu.controller.UserController;

import com.example.qlchitieu.R;
import com.example.qlchitieu.data.db.firebase.BaseFirebase;
import com.example.qlchitieu.databinding.ActivityChangePasswordBinding;
import com.google.android.material.textfield.TextInputEditText;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;
    private UserController userController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        // Set content view bằng getRoot() của binding
        setContentView(binding.getRoot());
        userController = new UserController(this);

        // Handle when user the first login Google hide Password
        handleCheckUserLoginGoogle();

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

    private void handleCheckUserLoginGoogle(){
        if(userController.isTheFirstLoginGoogle()){
            binding.tilCurrentPassword.setVisibility(View.GONE);
        }
    }

    private void handleChangePassword() {
        // Lấy text từ các trường nhập liệu qua binding
        String currentPass = binding.etCurrentPassword.getText().toString().trim();
        String newPass = binding.etNewPassword.getText().toString().trim();
        String confirmPass = binding.etConfirmPassword.getText().toString().trim();

        userController.handleChangePassword(currentPass, newPass, confirmPass, new BaseFirebase.DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                Toast.makeText(ChangePasswordActivity.this, data, Toast.LENGTH_SHORT).show();
                finish(); // Đóng Activity sau khi thành công
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}