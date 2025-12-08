package com.example.qlchitieu.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.qlchitieu.Fragment.ChatboxFragment;
import com.example.qlchitieu.Fragment.SettingFragment;
import com.example.qlchitieu.Fragment.CalendarFragment;
import com.example.qlchitieu.Fragment.HomeFragment;
import com.example.qlchitieu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nafis.bottomnavigation.NafisBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    NafisBottomNavigation bottomNavigation;
    FloatingActionButton fabAddTransaction;

    private HomeFragment homeFragment;
    private CalendarFragment calendarFragment;
    private ChatboxFragment chatboxFragment;
    private SettingFragment settingFragment;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        homeFragment = new HomeFragment();
        calendarFragment = new CalendarFragment();
        chatboxFragment = new ChatboxFragment();
        settingFragment = new SettingFragment();

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.container, settingFragment, "4").hide(settingFragment) // Ẩn ngay lập tức
                .add(R.id.container, chatboxFragment, "3").hide(chatboxFragment) // Ẩn ngay lập tức
                .add(R.id.container, calendarFragment, "2").hide(calendarFragment) // Ẩn ngay lập tức
                .add(R.id.container, homeFragment, "1") // Mặc định show Home
                .commit();
        activeFragment = homeFragment;

        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new NafisBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNavigation.add(new NafisBottomNavigation.Model(2, R.drawable.ic_calendar));
        bottomNavigation.add(new NafisBottomNavigation.Model(3, R.drawable.ic_chatbox));
        bottomNavigation.add(new NafisBottomNavigation.Model(4, R.drawable.ic_grid));

        bottomNavigation.show(1, true);

        fabAddTransaction = findViewById(R.id.fabAddTransaction);

        fabAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddChitieuActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigation.setOnShowListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {
                Fragment nextFragment = null;
                if (model.getId() == 1) {
                    nextFragment = homeFragment;
                } else if (model.getId() == 2) {
                    nextFragment = calendarFragment;
                } else if (model.getId() == 3) {
                    nextFragment = chatboxFragment;
                } else if (model.getId() == 4) {
                    nextFragment = settingFragment;
                }

                if (nextFragment != null && nextFragment != activeFragment) {
                    loadAndShowFragment(nextFragment); // Gọi hàm mới
                }
                if (model.getId() == 3 || model.getId() == 4) {
                    fabAddTransaction.hide(); // Tự động ẩn có hiệu ứng thu nhỏ
                } else {
                    fabAddTransaction.show(); // Tự động hiện có hiệu ứng phóng to
                }

                return null;
            }
        });

        bottomNavigation.setOnClickMenuListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {
                return null;
            }
        });


    }

    public void showBottomNavigationTab(int id, boolean animated) {
        if (bottomNavigation != null) {
            bottomNavigation.show(id, animated);
        }
    }

    private void loadAndShowFragment(Fragment nextFragment) {
        getSupportFragmentManager().beginTransaction()
                .hide(activeFragment) // Ẩn Fragment hiện tại
                .show(nextFragment) // Hiển thị Fragment mới
                .commit();
        activeFragment = nextFragment; // Cập nhật Fragment đang hoạt động
    }

}