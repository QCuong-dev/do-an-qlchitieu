package com.example.qlchitieu.controller;

import android.content.Context;
import android.util.Log;

import com.example.qlchitieu.data.db.DBHelper;
import com.example.qlchitieu.data.db.dao.TransactionDAO;
import com.example.qlchitieu.data.db.dao.UserDAO;
import com.example.qlchitieu.data.db.firebase.BaseFirebase;
import com.example.qlchitieu.data.db.firebase.TransactionFirebase;
import com.example.qlchitieu.data.db.firebase.UserFirebase;
import com.example.qlchitieu.model.Transaction;
import com.example.qlchitieu.model.Wallet;

import java.util.List;
import java.util.UUID;

public class TransactionController extends BaseController<Transaction, TransactionDAO, TransactionFirebase> {
    private WalletController walletController;
    private Context context;
    public TransactionController(Context context) {
        super(context, new TransactionDAO(DBHelper.getInstance(context).getWritableDatabase()), new TransactionFirebase());
        this.context = context;
    }

    public void setWalletController(WalletController walletController) {
        this.walletController = walletController;
    }

    public void saveTransaction(int amount,int categoryId, String note, String date,String time, String type, BaseFirebase.DataCallback<String> callback){
        if(walletController == null) walletController = new WalletController(context);

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
        transaction.setDate(helper.convertDateFormatQuery(date) + ":" + time);
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

    public List<Transaction> getListByDate(String date){
        int idUser = sharedPrefHelper.getInt("idUser",0);
        if(idUser == 0) return null;

        if(walletController == null) walletController = new WalletController(context);
        Wallet wallet = walletController.getWalletByUserId(idUser);
        if(wallet == null){
            return null;
        }

        return dao.getListByDate(date,wallet.getId());
    }

    public List<Transaction> getListByIdWallet(int idWallet){
        return dao.getListByIdWallet(idWallet);
    }

    public List<Transaction> getListByMonth(String date, boolean isGroup){
        return dao.getListByMonth(date,isGroup);
    }
}
