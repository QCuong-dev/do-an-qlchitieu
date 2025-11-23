package com.example.qlchitieu.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlchitieu.R;
import com.example.qlchitieu.databinding.ActivityAddEditWalletBinding;
import com.google.android.material.appbar.MaterialToolbar;

public class AddEditWalletActivity extends AppCompatActivity {

    public static final String EXTRA_WALLET_ID = "com.example.qlchitieu.EXTRA_WALLET_ID";

    // Khai báo biến binding
    private ActivityAddEditWalletBinding binding;

    // Biến để lưu ID nếu đang ở chế độ chỉnh sửa
    private long currentWalletId = -1;

    // Giả sử bạn đang làm việc với User ID tĩnh cho mục đích demo
    private final long CURRENT_USER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEditWalletBinding.inflate(getLayoutInflater());

        // **Bước 2: Thay thế setContentView bằng root view của binding**
        setContentView(binding.getRoot());

        // Lấy Toolbar từ Binding
        MaterialToolbar toolbar = binding.appBarLayout.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spnCurrency.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_WALLET_ID)) {
            // **CHẾ ĐỘ CHỈNH SỬA**
            toolbar.setTitle("Chỉnh sửa Ví"); // Cập nhật tiêu đề Toolbar
            currentWalletId = intent.getLongExtra(EXTRA_WALLET_ID, -1);

            // TODO: 1. Load dữ liệu Ví cũ từ Database dựa trên currentWalletId
            // Ví dụ: Wallet wallet = WalletRepository.getInstance(getApplication()).getWalletById(currentWalletId);
            // Giả định ví được load:
            // Wallet loadedWallet = new Wallet(currentWalletId, CURRENT_USER_ID, "Thẻ Tín Dụng", 5000000.0, "VND", 0);

            // TODO: 2. Hiển thị dữ liệu lên UI
            // Giao diện của bạn không có `etWalletName`, nên bỏ qua
            // binding.etBalance.setText(String.valueOf(loadedWallet.getBalance()));
            // setSpinnerSelection(loadedWallet.getCurrency()); // Hàm phụ trợ để chọn giá trị Spinner

        } else {
            // **CHẾ ĐỘ THÊM MỚI**
            toolbar.setTitle("Thêm Ví Mới"); // Cập nhật tiêu đề Toolbar
        }

        // Đặt lắng nghe sự kiện cho Button
        binding.buttonSaveWallet.setOnClickListener(v -> {
            Toast.makeText(this, "Nút Lưu Ví đã được nhấn!", Toast.LENGTH_SHORT).show();
            // Xử lý logic lưu dữ liệu ở đây
        });

        setupToolbar();
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