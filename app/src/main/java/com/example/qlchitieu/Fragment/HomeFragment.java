package com.example.qlchitieu.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlchitieu.Activites.AddChitieuActivity;
import com.example.qlchitieu.Activites.BienDongActivity;
import com.example.qlchitieu.Activites.GiaoDichDinhKyActivity;
import com.example.qlchitieu.Activites.HoTroActivity;
import com.example.qlchitieu.Activites.SigninActivity;
import com.example.qlchitieu.Activites.SignupActivity;
import com.example.qlchitieu.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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

    private ImageView  btnHeadset, btnClose;
    private LinearLayout btnBienDongThuChi, btnGiaoDichDinhKy, btnTienIchKhac;

    // === CÁC BIẾN MỚI CHO LOGIC THÁNG ===
    private ImageView ivMonthPrev, ivMonthNext;
    private TextView tvCurrentMonth, tvTotalExpense, tvTotalIncome;

    private Calendar currentCalendar; // Biến để theo dõi tháng đang chọn
    private SimpleDateFormat monthFormatter; // Định dạng "Tháng 11/2025"
    private NumberFormat currencyFormatter; // Định dạng tiền "1.000.000"

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 1. Ánh xạ LineChart từ file XML
        barChart = view.findViewById(R.id.chart);

        btnThemGD = view.findViewById(R.id.btnThemGD);

        btnHeadset = view.findViewById(R.id.btn_headset);
        btnBienDongThuChi = view.findViewById(R.id.btnThuChiTheoThang);
        btnGiaoDichDinhKy = view.findViewById(R.id.btnGiaoDichDinhKy);
        btnTienIchKhac = view.findViewById(R.id.btnTienichkhac);

        // === ÁNH XẠ CÁC VIEW MỚI ===
        ivMonthPrev = view.findViewById(R.id.ivMonthPrev);
        ivMonthNext = view.findViewById(R.id.ivMonthNext);
        tvCurrentMonth = view.findViewById(R.id.tvCurrentMonth);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);

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

        setupBarChart();
        loadBarChartData();

        setupMonthNavigation();

        updateDashboard();
    }

    private void setupMonthNavigation() {
        ivMonthPrev.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1); // Lùi 1 tháng
            updateDashboard(); // Cập nhật lại giao diện
        });

        ivMonthNext.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1); // Tăng 1 tháng
            updateDashboard(); // Cập nhật lại giao diện
        });
    }

    // === HÀM MỚI: CẬP NHẬT GIAO DIỆN KHI ĐỔI THÁNG ===
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

        // 2. Lấy dữ liệu (THAY THẾ BẰNG LOGIC CỦA BẠN)
        int year = currentCalendar.get(Calendar.YEAR);
        int month = currentCalendar.get(Calendar.MONTH); // Lưu ý: Tháng 0-indexed (0=Tháng 1)

        // Dùng hàm dữ liệu giả để minh họa
        long[] data = getFakeDataForMonth(year, month);
        long income = data[0];
        long expense = data[1];

        // 3. Cập nhật TextView thu/chi
        tvTotalIncome.setText(currencyFormatter.format(income));
        tvTotalExpense.setText(currencyFormatter.format(expense));

        // 4. (Tùy chọn) Tải lại biểu đồ BarChart với dữ liệu của tháng mới
        // loadBarChartData(year, month);
    }

    // === HÀM MỚI: DỮ LIỆU GIẢ ĐỂ MINH HỌA ===
    // (Bạn sẽ thay thế hàm này bằng logic thật)
    private long[] getFakeDataForMonth(int year, int month) {
        // Tạo dữ liệu giả dựa trên tháng
        long fakeIncome = 10000000 + (month * 500000) + (year - 2025) * 1000000;
        long fakeExpense = 1000000 + (month * 100000) + (year - 2025) * 50000;
        return new long[]{fakeIncome, fakeExpense};
    }


    private void addClickEvents() {


        btnHeadset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HoTroActivity.class);
                startActivity(intent);
            }
        });

        // Quick Actions
        btnBienDongThuChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BienDongActivity.class);
                startActivity(intent);
            }
        });

        btnGiaoDichDinhKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // CẬP NHẬT: Mở GiaoDichDinhKyActivity
                Intent intent = new Intent(getActivity(), GiaoDichDinhKyActivity.class);
                startActivity(intent);
            }
        });

        btnTienIchKhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    SettingFragment settingFragment = new SettingFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    // Thay thế layout container của bạn (ví dụ R.id.fragment_container)
                    // bằng fragment mới và thêm vào backstack
                    fragmentTransaction.replace(R.id.container, settingFragment); // <-- THAY ID NÀY
                    fragmentTransaction.addToBackStack(null); // Để nút back quay lại HomeFragment
                    fragmentTransaction.commit();
                }
            }
        });
    }


    // Phương thức tùy chỉnh biểu đồ
    private void setupBarChart() {
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

    // Phương thức tạo và nạp dữ liệu mẫu
    private void loadBarChartData() {
        // 3.1. Tạo một danh sách các điểm dữ liệu (BarEntry)
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 40000)); // (x=0, y=40000)
        entries.add(new BarEntry(1, 80000)); // (x=1, y=80000)
        entries.add(new BarEntry(2, 60000)); // (x=2, y=60000)
        entries.add(new BarEntry(3, 20000)); // (x=3, y=20000)
        entries.add(new BarEntry(4, 70000)); // (x=4, y=70000)

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
}