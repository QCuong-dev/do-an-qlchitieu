package com.example.qlchitieu.controller;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qlchitieu.data.db.DBHelper;
import com.example.qlchitieu.data.db.dao.UserDAO;
import com.example.qlchitieu.data.db.firebase.BaseFirebase;
import com.example.qlchitieu.data.db.firebase.UserFirebase;
import com.example.qlchitieu.helpers.Helpers;
import com.example.qlchitieu.helpers.SharedPrefHelper;
import com.example.qlchitieu.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserController extends BaseController<User,UserDAO, UserFirebase> {
    public UserController(Context context){
        super(context, new UserDAO(DBHelper.getInstance(context).getWritableDatabase()),new UserFirebase());
    }

    // Handle Login
    public boolean login(String username, String password){
        // Check user google
        if(password.equals("GOOGLE_USER")) return false;

        Pair<User,Boolean> result = dao.isLogin(username,password);
        if(!result.second){
            return false;
        }

        User user = result.first;
        // Lưu vào SharedPreferences
        saveSharedPrefUser(user);
        return true;
    }

    // Check email exists
    public boolean isEmailExists(String email){
        List<User> users = dao.getAll();
        for(User u : users){
            if(u.getEmail().equals(email)) return true;
        }
        return false;
    }

    private Pair<Boolean,String> valiadateRegister(String email, String fullName, String password, String confirmPassword){
        if(fullName == null || fullName.isEmpty()){
            return new Pair<>(false,"Tên không được để trống");
        }
        if(fullName.length() >= 30){
            return new Pair<>(false,"Họ và tên không được quá 30 ký tự");
        }
        if(!helper.isString(fullName)){
            return new Pair<>(false,"Họ và tên vui lòng phải là chữ");
        }
        if(email == null || email.isEmpty()){
            return new Pair<>(false, "Email không được để trống");
        }
        if(email.length() >= 40){
            return new Pair<>(false, "Email không được quá 40 ký tự");
        }
        if(!helper.isEmail(email)){
            return new Pair<>(false, "Email không đúng định dạng");
        }
        if((password == null || password.isEmpty()) && (confirmPassword == null || confirmPassword.isEmpty())){
            return new Pair<>(false, "Mật khẩu không được để trống");
        }
        if(password.length() < 6 || password.length() >= 20 ){
            return new Pair<>(false, "Mật khẩu phải từ 6-20 ký tự");
        }
        if(!password.equals(confirmPassword)){
            return new Pair<>(false, "Mật khẩu không khớp");
        }

        return new Pair<>(true,"Xác nhận thành công");
    }

    // Register
    public void register(String name, String email, String password, BaseFirebase.DataCallback<String> callback){
        User user = new User();
        String uuid = UUID.randomUUID().toString();

        // Check valiadate
        Pair<Boolean,String> valiadated = valiadateRegister(email,name,password,password);
        if(!valiadated.first){
            callback.onFailure(valiadated.second);
            return;
        }

        // Check email exist
        if(dao.exist("email",email)){
            callback.onFailure("Email đã tồn tại");
            return;
        }

        user.setUuid(uuid);
        user.setName(name);
        user.setAge("");
        user.setEmail(email);
        user.setPassword(password);
        user.setCreatedAt(helper.getCurrentDate());
        long result = dao.insert(user);
        if(result <= 0){
            callback.onFailure("Đăng ký thất bại");
        }else{
            user.setId((int)result);

            fBase.addDocument(uuid, user, new BaseFirebase.DataCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    callback.onSuccess("Đăng ký thành công");
                }

                @Override
                public void onFailure(String message) {
                    callback.onFailure("Error when insert: " + message);
                }
            });
        }
    }

    public User getUserByEmail(String email) {
        return dao.getUserByEmail(email);
    }

    public void saveSharedPrefUser(User user){
        sharedPrefHelper.saveString("nameUser",user.getName());
        sharedPrefHelper.saveString("emailUser", user.getEmail());
        sharedPrefHelper.saveInt("idUser",user.getId());
        sharedPrefHelper.saveBoolean("isLogin",true);

        Log.d("SHARED_PREF", "User saved to SharedPreferences: " + user.getName() + " - " + user.getEmail());
    }

    public void handleChangePassword(String oldPassword,String newPassword, String confirmNewPassword, BaseFirebase.DataCallback<String> callback){
        User user;
        int userId = sharedPrefHelper.getInt("idUser",0);
        // Check user exits
        if(!dao.exist("id",String.valueOf(userId))){
            callback.onFailure("Không tìm thấy User");
            return;
        }
        user = dao.getById(userId);

        // Check valiadate
        if(!isTheFirstLoginGoogle()){
            if(user.getPassword() != oldPassword){
                callback.onFailure("Mật khẩu cũ không đúng");
                return;
            }
        }
        if(newPassword.length() <6 || newPassword.length() >= 20){
            callback.onFailure("Mật khẩu có độ dài từ 6-20");
            return;
        }
        if(!newPassword.equals(confirmNewPassword)){
            callback.onFailure("Mật khẩu mới không khớp");
            return;
        }
        user.setPassword(newPassword);
        // Save local
        dao.update(user,"id = ?", new String[]{String.valueOf(userId)});
        // Save firebase
        fBase.updateDocument(user.getUuid(), user, new BaseFirebase.DataCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                callback.onSuccess("Đổi mật khẩu thành công");
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure("Lỗi khi lưu mật khẩu vào FB");
            }
        });
    }

    public boolean isTheFirstLoginGoogle(){
        return dao.getById(sharedPrefHelper.getInt("idUser",0)).getPassword().equals("GOOGLE_USER")?true:false;
    }

    // UserController.java
    public void handleGoogleLogin(FirebaseUser firebaseUser, Context context) {
        User user = new User();
        user.setEmail(firebaseUser.getEmail());

        if(isEmailExists(user.getEmail())){
            User userEmail = getUserByEmail(user.getEmail());

            // Save for uuid get
            user.setId(userEmail.getId());
            user.setName(userEmail.getName());
            user.setUsername(userEmail.getUsername());
            user.setPassword(userEmail.getPassword());
            user.setAge(userEmail.getAge());
            user.setUuid(userEmail.getUuid());
            user.setCreatedAt(userEmail.getCreatedAt());

            // Lưu vào SharedPreferences
            saveSharedPrefUser(userEmail);
        }else{
            // Lưu vào SQLite
            user.setName(firebaseUser.getDisplayName());
            user.setUsername(firebaseUser.getEmail()); // hoặc UID
            user.setPassword("GOOGLE_USER"); // vì user Google không có password
            user.setAge("N/A");
            user.setCreatedAt(dao.getCurrentDate());
            user.setUuid(UUID.randomUUID().toString());
            long resultInsert = dao.insert(user);
            if(resultInsert > 0) {
                user.setId((int) resultInsert);
                saveSharedPrefUser(user);
            }
        }

        // Lưu lên Firestore
        firebaseDB.collection("users")
                .document(user.getUuid())
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d("FIREBASE", "User Google saved successfully"))
                .addOnFailureListener(e -> Log.e("FIREBASE", "Failed to save Google user", e));
    }
}
