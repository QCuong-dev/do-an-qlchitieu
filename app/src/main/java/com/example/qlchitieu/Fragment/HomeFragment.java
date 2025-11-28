package com.example.qlchitieu.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlchitieu.Activites.AddChitieuActivity;
import com.example.qlchitieu.Activites.AddEditWalletActivity;
import com.example.qlchitieu.Activites.HoTroActivity;
import com.example.qlchitieu.Activites.MainActivity;
import com.example.qlchitieu.R;
import com.example.qlchitieu.controller.TransactionController;
import com.example.qlchitieu.controller.WalletController;
import com.example.qlchitieu.helpers.Helpers;
import com.example.qlchitieu.model.Transaction;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private WalletController walletController;
    private TransactionController transactionController;
    private Helpers helper;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    private BarChart barChart;
    private Button btnThemGD;

    private ImageView btnHeadset, btnClose;
    private LinearLayout btnAddWallet, btnAddChitieu, btnChatbox, btnTienIchKhac;

    // === CÁC BIẾN MỚI CHO LOGIC THÁNG ===
    private ImageView ivMonthPrev, ivMonthNext;
    private TextView tvCurrentMonth, tvTotalExpense, tvTotalIncome, tvThongbao;
    ;

    private Calendar currentCalendar; // Biến để theo dõi tháng đang chọn
    private SimpleDateFormat monthFormatter; // Định dạng "Tháng 11/2025"
    private NumberFormat currencyFormatter; // Định dạng tiền "1.000.000"

    private static class LabelFormatter extends ValueFormatter {
        public String[] labels = new String[]{"Ăn uống", "Du lịch", "Mua sắm", "Đi lại", "Khác"};

        @Override
        public String getFormattedValue(float value) {
            // Chuyển giá trị float (0, 1, 2, ...) thành chỉ mục mảng
            int index = (int) value;

            // Đảm bảo chỉ mục nằm trong giới hạn của mảng nhãn
            if (index >= 0 && index < labels.length) {
                return labels[index];
            }

            // Trả về giá trị mặc định nếu không khớp
            return String.valueOf(index);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 1. Ánh xạ LineChart từ file XML
        barChart = view.findViewById(R.id.chart);

        btnThemGD = view.findViewById(R.id.btnThemGD);

        btnHeadset = view.findViewById(R.id.btn_headset);
        btnAddWallet = view.findViewById(R.id.btnAddWallet);
        btnAddChitieu = view.findViewById(R.id.btnChitieuchitiet);
        btnChatbox = view.findViewById(R.id.btnChatbox);
        btnTienIchKhac = view.findViewById(R.id.btnTienichkhac);

        // === ÁNH XẠ CÁC VIEW MỚI ===
        ivMonthPrev = view.findViewById(R.id.ivMonthPrev);
        ivMonthNext = view.findViewById(R.id.ivMonthNext);
        tvCurrentMonth = view.findViewById(R.id.tvCurrentMonth);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvThongbao = view.findViewById(R.id.tvThongbao);

        // === KHỞI TẠO CÁC BIẾN LOGIC ===
        currentCalendar = Calendar.getInstance(); // Lấy ngày giờ hiện tại
        Locale localeVN = new Locale("vi", "VN");
        monthFormatter = new SimpleDateFormat("MMMM yyyy", localeVN); // Ví dụ: "Tháng Mười Một 2025"
        currencyFormatter = NumberFormat.getInstance(localeVN);


        btnThemGD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddChitieuActivity.class);
                startActivity(intent);
            }
        });

        addClickEvents();

        setupBarChart(new LabelFormatter());
//        loadBarChartData();

        setupMonthNavigation();

        initView();
