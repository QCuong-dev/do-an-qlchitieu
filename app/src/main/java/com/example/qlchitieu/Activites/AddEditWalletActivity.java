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

public class AddEditWalletActivity extends AppCompatActivity {

    public static final String EXTRA_WALLET_ID = "com.example.qlchitieu.EXTRA_WALLET_ID";

    private EditText editTextWalletName;
    private EditText editTextBalance;
    private Spinner spinnerCurrency;
    private Button buttonSave;
    // Biến để lưu ID nếu đang ở chế độ chỉnh sửa
    private long currentWalletId = -1;

    // Giả sử bạn đang làm việc với User ID tĩnh cho mục đích demo
    private final long CURRENT_USER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_wallet);
        // Ánh xạ View
        editTextBalance = findViewById(R.id.edit_text_balance);
        spinnerCurrency = findViewById(R.id.spinner_currency);
        buttonSave = findViewById(R.id.button_save_wallet);

        // Thiết lập Spinner (Currency)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);

        // Xử lý Chế độ Thêm hay Chỉnh sửa
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_WALLET_ID)) {
            // **CHẾ ĐỘ CHỈNH SỬA**
            setTitle("Chỉnh sửa Ví");
            currentWalletId = intent.getLongExtra(EXTRA_WALLET_ID, -1);

            // TODO: 1. Load dữ liệu Ví cũ từ Database dựa trên currentWalletId
            // Ví dụ: Wallet wallet = WalletRepository.getInstance(getApplication()).getWalletById(currentWalletId);
            // Giả định ví được load:
            // Wallet loadedWallet = new Wallet(currentWalletId, CURRENT_USER_ID, "Thẻ Tín Dụng", 5000000.0, "VND", 0);

            // TODO: 2. Hiển thị dữ liệu lên UI
            // editTextWalletName.setText(loadedWallet.getWallet_name());
            // editTextBalance.setText(String.valueOf(loadedWallet.getBalance()));
            // setSpinnerSelection(loadedWallet.getCurrency()); // Hàm phụ trợ để chọn giá trị Spinner

        } else {
            // **CHẾ ĐỘ THÊM MỚI**
            setTitle("Thêm Ví Mới");
        }

    }


}