package com.example.qlchitieu.Fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlchitieu.Activites.AddChitieuActivity;
import com.example.qlchitieu.Activites.AddEditWalletActivity;
import com.example.qlchitieu.Activites.HoTroActivity;
import com.example.qlchitieu.Activites.MainActivity;
import com.example.qlchitieu.Activites.OverviewWalletActivity;
import com.example.qlchitieu.Adapter.CategoryStatAdapter;
import com.example.qlchitieu.R;
import com.example.qlchitieu.controller.TransactionController;
import com.example.qlchitieu.controller.WalletController;
import com.example.qlchitieu.data.db.DBHelper;
import com.example.qlchitieu.helpers.Helpers;
import com.example.qlchitieu.model.Transaction;

// Import thư viện biểu đồ mới
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    // --- 1. KHAI BÁO BIẾN ---
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    // Controllers
    private WalletController walletController;
    private TransactionController transactionController;
    private Helpers helper;

    // Views cũ
    private ImageView btnHeadset;

    // Views Quick Actions (Các nút chức năng)
    private LinearLayout btnAddWallet, btnAddChitieu, btnChatbox, btnTienIchKhac, btnBillOverview;

    // Views MỚI cho giao diện biểu đồ tròn
    private TabLayout tabLayoutTime;
    private PieChart pieChart;
    private RecyclerView rcvCategoryDetails;
    private TextView tvCurrentTimeRange, tvTotalExpenseChart,tvMonthlyAmount; // Thay thế tvCurrentMonth, tvTotalExpense
    private ImageView ivTimePrev, ivTimeNext; // Thay thế ivMonthPrev, ivMonthNext


    // Logic thời gian
    private Calendar currentCalendar;
    private int currentTabMode = 1; // 0: Tuần, 1: Tháng, 2: Năm (Mặc định là Tháng)

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo Controller
        walletController = new WalletController(requireContext());
        transactionController = new TransactionController(requireContext());
        helper = new Helpers(requireContext());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDatabase();
        // 1. ÁNH XẠ VIEW
        initViews(view);

        // 2. KHỞI TẠO DỮ LIỆU BAN ĐẦU
        currentCalendar = Calendar.getInstance();

        // 3. CÀI ĐẶT SỰ KIỆN & CẤU HÌNH
        setupPieChartConfig();
        setupRecyclerView();
        addClickEvents(); // Sự kiện click các nút chức năng
        setupTimeEvents(); // Sự kiện Tab & Prev/Next

        // 4. LOAD DỮ LIỆU
        loadData();
    }

    void initDatabase(){
        DBHelper dbHelper = DBHelper.getInstance(requireContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(); // Load lại khi quay lại màn hình (ví dụ sau khi thêm GD)
    }

    private void initViews(View view) {
        // Header Buttons
        btnHeadset = view.findViewById(R.id.btn_headset);

        // Quick Actions
        btnAddWallet = view.findViewById(R.id.btnAddWallet); // Nút "Thêm chi tiêu tháng này"
        btnAddChitieu = view.findViewById(R.id.btnChitieuchitiet);
        btnBillOverview = view.findViewById(R.id.btnBillOverview);
        btnChatbox = view.findViewById(R.id.btnChatbox);
        btnTienIchKhac = view.findViewById(R.id.btnTienichkhac);

        // Chart & Stats Section (Mới)
        tabLayoutTime = view.findViewById(R.id.tabLayoutTime);
        pieChart = view.findViewById(R.id.pieChart);
        rcvCategoryDetails = view.findViewById(R.id.rcvCategoryDetails);

        tvCurrentTimeRange = view.findViewById(R.id.tvCurrentTimeRange);
        tvTotalExpenseChart = view.findViewById(R.id.tvTotalExpenseChart);

        ivTimePrev = view.findViewById(R.id.ivTimePrev);
        ivTimeNext = view.findViewById(R.id.ivTimeNext);

        tvMonthlyAmount = view.findViewById(R.id.tvMonthlyAmount);
    }

    private void setupRecyclerView() {
        rcvCategoryDetails.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvCategoryDetails.setNestedScrollingEnabled(false); // Để scroll mượt trong màn hình chính
    }

    private void setupPieChartConfig() {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 5, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        // Thiết kế dạng Donut (Rỗng ở giữa)
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleRadius(58f);

        // Text ở giữa
        pieChart.setDrawCenterText(true);
        pieChart.setCenterText("Chi tiêu");
        pieChart.setCenterTextSize(16f);
        pieChart.setCenterTextColor(Color.GRAY);

        // Tắt Legend (chú thích) vì đã có List bên dưới
        pieChart.getLegend().setEnabled(false);

        // Text khi không có dữ liệu
        pieChart.setNoDataText("Chưa có dữ liệu chi tiêu");
        pieChart.setNoDataTextColor(Color.BLACK);
    }

    private void setupTimeEvents() {
        // Mặc định chọn Tab "Tháng" (index 1)
        TabLayout.Tab monthTab = tabLayoutTime.getTabAt(1);
        if (monthTab != null) monthTab.select();

        // Xử lý khi chọn Tab
        tabLayoutTime.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTabMode = tab.getPosition(); // 0: Tuần, 1: Tháng, 2: Năm
                currentCalendar = Calendar.getInstance(); // Reset về thời gian hiện tại
                loadData();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Nút lùi/tiến thời gian
        ivTimePrev.setOnClickListener(v -> {
            adjustTime(-1);
            loadData();
        });

        ivTimeNext.setOnClickListener(v -> {
            adjustTime(1);
            loadData();
        });
    }

    private void adjustTime(int amount) {
        switch (currentTabMode) {
            case 0: // Tuần
                currentCalendar.add(Calendar.WEEK_OF_YEAR, amount);
                break;
            case 1: // Tháng
                currentCalendar.add(Calendar.MONTH, amount);
                break;
            case 2: // Năm
                currentCalendar.add(Calendar.YEAR, amount);
                break;
        }
    }

    // --- LOGIC TẢI VÀ XỬ LÝ DỮ LIỆU ---
    private void loadData() {
        // 1. Cập nhật Text thời gian (VD: Tháng 12/2025)
        updateTimeLabel();

        // 2. Lấy dữ liệu từ Database
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String queryDate = sdf.format(currentCalendar.getTime());

        tvMonthlyAmount.setText(walletController.getWallet() + " VND");

        // Lấy danh sách giao dịch.
        // Controller của bạn hiện có getListByMonth.
        // Để hỗ trợ Tuần/Năm tốt nhất, bạn nên thêm hàm getListByYear/Week vào Controller.
        // Ở đây mình dùng getListByMonth làm gốc và xử lý thêm.
        List<Transaction> transactionList = transactionController.getListByMonth(queryDate, false); // false = expense (chi tiêu) ? Kiểm tra lại logic bool này trong controller của bạn

        // Nếu Controller trả về cả thu và chi, ta cần lọc lấy 'expense'
        List<Transaction> filteredList = new ArrayList<>();

        // Lọc dữ liệu theo tab (Logic tạm thời chạy trên Client)
        for (Transaction t : transactionList) {
            // Chỉ lấy khoản CHI
            if ("expense".equalsIgnoreCase(t.getType())) {
                // Nếu là tab TUẦN, cần check xem ngày giao dịch có nằm trong tuần hiện tại của currentCalendar không
                if (currentTabMode == 0) {
                    // TODO: Thêm logic so sánh ngày ở đây nếu muốn chính xác tuyệt đối cho Tuần
                    // Tạm thời hiển thị dữ liệu tháng để demo giao diện
                    filteredList.add(t);
                } else {
                    filteredList.add(t);
                }
            }
        }

        // 3. Gom nhóm dữ liệu theo Danh mục
        Map<String, Double> categoryMap = new HashMap<>();
        double totalExpense = 0;

        for (Transaction t : filteredList) {
            String catName = t.getCategory_name();
            double amount = t.getAmount();

            categoryMap.put(catName, categoryMap.getOrDefault(catName, 0.0) + amount);
            totalExpense += amount;
        }

        // 4. Hiển thị Tổng tiền và Update UI
        tvTotalExpenseChart.setText("Tổng chi: " + helper.formatCurrency((int) totalExpense));
        updateChartAndListUI(categoryMap, totalExpense);
    }

    private void updateChartAndListUI(Map<String, Double> dataMap, double total) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        List<CategoryStatAdapter.CategoryStat> statList = new ArrayList<>();

        // Bảng màu sắc
        int[] colors = new int[]{
                Color.parseColor("#FF6384"), Color.parseColor("#36A2EB"),
                Color.parseColor("#FFCE56"), Color.parseColor("#4BC0C0"),
                Color.parseColor("#9966FF"), Color.parseColor("#FF9F40"),
                Color.GRAY
        };
        int index = 0;

        for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
            String name = entry.getKey();
            double value = entry.getValue();
            float percent = (total > 0) ? (float) ((value / total) * 100) : 0;

            // Add Pie Entry
            entries.add(new PieEntry((float) value, name));

            // Add List Entry
            int color = colors[index % colors.length];
            statList.add(new CategoryStatAdapter.CategoryStat(name, value, percent, color));
            index++;
        }

        // --- Cập nhật RecyclerView ---
        // Sắp xếp giảm dần theo số tiền
        Collections.sort(statList, (o1, o2) -> Double.compare(o2.amount, o1.amount));
        CategoryStatAdapter adapter = new CategoryStatAdapter(statList);
        rcvCategoryDetails.setAdapter(adapter);

        // --- Cập nhật PieChart ---
        if (entries.isEmpty()) {
            pieChart.setData(null);
            pieChart.setCenterText("Không có\ndữ liệu");
            pieChart.invalidate();
            return;
        } else {
            pieChart.setCenterText("Chi tiêu");
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setSliceSpace(3f);
        dataSet.setValueTextSize(0f); // Ẩn số trên biểu đồ cho đẹp

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.animateY(800);
        pieChart.invalidate();
    }

    private void updateTimeLabel() {
        SimpleDateFormat sdf;
        switch (currentTabMode) {
            case 0: // Tuần
                sdf = new SimpleDateFormat("'Tuần' w, yyyy", new Locale("vi", "VN"));
                tvCurrentTimeRange.setText(sdf.format(currentCalendar.getTime()));
                break;
            case 1: // Tháng
                sdf = new SimpleDateFormat("'Tháng' MM/yyyy", new Locale("vi", "VN"));
                tvCurrentTimeRange.setText(sdf.format(currentCalendar.getTime()));
                break;
            case 2: // Năm
                sdf = new SimpleDateFormat("yyyy", new Locale("vi", "VN"));
                tvCurrentTimeRange.setText(sdf.format(currentCalendar.getTime()));
                break;
        }
    }

    private void addClickEvents() {
        btnHeadset.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HoTroActivity.class);
            startActivity(intent);
        });


        btnAddChitieu.setOnClickListener(v -> {
            // Chức năng: Chi tiêu chi tiết (Nút Budget icon) -> Chuyển tab BottomNav
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showBottomNavigationTab(2, true);
            }
        });

        btnChatbox.setOnClickListener(v -> {
            // Chức năng: Chatbox
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showBottomNavigationTab(3, true);
            }
        });

        btnTienIchKhac.setOnClickListener(v -> {
            // Chức năng: Tiện ích khác
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).showBottomNavigationTab(4, true);
            }
        });

        btnAddWallet.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddEditWalletActivity.class);
            startActivity(intent);
        });

        // MỚI THÊM: Sự kiện click vào nút Quản lý Hóa đơn
        if (btnBillOverview != null) {
            btnBillOverview.setOnClickListener(v -> {
                // Chuyển sang màn hình OverviewActivity
                Intent intent = new Intent(getActivity(), OverviewWalletActivity.class);
                startActivity(intent);
            });
        }
    }

    private boolean isDateInCurrentRange(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Calendar targetDate = Calendar.getInstance();
            targetDate.setTime(sdf.parse(dateString));

            Calendar tempCurrent = (Calendar) currentCalendar.clone();

            switch (currentTabMode) {
                case 0: // TUẦN
                    // Lấy tuần của năm
                    int targetWeek = targetDate.get(Calendar.WEEK_OF_YEAR);
                    int targetYearWeek = targetDate.get(Calendar.YEAR);
                    int currentWeek = tempCurrent.get(Calendar.WEEK_OF_YEAR);
                    int currentYearWeek = tempCurrent.get(Calendar.YEAR);
                    return targetWeek == currentWeek && targetYearWeek == currentYearWeek;

                case 1: // THÁNG
                    int targetMonth = targetDate.get(Calendar.MONTH);
                    int targetYearMonth = targetDate.get(Calendar.YEAR);
                    int currentMonth = tempCurrent.get(Calendar.MONTH);
                    int currentYearMonth = tempCurrent.get(Calendar.YEAR);
                    return targetMonth == currentMonth && targetYearMonth == currentYearMonth;

                case 2: // NĂM
                    int targetYear = targetDate.get(Calendar.YEAR);
                    int currentYear = tempCurrent.get(Calendar.YEAR);
                    return targetYear == currentYear;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}