//        updateDashboard();
        updateBarChart();
    }

    private void initView() {
        tvTotalIncome.setText(walletController.getCurrentWallet());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Use appropriate Locale
        String formattedDate = sdf.format(currentCalendar.getTime());
        int result = 0;
        for (Transaction t : transactionController.getListByMonth(formattedDate,false)) {
            if (!t.getType().equals("income")) result -= (int) t.getAmount();
        }
        tvTotalExpense.setText(helper.formatCurrency(result));
    }

    private void setupMonthNavigation() {
        ivMonthPrev.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1); // Lùi 1 tháng
            updateBarChart(); // Cập nhật lại giao diện
        });

        ivMonthNext.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1); // Tăng 1 tháng
            updateBarChart(); // Cập nhật lại giao diện
        });
    }

    private void updateBarChart(){
        ArrayList<BarEntry> entries = new ArrayList<>();

        // 1. Cập nhật TextView tháng
        Calendar today = Calendar.getInstance();
        if (currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
            tvCurrentMonth.setText("Tháng này");
        } else {
            String monthText = monthFormatter.format(currentCalendar.getTime());
            // Viết hoa chữ cái đầu cho "Tháng..."
            monthText = monthText.substring(0, 1).toUpperCase() + monthText.substring(1);
            tvCurrentMonth.setText(monthText);
        }

        // 2. Lấy dữ liệu
//        int year = currentCalendar.get(Calendar.YEAR);
//        int month = currentCalendar.get(Calendar.MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Use appropriate Locale
        String formattedDate = sdf.format(currentCalendar.getTime());

        List<String> listCategoryName = new ArrayList<>();
        List<Transaction> transactionList = transactionController.getListByMonth(formattedDate,true);
        for(Transaction t : transactionList){
            listCategoryName.add(t.getCategory_name());
        }

        LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.labels = listCategoryName.toArray(new String[]{});
        setupBarChart(labelFormatter);

        ArrayList<BarEntry> barEntries = getBarChartDataForMonth(formattedDate,transactionList);
        loadBarChartData(barEntries);
    }

    private void updateDashboard() {
        // 1. Cập nhật TextView tháng
        Calendar today = Calendar.getInstance();
        if (currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
            tvCurrentMonth.setText("Tháng này");
        } else {
            String monthText = monthFormatter.format(currentCalendar.getTime());
            // Viết hoa chữ cái đầu cho "Tháng..."
            monthText = monthText.substring(0, 1).toUpperCase() + monthText.substring(1);
            tvCurrentMonth.setText(monthText);
        }

        // 2. Lấy dữ liệu
        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH);

        // Dùng hàm dữ liệu giả để minh họa (giữ nguyên logic cũ)
        long[] currentMonthData = getFakeDataForMonth(year, month);
        long currentIncome = currentMonthData[0];
        long currentExpense = currentMonthData[1];

        // Lấy dữ liệu tháng trước (giữ nguyên logic cũ)
        Calendar prevCalendar = (Calendar) currentCalendar.clone();
        prevCalendar.add(Calendar.MONTH, -1);
        long[] prevMonthData = getFakeDataForMonth(prevCalendar.get(Calendar.YEAR), prevCalendar.get(Calendar.MONTH));
        long prevExpense = prevMonthData[1];

        // 3. Cập nhật TextView thu/chi (giữ nguyên logic cũ)
//        tvTotalIncome.setText(currencyFormatter.format(currentIncome));
//        tvTotalExpense.setText(currencyFormatter.format(currentExpense));

        // 4. LOGIC CẬP NHẬT TVTHONGBAO (giữ nguyên logic cũ)
        long expenseDifference = currentExpense - prevExpense;

        if (expenseDifference > 0) {
            String diffText = currencyFormatter.format(expenseDifference) + "đ";
            tvThongbao.setText(String.format("Tăng %s so với cùng kỳ tháng trước", diffText));
            tvThongbao.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trending_up, 0, 0, 0);
            tvThongbao.setBackgroundResource(R.drawable.rounded_gray_bg);
            tvThongbao.setTextColor(Color.rgb(185, 28, 28));

        } else if (expenseDifference < 0) {
            long absoluteDifference = Math.abs(expenseDifference);
            String diffText = currencyFormatter.format(absoluteDifference) + "đ";
            tvThongbao.setText(String.format("Giảm %s so với cùng kỳ tháng trước", diffText));
            tvThongbao.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trending_down, 0, 0, 0);
            tvThongbao.setBackgroundResource(R.drawable.rounded_gray_bg);
            tvThongbao.setTextColor(Color.rgb(4, 120, 87));

        } else {
            tvThongbao.setText("Chi tiêu tháng này bằng tháng trước");
            tvThongbao.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvThongbao.setBackgroundResource(R.drawable.rounded_gray_bg);
            tvThongbao.setTextColor(Color.rgb(55, 65, 81));
        }

        // 5. Tải lại biểu đồ BarChart với dữ liệu của tháng mới
//        ArrayList<BarEntry> barEntries = getBarChartDataForMonth(year, month);
//        loadBarChartData(barEntries);
    }

    // === HÀM MỚI: DỮ LIỆU GIẢ ĐỂ MINH HỌA ===
    // (Bạn sẽ thay thế hàm này bằng logic thật)
    private long[] getFakeDataForMonth(int year, int month) {
        // Tạo dữ liệu giả dựa trên tháng
        long fakeIncome = 10000000 + (month * 500000) + (year - 2025) * 1000000;
        long fakeExpense = 1000000 + (month * 100000) + (year - 2025) * 50000;
        return new long[]{fakeIncome, fakeExpense};
    }

    private ArrayList<BarEntry> getBarChartDataForMonth(String formattedDate,List<Transaction> transactionListGroupBy) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        // Đây là logic giả lập: tạo dữ liệu khác nhau tùy theo tháng và năm
        // Base expense cho tháng 0 (January) của năm gốc (ví dụ: 2025)
