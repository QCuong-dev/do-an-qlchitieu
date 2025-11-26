package com.example.qlchitieu.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlchitieu.Adapter.CalendarAdapter;
import com.example.qlchitieu.R;
import com.example.qlchitieu.Transaction;
import com.example.qlchitieu.Adapter.TransactionAdapter;
import com.example.qlchitieu.databinding.FragmentCalendarBinding;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener,TransactionAdapter.OnItemLongClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //MyCode

    private FragmentCalendarBinding binding;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private CalendarAdapter calendarAdapter;
    private Button buttonPrevious, buttonNext;
    private RecyclerView transactionRecyclerView;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> allTransactions;



    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

//    Thay thế onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

//    Toàn bộ logic trong onCreate của Activity sẽ vào onViewCreated
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedDate = LocalDate.now();

        // Gắn listener cho 2 nút Back/Next (BẮT BUỘC vì android:onClick không hoạt động)
        binding.btnBack.setOnClickListener(v -> previousMonthAction(v));
        binding.btnNext.setOnClickListener(v -> nextMonthAction(v));

        binding.btnBack.setAlpha(1.0f);
        binding.btnNext.setAlpha(1.0f);

        // --- KHỞI TẠO DANH SÁCH GIAO DỊCH ---
        createMockTransactions();
        initTransactionRecyclerView(); // Khởi tạo RecyclerView cho giao dịch

        setMonthView(); // Hiển thị lịch

        // --- CẬP NHẬT DANH SÁCH LẦN ĐẦU ---
        updateTransactionList(selectedDate);
    }

    private void initTransactionRecyclerView() {
        // SỬA LỖI: dùng requireContext() thay cho 'this'
        transactionAdapter = new TransactionAdapter(new ArrayList<>(), requireContext());

        transactionAdapter.setOnItemLongClickListener(this);

        binding.recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewTransactions.setAdapter(transactionAdapter);
        binding.recyclerViewTransactions.setNestedScrollingEnabled(false);
    }

    private void createMockTransactions() {
        allTransactions = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate twoDaysAgo = today.minusDays(2);
        LocalDate tomorrow = today.plusDays(1);

        // Dữ liệu cho hôm nay
        allTransactions.add(new Transaction("Ăn uống", 50000, "expense", R.drawable.ic_eat, today));
        allTransactions.add(new Transaction("Lương tháng 11", 5000000, "income", R.drawable.ic_income, today));
        allTransactions.add(new Transaction("Đi chợ", 150000, "expense", R.drawable.ic_market, today));
        allTransactions.add(new Transaction("Xem phim", 200000, "expense", R.drawable.ic_cinema, yesterday));
        allTransactions.add(new Transaction("Xăng xe", 70000, "expense", R.drawable.ic_fuel, yesterday));
        allTransactions.add(new Transaction("Tiền nhà", 2000000, "expense", R.drawable.ic_home, tomorrow));
    }

    private void updateTransactionList(LocalDate date) {
        List<Transaction> filteredList = new ArrayList<>();
        if (allTransactions == null) return;

        for (Transaction transaction : allTransactions) {
            if (transaction.getDate().equals(date)) {
                filteredList.add(transaction);
            }
        }
        transactionAdapter.filterList(filteredList);
    }

    private void setMonthView() {
        // Dùng binding để truy cập
        binding.tvMonthYear.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        // SỬA LỖI: Dùng 'this' (cho Listener) và 'requireContext()' (cho Context)
        calendarAdapter = new CalendarAdapter(daysInMonth, this, selectedDate); // 'this' vì Fragment này implement OnItemListener
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 7);
        binding.calendarRecyclerView.setLayoutManager(layoutManager);
        binding.calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++) {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            } else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    // Các hàm này giờ là private, được gọi từ setOnClickListener
    private void previousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
        updateButtonStates(true);
    }

    private void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
        updateButtonStates(false);
    }

    private void updateButtonStates(boolean isPreviousClicked) {
        if (isPreviousClicked) {
            binding.btnBack.setAlpha(0.5f);
            binding.btnNext.setAlpha(1.0f);
        } else {
            binding.btnBack.setAlpha(1.0f);
            binding.btnNext.setAlpha(0.5f);
        }
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if(!dayText.equals("")) {
            calendarAdapter.setSelectedPosition(position);

            int day = Integer.parseInt(dayText);
            LocalDate clickedDate = selectedDate.withDayOfMonth(day);

            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            // SỬA LỖI: Dùng requireContext()
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();

            updateTransactionList(clickedDate);
        }
    }

    //Thêm hàm này để tránh memory leak với View Binding
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemLongClick(int position, Transaction transaction) {
        if (allTransactions == null || transaction == null) {
            Toast.makeText(requireContext(), "Lỗi: Dữ liệu giao dịch không hợp lệ.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Khởi tạo AlertDialog.Builder
        new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận Xóa Giao Dịch")
                .setMessage("Bạn có chắc chắn muốn xóa giao dịch '" + transaction.getCategoryName() + "' không?")

                // 2. Nút "CÓ" (Xóa)
                .setPositiveButton("CÓ (Xóa)", (dialog, which) -> {
                    // Logic xóa giao dịch khi người dùng xác nhận
                    boolean removed = allTransactions.remove(transaction);

                    if (removed) {
                        // Cập nhật lại danh sách hiển thị
                        updateTransactionList(selectedDate);

                        String message = "Đã xóa thành công giao dịch: " + transaction.getCategoryName();
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Lỗi: Không tìm thấy giao dịch để xóa.", Toast.LENGTH_SHORT).show();
                    }
                })

                // 3. Nút "KHÔNG" (Hủy)
                .setNegativeButton("KHÔNG (Hủy)", (dialog, which) -> {
                    // Không làm gì cả, chỉ đóng dialog
                    dialog.dismiss();
                    Toast.makeText(requireContext(), "Đã hủy thao tác xóa.", Toast.LENGTH_SHORT).show();
                })

                // 4. Hiển thị Dialog
                .setIcon(android.R.drawable.ic_dialog_alert) // Biểu tượng cảnh báo
                .show();
    }
}