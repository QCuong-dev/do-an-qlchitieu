package com.example.qlchitieu.Activites;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlchitieu.Bill;
import com.example.qlchitieu.R;
import com.example.qlchitieu.Adapter.BillAdapter;
import com.example.qlchitieu.controller.TransactionController;
import com.example.qlchitieu.model.Transaction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OverviewWalletActivity extends AppCompatActivity {

    private RecyclerView rvBills;
    private TextView tvTotalIncome, tvTotalExpense;
    private ImageView btnSearch, btnChevronLeft;
    private EditText edtSearch;

    private BillAdapter adapter;
    private List<Transaction> originalList; // Danh sách gốc (đầy đủ)
    private List<Transaction> displayList;  // Danh sách đang hiển thị (có thể đã bị lọc)
    private TransactionController transactionController;

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

        transactionController = new TransactionController(this);

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
        List<Transaction> list = transactionController.getAllHaveCategory();
        for(Transaction t : list){
            originalList.add(new Transaction("",t.getDate(),t.getCategory_name(),t.getNote(),t.getAmount(),t.getType(),R.drawable.ic_bill));
        }

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
        List<Transaction> filteredList = new ArrayList<>();

        for (Transaction item : originalList) {
            // Tìm kiếm theo Tên hoặc Ghi chú (không phân biệt hoa thường)
            if (item.getCategory_name().toLowerCase().contains(text.toLowerCase()) ||
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
    private void calculateTotals(List<Transaction> bills) {
        long totalIncome = 0;
        long totalExpense = 0;

        for (Transaction bill : bills) {
            if (bill.getType().equals("income")) {
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