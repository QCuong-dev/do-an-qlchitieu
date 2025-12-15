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

    public void saveTransaction(int amount,String categoryUid, String note, String date,String time, String type, BaseFirebase.DataCallback<String> callback){
        if(walletController == null) walletController = new WalletController(context);

        Transaction transaction = new Transaction();
        String uuid = UUID.randomUUID().toString();
        String uuidUser = sharedPrefHelper.getString("uuidUser","");

        // Check had login
//        if(uuidUser.isEmpty()){
//            callback.onFailure("Vui lòng đăng nhập trước khi thực hiện giao dịch");
//            return;
//        }

        // Check had wallet
        Wallet wallet = walletController.getWalletByUserId(uuidUser);
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
            case "income":
                type = "income";
                break;
            case "tiền ra":
            case "expense":
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
        transaction.setCategory_uid(categoryUid);
        transaction.setWallet_uid(wallet.getUuid());
        transaction.setAmount(amount);
        transaction.setCreated_at(helper.getCurrentDate());
        long result = dao.insert(transaction);

        if(result <= 0){
            callback.onFailure("Lỗi khi lưu giao dịch vào CSDL");
            return;
        }
        transaction.setId((int)result);
        callback.onSuccess("Lưu giao dịch thành công");

        // Save firebase
//        fBase.addDocument(uuid, transaction, new BaseFirebase.DataCallback<String>() {
//            @Override
//            public void onSuccess(String data) {
//                callback.onSuccess("Lưu giao dịch thành công");
//            }
//
//            @Override
//            public void onFailure(String message) {
//                callback.onFailure("Lỗi khi lưu giao dịch vào FB");
//            }
//        });
    }

    public List<Transaction> getListByDate(String date){
        String uuidUser = sharedPrefHelper.getString("uuidUser","");
//        if(uuidUser.isEmpty()) return null;

        if(walletController == null) walletController = new WalletController(context);
        Wallet wallet = walletController.getWalletByUserId(uuidUser);
        if(wallet == null){
            return null;
        }

        return dao.getListByDate(date,wallet.getUuid());
    }

    public List<Transaction> getListByIdWallet(String uidWallet){
        return dao.getListByIdWallet(uidWallet);
    }

    public List<Transaction> getListByMonth(String date, boolean isGroup){
        return dao.getListByMonth(date,isGroup);
    }

    public List<Transaction> getAllHaveCategory(){
        return dao.getAllHaveCategory();
    }
    public void updateTransaction(
            int id,
            int amount,
            String categoryUid,
            String note,
            String date,
            String time,
            String type,
            BaseFirebase.DataCallback<String> callback
    ) {
        Transaction t = dao.getById(id);
        if (t == null) {
            callback.onFailure("Không tìm thấy giao dịch");
            return;
        }

        t.setAmount(amount);
        t.setCategory_uid(categoryUid);
        t.setNote(note);
        t.setType(type);
        t.setDate(helper.convertDateFormatQuery(date) + ":" + time);

        int result = dao.update(t,"id = ?", new String[]{String.valueOf(id)});
        if (result <= 0) {
            callback.onFailure("Cập nhật thất bại");
            return;
        }

        callback.onSuccess("Cập nhật thành công");
    }

    public void deleteTransaction(int id, BaseFirebase.DataCallback<String> callback) {
        Transaction t = dao.getById(id);
        if (t == null) {
            callback.onFailure("Không tìm thấy giao dịch");
            return;
        }

        int result = dao.delete("id = ?", new String[]{String.valueOf(id)});
        if (result <= 0) {
            callback.onFailure("Xoá thất bại");
            return;
        }

        callback.onSuccess("Xoá thành công");
    }

}
