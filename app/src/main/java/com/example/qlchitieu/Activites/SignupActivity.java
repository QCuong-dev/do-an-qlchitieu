package com.example.qlchitieu.Activites;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qlchitieu.data.db.firebase.BaseFirebase;
import com.example.qlchitieu.databinding.ActivitySignupBinding;
import com.example.qlchitieu.controller.UserController;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userController = new UserController(this);

        binding.tvBackToLogin.setOnClickListener(v -> btnToLogin());

        binding.btnSignup.setOnClickListener(v -> btnSignup());
    }

    private void btnSignup(){
        // Check validated
        String email = binding.etEmail.getText().toString();
        String fullName = binding.etFullName.getText().toString();
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etConfirmPassword.getText().toString();

        userController.register(fullName, email, password, new BaseFirebase.DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                Toast.makeText(SignupActivity.this, data, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(String message) {
                Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void btnToLogin(){
        finish();
    }
}