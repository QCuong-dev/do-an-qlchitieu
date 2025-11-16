package com.example.qlchitieu.Activites;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlchitieu.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class BienDongActivity extends AppCompatActivity {

    ImageView ivBack;
    LineChart lineChartDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bien_dong);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        ivBack = findViewById(R.id.ivBack);
        lineChartDetail = findViewById(R.id.lineChartDetail);

        // Sự kiện quay lại
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Tải dữ liệu mẫu cho biểu đồ
        loadChartData();
    }

    private void loadChartData() {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 40000));
        entries.add(new Entry(1, 80000));
        entries.add(new Entry(2, 60000));
        // ...

        LineDataSet dataSet = new LineDataSet(entries, "Biến động chi tiêu");
        // ...

        LineData lineData = new LineData(dataSet);
        lineChartDetail.setData(lineData);
        lineChartDetail.getDescription().setEnabled(false);
        lineChartDetail.invalidate();
    }
}