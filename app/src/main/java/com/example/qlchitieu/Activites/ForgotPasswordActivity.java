package com.example.qlchitieu.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlchitieu.databinding.ActivityForgotpassBinding;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotpassBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotpassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvBackToLogin.setOnClickListener(v -> backToSignin());

        binding.btnSend.setOnClickListener(v -> sendPasswordResetEmail());

    }

    private void sendPasswordResetEmail(){
        if(binding.etEmail.getText().toString().equals("dung@gmail.com")){
            Toast.makeText(ForgotPasswordActivity.this, "Send email success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ForgotPasswordActivity.this, SigninActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(ForgotPasswordActivity.this, "Send email failed", Toast.LENGTH_SHORT).show();
    }

    private void backToSignin()
    {
        finish();
    }
}