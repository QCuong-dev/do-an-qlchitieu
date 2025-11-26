package com.example.qlchitieu.Activites;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
                sendEmail();
//                Toast.makeText(HoTroActivity.this, "Mở trình gửi email...", Toast.LENGTH_SHORT).show();
            }
        });

        btnGoiTongDai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSupportPhone();
//                Toast.makeText(HoTroActivity.this, "Mở trình gọi điện...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendSupportPhone(){

        final String defaultPhoneNumber = "0774952891"; // Ví dụ: Số hotline hỗ trợ


        String uri = "tel:" + defaultPhoneNumber.trim();


        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse(uri));


        try {
            startActivity(dialIntent);
        } catch (ActivityNotFoundException e) {
            Log.e("Gọi điện", "Không thể tìm thấy ứng dụng xử lý cuộc gọi.", e);
        }
    }
    private void sendEmail() {
        String recipient = "cuongho304@gmail.com";
        String subject = "Yêu cầu hỗ trợ từ ứng dụng PayDC";
        String body = "Xin chào đội ngũ hỗ trợ,\n\nTôi đang gặp vấn đề:\n[Mô tả vấn đề của bạn ở đây]\n\nCảm ơn.";

        // 1. Mã hóa Subject và Body để chúng hoạt động trong URI (RẤT QUAN TRỌNG)
        // Các ký tự đặc biệt, dấu cách, và xuống dòng phải được mã hóa.
        String encodedSubject = Uri.encode(subject);
        String encodedBody = Uri.encode(body);

        // 2. Tạo URI mailto: với cả Subject và Body
        // Cấu trúc: mailto:recipient?subject=...&body=...
        String uriString = "mailto:" + recipient +
                "?subject=" + encodedSubject +
                "&body=" + encodedBody;

        // 3. Tạo Intent với ACTION_SENDTO và URI đã tạo
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(uriString));

        // BỎ CÁC DÒNG putExtra cho EXTRA_SUBJECT và EXTRA_TEXT ĐI

        // Khởi chạy Intent
        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            // Xử lý trường hợp không tìm thấy ứng dụng email
            // Ví dụ: Toast.makeText(this, "Không tìm thấy ứng dụng email.", Toast.LENGTH_SHORT).show();
        }
    }
}