package com.example.qlchitieu.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qlchitieu.data.db.DBHelper;
import com.example.qlchitieu.data.db.dao.UserDAO;
import com.example.qlchitieu.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController extends BaseController<User> {
    public UserController(Context context){
        super(new UserDAO(DBHelper.getInstance(context).getWritableDatabase()));
    }

    // Handle Login in local
    public User loginLocal(String username,String password){
        List<User> users = dao.getAll();
        for(User u : users){
            if(u.getUsername().equals(username) && u.getPassword().equals(password)) return u;
        }
        return null;
    }

    // Check email exists
    public boolean isEmailExists(String email){
        List<User> users = dao.getAll();
        for(User u : users){
            if(u.getEmail().equals(email)) return true;
        }
        return false;
    }

    // Login with firebase
    public void loginWithFirebase(String email, LoginCallback callback){
        firebaseDB.collection("users")
                .whereEqualTo("email",email)
                .get()
                .addOnSuccessListener(querySnapshot ->{
                    if(!querySnapshot.isEmpty()){
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
                        User user = doc.toObject(User.class);
                        if(user != null){
                            if(isEmailExists(user.getEmail())) dao.insert(user);
                        }
                        callback.onSuccess(user);
                    }else{
                        callback.onFailure("User not found");
                    }
                }).addOnFailureListener(e -> callback.onFailure("Error: " + e.getMessage()));
    }

    // Register
    public void register(User user, Context context){
        Map<String,Object> data = new HashMap<>();
        data.put("name",user.getName());
        data.put("age",user.getAge());
        data.put("email",user.getEmail());
        data.put("username",user.getUsername());
        data.put("password",user.getPassword());

        firebaseDB.collection("users")
                .add(data)
                .addOnSuccessListener(doc->{
                    dao.insert(user);
                    Toast.makeText(context, "Register success", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e->{
                    Toast.makeText(context, "Register failed error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FIREBASE","Error Register Firebase: ", e);
                });
    }

    public User getUserByEmail(String email) {
        List<User> users = dao.getAll();
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    // UserController.java
    public void handleGoogleLogin(FirebaseUser firebaseUser, Context context) {
        User user = new User();
        user.setName(firebaseUser.getDisplayName());
        user.setEmail(firebaseUser.getEmail());
        user.setUsername(firebaseUser.getEmail()); // hoặc UID
        user.setPassword("GOOGLE_USER"); // vì user Google không có password
        user.setAge("N/A");

        // Lưu vào SQLite
        dao.insert(user);

        // Lưu lên Firestore
        firebaseDB.collection("users")
                .document(firebaseUser.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d("FIREBASE", "User Google saved successfully"))
                .addOnFailureListener(e -> Log.e("FIREBASE", "Failed to save Google user", e));
    }


    /**
     * Callback interface login async
     */
    public interface LoginCallback{
        void onSuccess(User user);
        void onFailure(String message);
    }
}
