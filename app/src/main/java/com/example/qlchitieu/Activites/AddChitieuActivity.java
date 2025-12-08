package com.example.qlchitieu.Activites;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.qlchitieu.R;
import com.example.qlchitieu.controller.CategoryController;
import com.example.qlchitieu.controller.TransactionController;
import com.example.qlchitieu.data.db.firebase.BaseFirebase;
import com.example.qlchitieu.databinding.ActivityAddChitieuBinding;
import com.example.qlchitieu.databinding.ItemCategoryTransactionBinding;
import com.example.qlchitieu.model.Category;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddChitieuActivity extends AppCompatActivity {

    private ActivityAddChitieuBinding binding;
    private CategoryController categoryController;
    private TransactionController transactionController;

//     --- PHẦN THÊM MỚI ĐỂ XỬ LÝ KẾT QUẢ TỪ CAMERA/GALLERY ---
//     Launcher cho việc chọn ảnh từ thư viện
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMediaLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                    // TODO: Hiển thị ảnh này lên một ImageView (nếu có)
                    // binding.imageView.setImageURI(uri);
                    Toast.makeText(this, "Đã chọn ảnh từ thư viện!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Quyền đã được cấp, tiến hành mở Camera
                    openCameraInternal();
                } else {
                    // Quyền bị từ chối
                    Toast.makeText(this, "Cần quyền Camera để chụp ảnh.", Toast.LENGTH_SHORT).show();
                }
            });

    private void openCamera() {
        // 1. Kiểm tra xem quyền đã được cấp chưa
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            // Quyền đã được cấp, mở Camera ngay lập tức
            openCameraInternal();
        } else {
            // Quyền chưa được cấp, yêu cầu quyền từ người dùng
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA);
        }
    }
    private void openCameraInternal() {
        // Không cần kiểm tra quyền nữa, chỉ cần chạy launcher
        takePicturePreviewLauncher.launch(null);
    }
    // Launcher cho việc chụp ảnh (chỉ lấy ảnh thumbnail, đơn giản)
    private final ActivityResultLauncher<Void> takePicturePreviewLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    Log.d("Camera", "Photo taken!");
                    // TODO: Hiển thị ảnh này lên một ImageView (nếu có)
                    // binding.imageView.setImageBitmap(bitmap);
                    Toast.makeText(this, "Đã chụp ảnh!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Camera", "No photo taken");
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddChitieuBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        categoryController = new CategoryController(this);
        transactionController = new TransactionController(this);
        setContentView(binding.getRoot());

        // Cài đặt ngày giờ hiện tại làm mặc định
        setupDefaultDateTime();

        // Thêm sự kiện click cho các nút
        setupClickListeners();

        // Render categories
