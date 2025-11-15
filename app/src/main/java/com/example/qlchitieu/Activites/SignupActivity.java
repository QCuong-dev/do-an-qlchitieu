package com.example.qlchitieu.Activites;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlchitieu.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvBackToLogin.setOnClickListener(v -> btnToLogin());

        binding.btnSignup.setOnClickListener(v -> btnSignup());
    }

    private void btnSignup(){

    }
    private void btnToLogin(){
        finish();
    }
}