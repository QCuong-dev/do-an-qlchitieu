package com.example.qlchitieu.Activites;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlchitieu.Bill;
import com.example.qlchitieu.R;
import com.example.qlchitieu.controller.BillAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OverviewWalletActivity extends AppCompatActivity {

    private RecyclerView rvBills;
    private TextView tvTotalIncome, tvTotalExpense;
    private ImageView btnSearch, btnChevronLeft;
    private EditText edtSearch;

    private BillAdapter adapter;
    private List<Bill> originalList; // Danh sách gốc (đầy đủ)
    private List<Bill> displayList;  // Danh sách đang hiển thị (có thể đã bị lọc)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_overview_wallet);

        // Ánh xạ
        rvBills = findViewById(R.id.rvBills);
        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        btnSearch = findViewById(R.id.btnSearch);
        btnChevronLeft = findViewById(R.id.btnChevronLeft);
        edtSearch = findViewById(R.id.edtSearch);

        setupData();

        btnChevronLeft.setOnClickListener(v -> finish());

        // Nút Search: Ẩn/Hiện ô nhập liệu
        btnSearch.setOnClickListener(v -> {
            if (edtSearch.getVisibility() == View.VISIBLE) {
                edtSearch.setVisibility(View.GONE);
                edtSearch.setText(""); // Xóa text khi đóng
            } else {
                edtSearch.setVisibility(View.VISIBLE);
                edtSearch.requestFocus(); // Focus để nhập ngay
            }
        });

        // Xử lý nhập liệu tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupData() {
        originalList = new ArrayList<>();

        // Dữ liệu mẫu (Bạn có thể lấy từ Database sau này)
        originalList.add(new Bill("Thứ 5", "10/10/2024", "Lương tháng 10", "Thu nhập cty", 15000000, true, R.drawable.ic_bill));
        originalList.add(new Bill("Thứ 5", "10/10/2024", "Đi chợ BigC", "Thực phẩm", 600000, false, R.drawable.ic_bill));
        originalList.add(new Bill("Thứ 5", "10/10/2024", "Cà phê Highland", "Gặp bạn", 55000, false, R.drawable.ic_bill));

        originalList.add(new Bill("Thứ 4", "09/10/2024", "Đổ xăng", "Xe máy", 80000, false, R.drawable.ic_bill));
        originalList.add(new Bill("Thứ 4", "09/10/2024", "Bán đồ cũ", "Thanh lý", 250000, true, R.drawable.ic_bill));

        // Ban đầu hiển thị toàn bộ
        displayList = new ArrayList<>(originalList);

        adapter = new BillAdapter(displayList);
        rvBills.setLayoutManager(new LinearLayoutManager(this));
        rvBills.setAdapter(adapter);

        // Tính tổng ban đầu
        calculateTotals(displayList);
    }

    // Hàm lọc danh sách
    private void filterList(String text) {
        List<Bill> filteredList = new ArrayList<>();

        for (Bill item : originalList) {
            // Tìm kiếm theo Tên hoặc Ghi chú (không phân biệt hoa thường)
            if (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getNote().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        // Cập nhật lại RecyclerView và Tổng tiền
        displayList = filteredList;
        adapter.updateData(displayList);
        calculateTotals(displayList);
    }

    // Hàm tính tổng tiền (Dựa trên danh sách đang hiển thị)
    private void calculateTotals(List<Bill> bills) {
        long totalIncome = 0;
        long totalExpense = 0;

        for (Bill bill : bills) {
            if (bill.isIncome()) {
                totalIncome += bill.getAmount();
            } else {
                totalExpense += bill.getAmount();
            }
        }

        DecimalFormat formatter = new DecimalFormat("#,### đ");
        tvTotalIncome.setText(formatter.format(totalIncome));
        tvTotalExpense.setText(formatter.format(totalExpense));
    }
}