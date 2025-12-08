package com.example.qlchitieu.Activites;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qlchitieu.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class SyncDataActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextInputLayout tilDeviceId; // Layout chứa ID máy cũ (để xử lý nút copy)
    private TextInputEditText etDeviceId; // Hiển thị ID máy cũ
    private TextInputEditText etInputId;  // Nhập ID máy cũ (trên máy mới)
    private MaterialButton btnSync;
    private FrameLayout loadingOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sync_data);

        initViews();
        setupToolbar();
        setupOldDeviceCard(); // Logic cho máy cũ (Hiển thị ID)
        setupNewDeviceCard();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tilDeviceId = findViewById(R.id.tilDeviceId); // Nhớ thêm ID này vào XML như hướng dẫn trên
        etDeviceId = findViewById(R.id.etDeviceId);
        etInputId = findViewById(R.id.etInputId);
        btnSync = findViewById(R.id.btnSync);
        loadingOverlay = findViewById(R.id.loadingOverlay);
    }

    private void setupToolbar() {
        btnBack.setOnClickListener(v -> finish());
    }

    // --- LOGIC MÁY CŨ (SENDER) ---
    private void setupOldDeviceCard() {
        // 1. Tạo hoặc lấy ID định danh của máy
        // Trong thực tế: Bạn có thể lấy User ID từ Server hoặc Android ID
        String myDeviceId = generateRandomDeviceId();
        etDeviceId.setText(myDeviceId);

        // 2. Xử lý sự kiện click vào icon Copy
        if (tilDeviceId != null) {
            tilDeviceId.setEndIconOnClickListener(v -> {
                copyToClipboard(etDeviceId.getText().toString());
            });
        }
    }

    // --- LOGIC MÁY MỚI (RECEIVER) ---
    private void setupNewDeviceCard() {
        btnSync.setOnClickListener(v -> {
            String inputId = etInputId.getText().toString().trim();

            // 1. Validate dữ liệu
            if (inputId.isEmpty()) {
                etInputId.setError("Vui lòng nhập Mã ID");
                etInputId.requestFocus();
                return;
            }

            // Xóa lỗi nếu có trước đó
            etInputId.setError(null);

            // Ẩn bàn phím
            hideKeyboard();

            // 2. Thực hiện đồng bộ (Giả lập)
            performSyncProcess(inputId);
        });
    }

    // --- CÁC HÀM HỖ TRỢ (UTILITIES) ---

    // Hàm giả lập quá trình đồng bộ dữ liệu
    private void performSyncProcess(String targetId) {
        // Hiển thị loading
        loadingOverlay.setVisibility(View.VISIBLE);
        btnSync.setEnabled(false); // Chặn click liên tục

        // Giả lập gọi API mất 2 giây
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            loadingOverlay.setVisibility(View.GONE);
            btnSync.setEnabled(true);

            // Kiểm tra logic thành công hay thất bại (Demo luôn thành công)
            boolean isSuccess = true;

            if (isSuccess) {
                Toast.makeText(SyncDataActivity.this, "Đồng bộ dữ liệu từ ID " + targetId + " thành công!", Toast.LENGTH_LONG).show();
                // Có thể chuyển màn hình hoặc reload dữ liệu tại đây
            } else {
                Toast.makeText(SyncDataActivity.this, "Không tìm thấy thiết bị hoặc lỗi kết nối.", Toast.LENGTH_SHORT).show();
            }

        }, 2000); // 2000ms = 2s
    }

    // Hàm tạo ID ngẫu nhiên (Demo)
    private String generateRandomDeviceId() {
        Random random = new Random();
        int code = random.nextInt(900000) + 100000; // Random 6 số
        return "ID-" + code;
    }

    // Hàm copy vào bộ nhớ tạm
    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Device ID", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Đã sao chép mã ID", Toast.LENGTH_SHORT).show();
    }

    // Hàm ẩn bàn phím
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}