//        loadCategories();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
    }

    /**
     * Gộp tất cả các sự kiện click vào một nơi
     */
    private void setupClickListeners() {
        // 1. Nút đóng
        binding.ivClose.setOnClickListener(v -> finish()); // Đóng Activity

        // 2. Nút Lưu
        binding.tvSave.setOnClickListener(v -> saveTransaction());

        // 3. Nút Thêm mới (danh mục)
        binding.chipThemMoi.setOnClickListener(v -> clickAddCategory());

        // 4. Chọn ngày
        binding.tvDate.setOnClickListener(v -> showDatePicker());

        // 5. Chọn giờ
        binding.tvTime.setOnClickListener(v -> showTimePicker());

        // 6. Nhập ghi chú
        binding.tvNote.setOnClickListener(v -> showNoteDialog());

        // 7. Mở Thư viện ảnh
        binding.btnGallery.setOnClickListener(v -> openGallery());

        // 8. Mở Camera
        binding.btnCamera.setOnClickListener(v -> openCamera());
    }

    /**
     * Xử lý logic khi nhấn nút Lưu
     */
    private void saveTransaction() {
        // Lấy số tiền
        String amountString = binding.etAmount.getText().toString();
        if (amountString.isEmpty() || amountString.equals("0 VND")) {
            Toast.makeText(this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy danh mục được chọn
        String category = getSelectedCategory();
        if (category == null) {
            Toast.makeText(this, "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy các thông tin khác
        String date = binding.tvDate.getText().toString();
        String time = binding.tvTime.getText().toString();
        String note = binding.tvNote.getText().toString();
        if (note.equals("Ghi Chú")) {
            note = ""; // Nếu người dùng chưa nhập gì, lưu là rỗng
        }

        // Tạo một thông báo Toast để hiển thị kết quả
        transactionController.saveTransaction(Integer.parseInt(amountString), category, note, date, time, binding.snpOption.getSelectedItem().toString(), new BaseFirebase.DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                Toast.makeText(AddChitieuActivity.this, data, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(AddChitieuActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Sau khi lưu thành công, bạn có thể gọi finish()
         finish();
    }

    /**
     * Lấy text của Chip đang được chọn trong ChipGroup
     *
     * @return Tên danh mục (ví dụ: "🍜 Ăn uống") hoặc null nếu chưa chọn
     */
    private String getSelectedCategory() {
        int selectedChipId = binding.chipGroupCategories.getCheckedChipId();
        if (selectedChipId != -1) {
            Chip selectedChip = findViewById(selectedChipId);
            return selectedChip.getTag().toString();
        }
        return null; // Không có chip nào được chọn
    }

    private void loadCategories() {

        binding.chipGroupCategories.removeAllViews();
        Chip chipThemMoi = binding.chipThemMoi;

        List<Category> list = categoryController.getAll();
        String firstCategoryId = null; // Biến lưu ID của Chip đầu tiên

        for(int i = 0 ; i < list.size() ; i++){
            Category category = list.get(i);

            if (i == 0) {
                firstCategoryId = String.valueOf(category.getId());
            }

            addCategoryToLayout(category);
        }

        binding.chipGroupCategories.addView(chipThemMoi);

        if (firstCategoryId != null) {

            com.google.android.material.chip.ChipGroup chipGroup = binding.chipGroupCategories;
            int chipToSelectId = View.NO_ID;

            for (int j = 0; j < chipGroup.getChildCount(); j++) {
                View child = chipGroup.getChildAt(j);
                if (child instanceof com.google.android.material.chip.Chip) {
                    com.google.android.material.chip.Chip chip = (com.google.android.material.chip.Chip) child;

                    if (firstCategoryId.equals(chip.getTag())) {
                        chipToSelectId = chip.getId();
                        break;
                    }
                }
            }

            // Chỉ gọi check(id) nếu tìm thấy ID View hợp lệ
            if (chipToSelectId != View.NO_ID) {
                chipGroup.check(chipToSelectId);
            } else {
                Log.e("CategoryFragment", "Không tìm thấy Chip đầu tiên để chọn với ID: " + firstCategoryId);
            }
        }
    }

    // KHÔNG SỬA ĐỔI PHƯƠNG THỨC NÀY
    private void addCategoryToLayout(Category category){
        ChipGroup chipGroup = binding.chipGroupCategories;

        ItemCategoryTransactionBinding itemBinding =
                ItemCategoryTransactionBinding.inflate(getLayoutInflater(), chipGroup, false);

        Chip newChip = itemBinding.getRoot();

        newChip.setText(category.getName());
        newChip.setTag(category.getUuid());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            newChip.setId(View.generateViewId());
        } else {
            newChip.setId((int) System.currentTimeMillis());
        }

        chipGroup.addView(newChip);
    }

    /**
     * Mở Activity thêm danh mục
     */
    private void clickAddCategory() {
        Intent intent = new Intent(AddChitieuActivity.this, AddCategorySheetActivity.class);
        startActivity(intent);
    }

    /**
     * Mở thư viện ảnh (sử dụng PickVisualMedia)
     */
    private void openGallery() {
        // Chỉ chọn ảnh
        pickMediaLauncher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }


    private void setupDefaultDateTime() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateString = String.format(Locale.getDefault(), "%02d-%02d-%d", day, month + 1, year);
        binding.tvDate.setText(dateString);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String timeString = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
        binding.tvTime.setText(timeString);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String dateString = String.format(Locale.getDefault(), "%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear);
                    binding.tvDate.setText(dateString);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    String timeString = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    binding.tvTime.setText(timeString);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm Ghi Chú");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setLines(4);
        input.setHint("Nhập ghi chú của bạn...");

        String currentNote = binding.tvNote.getText().toString();
        if (!currentNote.equals("Ghi Chú")) {
            input.setText(currentNote);
        }

        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        params.leftMargin = 50;
        params.rightMargin = 50;
        input.setLayoutParams(params);
        container.addView(input);

        builder.setView(container);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String note = input.getText().toString().trim();
            if (note.isEmpty()) {
                binding.tvNote.setText("Ghi Chú");
                binding.tvNote.setTextColor(ContextCompat.getColor(this, R.color.gray));
            } else {
                binding.tvNote.setText(note);
                binding.tvNote.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}