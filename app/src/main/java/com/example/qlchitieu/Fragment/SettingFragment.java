package com.example.qlchitieu.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qlchitieu.Activites.AccountActivity;
import com.example.qlchitieu.Activites.ChangePasswordActivity;
import com.example.qlchitieu.Activites.SigninActivity;
import com.example.qlchitieu.Activites.SyncDataActivity; // Import Activity mới
import com.example.qlchitieu.R;
import com.example.qlchitieu.controller.WalletController;
import com.example.qlchitieu.helpers.Helpers;
import com.example.qlchitieu.helpers.SharedPrefHelper;
import com.google.android.material.imageview.ShapeableImageView;

public class SettingFragment extends Fragment {

    // ... (Giữ nguyên các biến cũ) ...
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private WalletController walletController;
    private Helpers helper;
    private String mParam1;
    private String mParam2;
    private SharedPrefHelper sharedPrefHelper;

    // View Components
    private TextView tvUserName, tvMonthlyAmount;
    private ShapeableImageView ivAvatar;
    // Thêm btnSyncData vào đây
    private ConstraintLayout btnAccount, btnChangePassword, btnSyncData;

    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new Helpers(requireContext());
        walletController = new WalletController(requireContext());
        sharedPrefHelper = new SharedPrefHelper(requireContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        anhXaView(view);
//        setClickListeners();
//        loadUserData();
    }

//    private void anhXaView(View view) {
//        // Thông tin người dùng
//        tvUserName = view.findViewById(R.id.tvUserName);
//        ivAvatar = view.findViewById(R.id.ivAvatar);
//        tvMonthlyAmount = view.findViewById(R.id.tvMonthlyAmount);
//
//
//        // [MỚI] Ánh xạ nút Đồng bộ
//        btnSyncData = view.findViewById(R.id.btnSyncData);
//    }
//
//    private void setClickListeners() {
//
//        // [MỚI] Sự kiện click cho nút Đồng bộ dữ liệu
//        btnSyncData.setOnClickListener(v -> {
//            Log.d("SettingFragment", "Nút Đồng bộ được click.");
//            // Chuyển sang Activity xử lý Sync (QR code / Firebase)
//            Intent intent = new Intent(getActivity(), SyncDataActivity.class);
//            startActivity(intent);
//        });
//    }

    // ... (Giữ nguyên các hàm showLogoutConfirmationDialog và loadUserData) ...

//    private void loadUserData() {
////        tvUserName.setText(sharedPrefHelper.getString("nameUser",""));
//        tvMonthlyAmount.setText(walletController.getWallet() + " VND");
//    }
}