//        long baseExpense = 50000;
//        // Thêm một hệ số dựa trên tháng (month là 0-indexed)
//        long monthFactor = (long) month * 15000;
//        // Thêm một hệ số dựa trên năm
//        long yearFactor = (long) (year - 2025) * 50000;
//
//        // Giả lập dữ liệu chi tiêu cho 5 danh mục:
//        // "Ăn uống", "Du lịch", "Mua sắm", "Đi lại", "Khác"
//        entries.add(new BarEntry(0, baseExpense * 8 + monthFactor * 2 + yearFactor)); // Ăn uống (index 0)
//        entries.add(new BarEntry(1, baseExpense * 3 + monthFactor + yearFactor));     // Du lịch (index 1)
//        entries.add(new BarEntry(2, baseExpense * 5 + (long) (monthFactor * 1.5) + yearFactor)); // Mua sắm (index 2)
//        entries.add(new BarEntry(3, baseExpense * 2 + monthFactor + yearFactor));     // Đi lại (index 3)
//        entries.add(new BarEntry(4, baseExpense * 4 + (long) (monthFactor * 0.5) + yearFactor)); // Khác (index 4)


        Map<String, Double> categoryTotals = new HashMap<>();
        List<Transaction> transactionList = transactionController.getListByMonth(formattedDate,false);
        for (Transaction transaction : transactionList) {

            // 4. KIỂM TRA ĐIỀU KIỆN 1: Phải là giao dịch "expense"
            if (transaction.getType().equalsIgnoreCase("expense")) {

                String category = transaction.getCategory_name();
                double amount = transaction.getAmount();

                // 5. Tính tổng: Sử dụng compute hoặc get/put
                // getOrDefault(key, defaultValue) giúp lấy giá trị hiện tại (hoặc 0.0 nếu chưa có)
                double currentTotal = categoryTotals.getOrDefault(category, 0.0);

                // Cộng thêm số tiền mới vào tổng
                double newTotal = currentTotal + amount;

                // Cập nhật lại Map
                categoryTotals.put(category, newTotal);

                // Cú pháp ngắn hơn (Java 8+):
                // categoryTotals.merge(category, amount, Double::sum);
            }
        }

        AtomicInteger indexCounter = new AtomicInteger(0);

        // Duyệt qua entrySet() của Map đã tính tổng
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {

            String category = entry.getKey();
            // Lấy tổng đã tính, ép kiểu sang float (nếu thư viện BarEntry yêu cầu float)
            float total = entry.getValue().floatValue();

            // Add BarEntry
            // Index (x) được tăng tự động
            entries.add(new BarEntry(indexCounter.getAndIncrement(), total));
        }

        return entries;
    }

    // === HÀM MỚI: CẬP NHẬT GIAO DIỆN KHI ĐỔI THÁNG ===
//    private void updateDashboard() {

    /// /        // 1. Cập nhật TextView tháng
    /// /        Calendar today = Calendar.getInstance();
    /// /        if (currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
    /// /                currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
    /// /            tvCurrentMonth.setText("Tháng này");
    /// /        } else {
    /// /            String monthText = monthFormatter.format(currentCalendar.getTime());
    /// /            // Viết hoa chữ cái đầu cho "Tháng..."
    /// /            monthText = monthText.substring(0, 1).toUpperCase() + monthText.substring(1);
    /// /            tvCurrentMonth.setText(monthText);
    /// /        }
    /// /
    /// /        // 2. Lấy dữ liệu (THAY THẾ BẰNG LOGIC CỦA BẠN)
    /// /        int year = currentCalendar.get(Calendar.YEAR);
    /// /        int month = currentCalendar.get(Calendar.MONTH); // Lưu ý: Tháng 0-indexed (0=Tháng 1)
    /// /
    /// /        // Dùng hàm dữ liệu giả để minh họa
    /// /        long[] data = getFakeDataForMonth(year, month);
    /// /        long income = data[0];
    /// /        long expense = data[1];
    /// /
    /// /        // 3. Cập nhật TextView thu/chi
    /// /        tvTotalIncome.setText(currencyFormatter.format(income));
    /// /        tvTotalExpense.setText(currencyFormatter.format(expense));
    /// /
    /// /        // 4. (Tùy chọn) Tải lại biểu đồ BarChart với dữ liệu của tháng mới
    /// /        // loadBarChartData(year, month);
