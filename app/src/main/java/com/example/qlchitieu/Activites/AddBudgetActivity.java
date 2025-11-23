package com.example.qlchitieu.Activites;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlchitieu.R;
import com.example.qlchitieu.databinding.ActivityAccountBinding;
import com.example.qlchitieu.databinding.ActivityAddEditBudgetBinding;

import java.util.Calendar;
import java.util.Locale;

public class AddBudgetActivity extends AppCompatActivity {
    private ActivityAddEditBudgetBinding binding;
    private final Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditBudgetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar();

        // Thiết lập lắng nghe sự kiện cho LinearLayout chọn ngày bắt đầu
        binding.llStartDate.setOnClickListener(v -> showDatePickerDialog(binding.tvStartDate));

        // Thiết lập lắng nghe sự kiện cho LinearLayout chọn ngày kết thúc
        binding.llEndDate.setOnClickListener(v -> showDatePickerDialog(binding.tvEndDate));
    }


    private void showDatePickerDialog(TextView targetTextView) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    // Định dạng ngày: ngày/tháng/năm
                    String date = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month1 + 1, year1);
                    targetTextView.setText(date);
                },
                year, month, day
        );
        datePickerDialog.show();
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
}