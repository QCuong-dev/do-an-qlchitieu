package com.example.qlchitieu.Activites;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.qlchitieu.Adapter.CalendarAdapter;
import com.example.qlchitieu.R;
import com.example.qlchitieu.Transaction;
import com.example.qlchitieu.Adapter.TransactionAdapter;
import com.example.qlchitieu.databinding.ActivityCalendarBinding;

public class CalendarActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private ActivityCalendarBinding binding;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private CalendarAdapter calendarAdapter;
    private Button buttonPrevious, buttonNext;
    private RecyclerView transactionRecyclerView;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> allTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });


        //Lich

        initWidgets();
        selectedDate = LocalDate.now();

        buttonPrevious = findViewById(R.id.btnBack);
        buttonNext = findViewById(R.id.btnNext);

        buttonPrevious.setAlpha(1.0f);
        buttonNext.setAlpha(1.0f);

        setMonthView();

        // --- THÊM PHẦN KHỞI TẠO DANH SÁCH GIAO DỊCH ---
        createMockTransactions(); // Tạo dữ liệu mẫu
        initTransactionRecyclerView(); // Khởi tạo RecyclerView cho giao dịch

        setMonthView(); // Hàm này đã có

        // --- CẬP NHẬT DANH SÁCH LẦN ĐẦU KHI MỞ APP ---
        // Hiển thị giao dịch cho ngày hôm nay
        updateTransactionList(selectedDate);


        //Menu
    }



    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.tvMonthYear);

        transactionRecyclerView = findViewById(R.id.recyclerViewTransactions);
    }

    // Khởi tạo RecyclerView cho danh sách giao dịch
    private void initTransactionRecyclerView() {
        // Khởi tạo adapter với một danh sách rỗng ban đầu
        transactionAdapter = new TransactionAdapter(new ArrayList<>(), this);
        transactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        transactionRecyclerView.setAdapter(transactionAdapter);
        // Dòng này quan trọng vì RecyclerView của bạn nằm trong NestedScrollView
        transactionRecyclerView.setNestedScrollingEnabled(false);
    }

    // Tạo dữ liệu mẫu (bạn sẽ thay thế bằng CSDL sau này)
    private void createMockTransactions() {
        allTransactions = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate twoDaysAgo = today.minusDays(2);
        LocalDate tomorrow = today.plusDays(1);

        // Dữ liệu cho hôm nay
        allTransactions.add(new Transaction("Ăn uống", 50000, "expense", R.drawable.ic_chatbox, today)); // Dùng tạm icon có sẵn
        allTransactions.add(new Transaction("Lương tháng 11", 5000000, "income", R.drawable.ic_home, today));
        allTransactions.add(new Transaction("Đi chợ", 150000, "expense", R.drawable.ic_grid, today));

        // Dữ liệu cho hôm qua
        allTransactions.add(new Transaction("Xem phim", 200000, "expense", R.drawable.ic_calendar, yesterday));
        allTransactions.add(new Transaction("Xăng xe", 70000, "expense", R.drawable.ic_chatbox, yesterday));

        // Dữ liệu cho ngày mốt
        allTransactions.add(new Transaction("Tiền nhà", 2000000, "expense", R.drawable.ic_home, tomorrow));
    }

    // --- THÊM HÀM MỚI ---
    // Lọc và cập nhật danh sách giao dịch dựa trên ngày
    private void updateTransactionList(LocalDate date) {
        List<Transaction> filteredList = new ArrayList<>();

        // Nếu allTransactions chưa được khởi tạo thì thoát
        if (allTransactions == null) return;

        // Lặp qua tất cả giao dịch
        for (Transaction transaction : allTransactions) {
            // Chỉ thêm vào danh sách lọc nếu ngày trùng khớp
            if (transaction.getDate().equals(date)) {
                filteredList.add(transaction);
            }
        }

        // Cập nhật adapter với danh sách đã lọc
        transactionAdapter.filterList(filteredList);
    }



    private void setMonthView()
    {
//        monthYearText.setText(monthYearFromDate(selectedDate));
//        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);
//
//        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
//        calendarRecyclerView.setLayoutManager(layoutManager);
//        calendarRecyclerView.setAdapter(calendarAdapter);
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        // 2. Sử dụng biến thành viên, bỏ "CalendarAdapter" ở đầu
        calendarAdapter = new CalendarAdapter(daysInMonth, this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
        updateButtonStates(true);
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
        updateButtonStates(false);
    }

//    Cập nhật độ mờ của nút Back/Next

    private void updateButtonStates(boolean isPreviousClicked) {
        if (isPreviousClicked) {
            // Nhấn Back: Back mờ đi, Next rõ lại
            buttonPrevious.setAlpha(0.5f); // 50% mờ
            buttonNext.setAlpha(1.0f);     // 100% rõ
        } else {
            // Nhấn Next: Next mờ đi, Back rõ lại
            buttonPrevious.setAlpha(1.0f);   // 100% rõ
            buttonNext.setAlpha(0.5f);     // 50% mờ
        }
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if(!dayText.equals(""))
        {
            calendarAdapter.setSelectedPosition(position);

            int day = Integer.parseInt(dayText);
            LocalDate clickedDate = selectedDate.withDayOfMonth(day);

            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            updateTransactionList(clickedDate);
        }
    }
}