//        // 1. Cập nhật TextView tháng (Giữ nguyên)
//        Calendar today = Calendar.getInstance();
//        if (currentCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
//                currentCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
//            tvCurrentMonth.setText("Tháng này");
//        } else {
//            String monthText = monthFormatter.format(currentCalendar.getTime());
//            // Viết hoa chữ cái đầu cho "Tháng..."
//            monthText = monthText.substring(0, 1).toUpperCase() + monthText.substring(1);
//            tvCurrentMonth.setText(monthText);
//        }
//
//        // 2. Lấy dữ liệu
//        int year = currentCalendar.get(Calendar.YEAR);
//        int month = currentCalendar.get(Calendar.MONTH);
//
//        // Dùng hàm dữ liệu giả để minh họa
//        long[] currentMonthData = getFakeDataForMonth(year, month);
//        long currentIncome = currentMonthData[0];
//        long currentExpense = currentMonthData[1];
//
//        // Lấy dữ liệu tháng trước
//        Calendar prevCalendar = (Calendar) currentCalendar.clone();
//        prevCalendar.add(Calendar.MONTH, -1);
//        long[] prevMonthData = getFakeDataForMonth(prevCalendar.get(Calendar.YEAR), prevCalendar.get(Calendar.MONTH));
//        long prevExpense = prevMonthData[1]; // Chỉ so sánh chi tiêu để minh họa
//
//        // 3. Cập nhật TextView thu/chi (Giữ nguyên)
//        tvTotalIncome.setText(currencyFormatter.format(currentIncome));
//        tvTotalExpense.setText(currencyFormatter.format(currentExpense));
//
//        // 4. LOGIC CẬP NHẬT TVTHONGBAO
//        long expenseDifference = currentExpense - prevExpense; // Lượng thay đổi chi tiêu so với tháng trước
//
//        if (expenseDifference > 0) {
//            // Chi tiêu Tăng
//            String diffText = currencyFormatter.format(expenseDifference) + "đ";
//            tvThongbao.setText(String.format("Tăng %s so với cùng kỳ tháng trước", diffText));
//
//            // Thiết lập Drawable (Icon) bên trái: ic_trending_up
//            tvThongbao.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trending_up, 0, 0, 0);
//
//            // Cập nhật màu background (tùy chọn)
//            tvThongbao.setBackgroundResource(R.drawable.rounded_gray_bg); // Giả sử bạn có bg màu đỏ nhạt
//            // Cập nhật màu chữ (tùy chọn)
//            tvThongbao.setTextColor(Color.rgb(185, 28, 28)); // Màu đỏ đậm
//
//        } else if (expenseDifference < 0) {
//            // Chi tiêu Giảm
//            long absoluteDifference = Math.abs(expenseDifference);
//            String diffText = currencyFormatter.format(absoluteDifference) + "đ";
//            tvThongbao.setText(String.format("Giảm %s so với cùng kỳ tháng trước", diffText));
//
//            // Thiết lập Drawable (Icon) bên trái: ic_trending_down
//            tvThongbao.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_trending_down, 0, 0, 0);
//
//            // Cập nhật màu background (tùy chọn)
//            tvThongbao.setBackgroundResource(R.drawable.rounded_gray_bg); // Giả sử bạn có bg màu xanh lá nhạt
//            // Cập nhật màu chữ (tùy chọn)
//            tvThongbao.setTextColor(Color.rgb(4, 120, 87)); // Màu xanh lá đậm
//
//        } else {
//            // Chi tiêu Không đổi
//            tvThongbao.setText("Chi tiêu tháng này bằng tháng trước");
//
//            // Sử dụng một icon trung tính hoặc ẩn
//            tvThongbao.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//
//            // Thiết lập màu trung tính (ví dụ: nền xám nhạt)
//            tvThongbao.setBackgroundResource(R.drawable.rounded_gray_bg);
//            tvThongbao.setTextColor(Color.rgb(55, 65, 81)); // Màu chữ xám/đen
//        }
//
//        // 5. (Tùy chọn) Tải lại biểu đồ BarChart với dữ liệu của tháng mới
//        // loadBarChartData(year, month);
//    }

    private void addClickEvents() {


        btnHeadset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HoTroActivity.class);
                startActivity(intent);
            }
        });

        // Quick Actions
        btnAddWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEditWalletActivity.class);
                startActivity(intent);
            }
        });

        btnAddChitieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    // Gọi phương thức public từ MainActivity để thực hiện hành động
                    mainActivity.showBottomNavigationTab(2, true);
                }
            }
        });

        btnChatbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    // Gọi phương thức public từ MainActivity để thực hiện hành động
                    mainActivity.showBottomNavigationTab(3, true);
                }
            }
        });

        btnTienIchKhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    // Gọi phương thức public từ MainActivity để thực hiện hành động
                    mainActivity.showBottomNavigationTab(4, true);
                }
            }
        });
    }


    // Phương thức tùy chỉnh biểu đồ
    private void setupBarChart(LabelFormatter labelFormatter) {
        // Bỏ mô tả (description label)
        barChart.setDescription(null);
        // Tắt trục Y bên phải
        barChart.getAxisRight().setEnabled(false);
        // Tắt vẽ lưới nền
        barChart.setDrawGridBackground(false);

        // Tùy chỉnh trục X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Đặt nhãn trục X ở dưới cùng
        xAxis.setGranularity(1f); // Đảm bảo các nhãn không bị trùng lặp
        xAxis.setDrawGridLines(false); // Tắt đường lưới trục X

        // Tùy chỉnh trục Y bên trái
        xAxis.setValueFormatter(labelFormatter);
        barChart.getAxisLeft().setDrawGridLines(true); // Bật đường lưới trục Y (mờ)

        // Bật và tùy chỉnh Chú thích (Legend)
        Legend legend = barChart.getLegend();
        legend.setEnabled(true); // Hiển thị chú thích
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // Vị trí trên cùng
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); // Căn phải
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL); // Xếp ngang
        legend.setDrawInside(false); // Vẽ bên ngoài biểu đồ
        legend.setForm(Legend.LegendForm.SQUARE); // Hình dạng (vuông)
        legend.setTextSize(12f);
    }

    private void loadBarChartData(ArrayList<BarEntry> entries) {
        // 3.2. Tạo một BarDataSet từ danh sách entries
        BarDataSet dataSet = new BarDataSet(entries, "Chi tiêu"); // Label cho bộ dữ liệu này

        // Tùy chỉnh màu sắc cho cột (dùng màu xanh dương đơn giản)
        dataSet.setColor(Color.rgb(102, 178, 255));
        dataSet.setValueTextSize(10f); // Kích thước chữ của giá trị
        dataSet.setValueTextColor(Color.BLACK); // Màu chữ của giá trị

        // 3.3. Tạo BarData để chứa tất cả các BarDataSet
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f); // Đặt độ rộng của cột (0.9f là 90% không gian)

        // 3.4. Nạp BarData vào BarChart
        barChart.setData(barData);
        barChart.setFitBars(true); // Căn chỉnh các cột vào giữa

        // 3.5. Yêu cầu biểu đồ vẽ lại
        barChart.invalidate();
    }

