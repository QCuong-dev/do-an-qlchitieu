package com.example.qlchitieu.controller;

import android.content.Context;
import android.util.Log;

import com.example.qlchitieu.data.db.DBHelper;
import com.example.qlchitieu.data.db.dao.WalletDAO;
import com.example.qlchitieu.data.db.firebase.BaseFirebase;
import com.example.qlchitieu.data.db.firebase.WalletFirebase;
import com.example.qlchitieu.helpers.SharedPrefHelper;
import com.example.qlchitieu.model.Transaction;
import com.example.qlchitieu.model.Wallet;

import java.util.UUID;

public class WalletController extends BaseController<Wallet, WalletDAO, WalletFirebase> {
    private final TransactionController transactionController;
    public WalletController(Context context) {
        super(context, new WalletDAO(DBHelper.getInstance(context).getWritableDatabase()), new WalletFirebase());
        this.transactionController = new TransactionController(context);
        // 2. Phá vỡ vòng lặp: Inject chính WalletController này vào TransactionController
        this.transactionController.setWalletController(this);
    }

    public String getWallet(){
        String uuidUser = sharedPrefHelper.getString("uuidUser","");
        if(uuidUser.isEmpty()) return "0";

        Wallet wallet = dao.getBy("user_uid",uuidUser);
        if(wallet == null) return "0";

        // Get balance from transaction
        int result = 0;
        for(Transaction t : transactionController.getListByIdWallet(wallet.getUuid())){
            if(t.getType().equals("income")){
                result += (int)t.getAmount();
            }else{
                result -= (int)t.getAmount();
            }
        }

        return helper.formatCurrency((wallet.getBalance() + result));
    }

    public String getCurrentWallet(){
        String uuidUser = sharedPrefHelper.getString("uuidUser","");
        if(uuidUser.isEmpty()) return "0";

        Wallet wallet = dao.getBy("user_uid",uuidUser);
        if(wallet == null) return "0";

        return helper.formatCurrency((wallet.getBalance()));
    }

    public void saveWallet(int balance, String currency, BaseFirebase.DataCallback<String> callback){
        Wallet wallet = new Wallet();
        String uuid = UUID.randomUUID().toString();
        String uuidUser = sharedPrefHelper.getString("uuidUser","");

        // Handle valiadate
        if(uuidUser.isEmpty()){
            callback.onFailure("Không tìm thấy User đăng nhập");
            return;
        }

        // Check user had create wallet
        if(dao.exist("user_uid",uuidUser)){
            wallet = dao.getBy("user_uid",uuidUser);
            wallet.setBalance(balance);
            // Update
            int result = dao.update(wallet,"user_uid = ?",new String[]{uuidUser});
            if(result <= 0){
                callback.onFailure("Lỗi khi cập nhật ví vào CSDL");
                return;
            }

            fBase.updateDocument(uuid, wallet, new BaseFirebase.DataCallback<Void>() {
                @Override
                public void onSuccess(Void data) {
                    callback.onSuccess("Cập nhật ví thành công");
                }

                @Override
                public void onFailure(String message) {
                    callback.onFailure("Lỗi khi cập nhật ví vào FB");
                }
            });

        }else{
            wallet.setUuid(uuid);
            wallet.setUser_uid(uuidUser);
            wallet.setWallet_name("Normal Wallet");
            wallet.setBalance(balance);
            wallet.setCurrency(currency);
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

    public Wallet getWalletByUserId(String uidUser){
        return dao.getWalletByUserId(uidUser);
    }
}
