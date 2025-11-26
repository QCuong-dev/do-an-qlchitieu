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
import com.example.qlchitieu.Activites.AddBudgetActivity;
import com.example.qlchitieu.Activites.AddEditWalletActivity;
import com.example.qlchitieu.Activites.ChangePasswordActivity;
import com.example.qlchitieu.Activites.HistoryActivity;
import com.example.qlchitieu.Activites.SigninActivity;
import com.example.qlchitieu.R;
import com.example.qlchitieu.controller.WalletController;
import com.example.qlchitieu.helpers.Helpers;
import com.example.qlchitieu.helpers.SharedPrefHelper;
import com.google.android.material.imageview.ShapeableImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private WalletController walletController;
    private Helpers helper;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Init
        helper = new Helpers(requireContext());
        walletController = new WalletController(requireContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    private TextView tvUserName, tvMonthlyAmount;
    private ShapeableImageView ivAvatar;
    private ConstraintLayout btnAccount, btnChangePassword, btnHistoryTrade, btnLogout, btnBudget,btnWallet;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        anhXaView(view);

        // 2. Gán sự kiện click cho các nút
        setClickListeners();

        // 3. (Tùy chọn) Tải dữ liệu người dùng
        loadUserData();
    }

    private void anhXaView(View view) {
        // Thông tin người dùng
        tvUserName = view.findViewById(R.id.tvUserName);
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvMonthlyAmount = view.findViewById(R.id.tvMonthlyAmount);

        // Các nút chức năng
        btnAccount = view.findViewById(R.id.btnAccount);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnHistoryTrade = view.findViewById(R.id.btnHistoryTrade);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnBudget = view.findViewById(R.id.btnBudget);
        btnWallet =  view.findViewById(R.id.btnWallet);
    }

    /**
     * Gán các trình xử lý sự kiện click cho các View.
     */
    private void setClickListeners() {
        // Sự kiện click cho nút "Tài Khoản"
        btnAccount.setOnClickListener(v -> {
            Log.d("SettingFragment", "Nút Tài Khoản được click. Mở AccountActivity.");
            // Mở màn hình AccountActivity
            Intent intent = new Intent(getActivity(), AccountActivity.class);
            startActivity(intent);
        });

        // Sự kiện click cho nút "Đổi Mật Khẩu"
        btnChangePassword.setOnClickListener(v -> {
            Log.d("SettingFragment", "Nút Đổi Mật Khẩu được click. Mở ChangePasswordActivity.");
            // Mở màn hình ChangePasswordActivity
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(intent);
        });
        // Sự kiện click cho nút "Wallet"
        btnWallet.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddEditWalletActivity.class);
            startActivity(intent);
                });
        // Sự kiện click cho nút "Budget"
        btnBudget.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddBudgetActivity.class);
            startActivity(intent);
        });
        // Sự kiện click cho nút "Lịch sử"
        btnHistoryTrade.setOnClickListener(v -> {
            Log.d("SettingFragment", "Nút Lịch sử được click. Mở HistoryActivity.");
            // Mở màn hình HistoryActivity
            Intent intent = new Intent(getActivity(), HistoryActivity.class);
            startActivity(intent);
        });

        // Sự kiện click cho nút "Đăng xuất"
        btnLogout.setOnClickListener(v -> {
            showLogoutConfirmationDialog();
        });
    }

    /**
     * Hiển thị Dialog xác nhận trước khi đăng xuất.
     */
    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản này không?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    helper.handleLogout();
                    Toast.makeText(getContext(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(getActivity(), SigninActivity.class);
                     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     startActivity(intent);
                     getActivity().finish();
                })
                .setNegativeButton("Hủy", (dialog, which) -> {
                    // Người dùng chọn "Hủy", không làm gì cả
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert) // (Tùy chọn)
                .show();
    }

    /**
     * (Tùy chọn) Tải và hiển thị dữ liệu người dùng lên UI.
     */
    private void loadUserData() {
        // TODO: Lấy dữ liệu người dùng từ SharedPreferences, Database hoặc API
        // Ví dụ:
        // String userName = ...;
        // String monthlyAmount = ...;
        // String avatarUrl = ...;

        // tvUserName.setText(userName);
        // tvMonthlyAmount.setText(monthlyAmount + " VND");
        // (Sử dụng thư viện như Glide hoặc Picasso để tải ảnh)
        // Glide.with(this).load(avatarUrl).into(ivAvatar);

        // Dữ liệu giả (từ XML) đã được hiển thị, bạn có thể cập nhật
        // nếu cần dữ liệu động
        tvUserName.setText("Nguyễn Văn A (Updated)");
        tvMonthlyAmount.setText(walletController.getWallet() + " VND");
    }
}