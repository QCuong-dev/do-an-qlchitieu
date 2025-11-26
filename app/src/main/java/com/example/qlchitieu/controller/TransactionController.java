package com.example.qlchitieu.controller;

import android.content.Context;

import com.example.qlchitieu.data.db.DBHelper;
import com.example.qlchitieu.data.db.dao.TransactionDAO;
import com.example.qlchitieu.data.db.dao.UserDAO;
import com.example.qlchitieu.data.db.firebase.BaseFirebase;
import com.example.qlchitieu.data.db.firebase.TransactionFirebase;
import com.example.qlchitieu.data.db.firebase.UserFirebase;
import com.example.qlchitieu.model.Transaction;
import com.example.qlchitieu.model.Wallet;

import java.util.UUID;

public class TransactionController extends BaseController<Transaction, TransactionDAO, TransactionFirebase> {
    private WalletController walletController;
    public TransactionController(Context context) {
        super(context, new TransactionDAO(DBHelper.getInstance(context).getWritableDatabase()), new TransactionFirebase());
        walletController = new WalletController(context);
    }

    public void saveTransaction(int amount,int categoryId, String note, String date,String time, String type, BaseFirebase.DataCallback<String> callback){
        Transaction transaction = new Transaction();
        String uuid = UUID.randomUUID().toString();
        int idUser = sharedPrefHelper.getInt("idUser",0);

        // Check had login
        if(idUser <= 0){
            callback.onFailure("Vui lòng đăng nhập trước khi thực hiện giao dịch");
            return;
        }

        // Check had wallet
        Wallet wallet = walletController.getWalletByUserId(idUser);
        if(wallet == null){
            callback.onFailure("Vui lòng tạo ví trước khi thực hiện giao dịch");
            return;
        }

        // Valiadate
        if(amount <= 0){
            callback.onFailure("Số tiền phải lớn hơn 0");
            return;
        }
        switch (type.toLowerCase()){
            case "tiền vào":
                type = "income";
                break;
            case "tiền ra":
                type = "expense";
                break;
            default:
                callback.onFailure("Loại giao dịch không hợp lệ");
                return;
        }

        // Save local
        transaction.setUuid(uuid);
        transaction.setType(type);
        transaction.setNote(note);
        transaction.setDate(date + ":" + time);
        transaction.setCategory_id(categoryId);
        transaction.setWallet_id(wallet.getId());
        transaction.setAmount(amount);
        transaction.setCreated_at(helper.getCurrentDate());
        long result = dao.insert(transaction);

        if(result <= 0){
            callback.onFailure("Lỗi khi lưu giao dịch vào CSDL");
            return;
        }
        transaction.setId((int)result);

        // Save firebase
        fBase.addDocument(uuid, transaction, new BaseFirebase.DataCallback<String>() {
            @Override
            public void onSuccess(String data) {
                callback.onSuccess("Lưu giao dịch thành công");
            }

            @Override
            public void onFailure(String message) {
                callback.onFailure("Lỗi khi lưu giao dịch vào FB");
            }
        });
    }
}
