package com.example.qlchitieu.Activites;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlchitieu.R;
import com.nafis.bottomnavigation.NafisBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HomeActivity extends AppCompatActivity {
    NafisBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
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

        bottomNavigation.show(1,true);

        bottomNavigation.setOnShowListener(new Function1<NafisBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(NafisBottomNavigation.Model model) {

                if (model.getId() == 1) {

                } else if (model.getId() == 2) {

                } else if (model.getId() == 3) {

                } else if (model.getId() == 4) {

                } else if (model.getId() == 5) {

                }
                return null;
            }
        });
    }
}