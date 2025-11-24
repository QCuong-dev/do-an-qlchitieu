package com.example.qlchitieu.controller;

import android.content.Context;

import com.example.qlchitieu.data.db.DBHelper;
import com.example.qlchitieu.data.db.dao.WalletDAO;
import com.example.qlchitieu.data.db.firebase.BaseFirebase;
import com.example.qlchitieu.data.db.firebase.WalletFirebase;
import com.example.qlchitieu.helpers.SharedPrefHelper;
import com.example.qlchitieu.model.Wallet;

import java.util.UUID;

public class WalletController extends BaseController<Wallet, WalletDAO, WalletFirebase> {
    private SharedPrefHelper sharedPrefHelper;
    public WalletController(Context context) {
        super(new WalletDAO(DBHelper.getInstance(context).getWritableDatabase()), new WalletFirebase());
        sharedPrefHelper = new SharedPrefHelper(context);
    }

    public void saveWallet(int balance, String currency, BaseFirebase.DataCallback<String> callback){
        Wallet wallet = new Wallet();
        String uuid = UUID.randomUUID().toString();
        int userId = sharedPrefHelper.getInt("idUser",0);

        // Handle valiadate
        if(userId == 0){
            callback.onFailure("Không tìm thấy User đăng nhập");
            return;
        }

        wallet.setUuid(uuid);
        wallet.setUser_id(userId);
        wallet.setWallet_name("Normal Wallet");
        wallet.setBalance(balance);
        wallet.setCurrency(currency);

        // Check user had create wallet
        if(dao.exist("user_id",String.valueOf(userId))){
            // Update
            int result = dao.update(wallet,"user_id",new String[]{String.valueOf(userId)});
            if(result <= 0){
                callback.onFailure("Lỗi khi cập nhật ví vào CSDL");
                return;
            }
            wallet.setId(result);

            fBase.addDocument(uuid, wallet, new BaseFirebase.DataCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    callback.onSuccess("Cập nhật ví thành công");
                }

                @Override
                public void onFailure(String message) {
                    callback.onFailure("Lỗi khi cập nhật ví vào FB");
                }
            });

        }else{
            // Save
            long result = dao.insert(wallet);
            if(result <= 0) {
                callback.onFailure("Lỗi khi lưu ví vào CSDL");
                return;
            }
            wallet.setId((int)result);

            fBase.addDocument(uuid, wallet, new BaseFirebase.DataCallback<String>() {
                @Override
                public void onSuccess(String data) {
                    callback.onSuccess("Lưu ví thành công");
                }

                @Override
                public void onFailure(String message) {
                    callback.onFailure("Lỗi khi lưu ví vào FB");
                }
            });
        }
    }
}
