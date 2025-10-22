package com.example.qlchitieu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.ar.imp.view.View;

public class SignupActivity extends AppCompatActivity {
    TextView tvBackToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Ánh xạ TextView từ layout
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        // Thiết lập sự kiện click cho TextView
        tvBackToLogin.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                // Tạo một Intent để quay lại SigninActivity
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                // Cờ này sẽ xóa các activity phía trên SigninActivity khỏi stack,
                // đảm bảo người dùng không tạo ra một chuỗi activity vô tận
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish(); // Đóng SignupActivity hiện tại
            }
        });
    }
}