//    // Phương thức tạo và nạp dữ liệu mẫu
//    private void loadBarChartData() {
//        // 3.1. Tạo một danh sách các điểm dữ liệu (BarEntry)
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(0, 40000)); // (x=0, y=40000)
//        entries.add(new BarEntry(1, 80000)); // (x=1, y=80000)
//        entries.add(new BarEntry(2, 60000)); // (x=2, y=60000)
//        entries.add(new BarEntry(3, 20000)); // (x=3, y=20000)
//        entries.add(new BarEntry(4, 70000)); // (x=4, y=70000)
//
//        // 3.2. Tạo một BarDataSet từ danh sách entries
//        BarDataSet dataSet = new BarDataSet(entries, "Chi tiêu"); // Label cho bộ dữ liệu này
//
//        // Tùy chỉnh màu sắc cho cột (dùng màu xanh dương đơn giản)
//        dataSet.setColor(Color.rgb(102, 178, 255));
//        dataSet.setValueTextSize(10f); // Kích thước chữ của giá trị
//        dataSet.setValueTextColor(Color.BLACK); // Màu chữ của giá trị
//
//        // 3.3. Tạo BarData để chứa tất cả các BarDataSet
//        BarData barData = new BarData(dataSet);
//        barData.setBarWidth(0.9f); // Đặt độ rộng của cột (0.9f là 90% không gian)
//
//        // 3.4. Nạp BarData vào BarChart
//        barChart.setData(barData);
//        barChart.setFitBars(true); // Căn chỉnh các cột vào giữa
//
//        // 3.5. Yêu cầu biểu đồ vẽ lại
//        barChart.invalidate();
//    }
}