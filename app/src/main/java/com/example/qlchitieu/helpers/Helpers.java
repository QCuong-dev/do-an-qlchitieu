package com.example.qlchitieu.helpers;

import android.content.Context;
import android.util.Patterns;

import com.example.qlchitieu.Activites.MainActivity;
import com.example.qlchitieu.Fragment.HomeFragment;
import com.example.qlchitieu.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helpers {
    private final SharedPrefHelper sharedPrefHelper;
    private final FirebaseAuth mAuth;
    private final GoogleSignInClient mGoogleSignInClient;
    private final Context context;
    public Helpers(Context context){
        this.context = context;
        sharedPrefHelper = new SharedPrefHelper(context);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void handleLogout(){
        // Remove login Google
        mAuth.signOut();
        mGoogleSignInClient.signOut();

        // Remove data shared
        sharedPrefHelper.clearAll();
    }

    public boolean isString(String str){
        if(str == null || str.isEmpty()) return false;

        for(char c: str.toCharArray()){
            if(Character.isDigit(c)) return false;
        }
        return true;
    }
    public boolean isEmail(String str){
        return (!str.isEmpty() || str != null) && Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    public String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    public boolean handleIsLogin(){
        boolean isLogin = sharedPrefHelper.getBoolean("isLogin",false);

        if(isLogin){
            Intent intent = new Intent(context, MainActivity.class);

            // Dọn dẹp Activity stack (quan trọng để người dùng không bấm Back quay lại Login)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            context.startActivity(intent);
            return  true;
        }
        return false;
    }
}
