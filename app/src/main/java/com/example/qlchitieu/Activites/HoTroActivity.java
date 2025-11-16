package com.example.qlchitieu.Activites;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlchitieu.R;

public class HoTroActivity extends AppCompatActivity {

    ImageView ivBack;
    Button btnGuiHoTro, btnGoiTongDai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ho_tro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        // Ánh xạ
        ivBack = findViewById(R.id.ivBack);
        btnGuiHoTro = findViewById(R.id.btnGuiHoTro);
        btnGoiTongDai = findViewById(R.id.btnGoiTongDai);

        // Sự kiện quay lại
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity hiện tại
            }
        });

        // Sự kiện cho các nút
        btnGuiHoTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HoTroActivity.this, "Mở trình gửi email...", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoiTongDai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HoTroActivity.this, "Mở trình gọi điện...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}