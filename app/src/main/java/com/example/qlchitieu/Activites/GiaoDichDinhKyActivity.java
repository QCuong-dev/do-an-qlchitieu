package com.example.qlchitieu.Activites;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlchitieu.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GiaoDichDinhKyActivity extends AppCompatActivity {

    MaterialToolbar toolbarGiaoDich;
    RecyclerView rvGiaoDich;
    FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_giao_dich_dinh_ky);


        toolbarGiaoDich = findViewById(R.id.toolbarGiaoDich);
        rvGiaoDich = findViewById(R.id.rvGiaoDich);
        fabAdd = findViewById(R.id.fabAdd);

        // Sự kiện quay lại trên Toolbar
        toolbarGiaoDich.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvGiaoDich.setLayoutManager(new LinearLayoutManager(this));
        // TODO: Gán Adapter cho rvGiaoDich

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GiaoDichDinhKyActivity.this, "Mở màn hình Thêm mới", Toast.LENGTH_SHORT).show();
            }
        });
    }
}