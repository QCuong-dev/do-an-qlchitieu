package com.example.qlchitieu.Activites;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.qlchitieu.Fragment.ChatboxFragment;
import com.example.qlchitieu.Fragment.SettingFragment;
import com.example.qlchitieu.Fragment.CalendarFragment;
import com.example.qlchitieu.Fragment.HomeFragment;
import com.example.qlchitieu.R;
import com.nafis.bottomnavigation.NafisBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    NafisBottomNavigation bottomNavigation;
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
        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new NafisBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNavigation.add(new NafisBottomNavigation.Model(2, R.drawable.ic_calendar));
        bottomNavigation.add(new NafisBottomNavigation.Model(3, R.drawable.ic_chatbox));
        bottomNavigation.add(new NafisBottomNavigation.Model(4, R.drawable.ic_grid));

        bottomNavigation.show(1, true);

        bottomNavigation.setOnShowListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {
                Fragment fragment =null;
                if (model.getId() == 1) {
                    fragment = new HomeFragment();

                } else if (model.getId() == 2) {
                    fragment =new CalendarFragment();
                } else if (model.getId() == 3) {
                    fragment = new ChatboxFragment();
                } else if (model.getId() == 4) {
                    fragment = new SettingFragment();
                }

                if (fragment != null) {
                    LoadAndReplaceFragment(fragment);
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


    private void LoadAndReplaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

}