package com.example.qlchitieu.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qlchitieu.R;
import com.example.qlchitieu.controller.UserController;
import com.example.qlchitieu.databinding.ActivitySigninBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SigninActivity extends AppCompatActivity {

    private ActivitySigninBinding binding;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 🔹 Khởi tạo Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // 🔹 Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // lấy từ google-services.json
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 🔹 Bắt sự kiện khi bấm nút đăng nhập Google
        binding.btnGoogleSignIn.setOnClickListener(v -> signInWithGoogle());

        // Handle login demo
        binding.btnSignin.setOnClickListener(v -> signinWithEmailAndPassword());

        // Handle Forgot Password
        binding.tvForgotpassword.setOnClickListener(v -> eventForgopass());

        //Handle Register
        binding.tvSignUp.setOnClickListener(v -> eventRegister());


    }

    private void eventRegister(){
        Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    private  void eventForgopass(){
        Intent intent = new Intent(SigninActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);

    }

    private void signinWithEmailAndPassword() {
        if(binding.etEmail.getText().toString().equals("dung@gmail.com") && binding.etPassword.getText().toString().equals("123")){
            Toast.makeText(SigninActivity.this, "Login success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(SigninActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                Log.e("GOOGLE_SIGNIN", "Google Sign-In failed", e);
                Toast.makeText(this, "Đăng nhập Google thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("FIREBASE_AUTH", "Đang xác thực Google token...");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            Log.d("FIREBASE_AUTH", "Đăng nhập Firebase thành công: " + firebaseUser.getEmail());

                            // 🔹 Gọi controller để xử lý lưu user (Firestore + SQLite nếu cần)
                            UserController userController = new UserController(this);
                            userController.handleGoogleLogin(firebaseUser, this);

                            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                            // 🔹 Chuyển sang MainActivity
                            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Exception e = task.getException();
                        Log.e("FIREBASE_AUTH", "Lỗi khi xác thực Firebase", e);
                        Toast.makeText(this, "Xác thực Firebase thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
