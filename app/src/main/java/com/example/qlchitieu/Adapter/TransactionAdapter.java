package com.example.qlchitieu.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlchitieu.R;
import com.example.qlchitieu.Transaction;
import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactionList;
    private Context context;
    private OnItemLongClickListener onItemLongClickListener;
    private static final DecimalFormat formatter = new DecimalFormat("#,### VND");

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, Transaction transaction);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }
    public TransactionAdapter(List<Transaction> transactionList, Context context) {
        this.transactionList = transactionList;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);

        holder.tvCategoryName.setText(transaction.getCategoryName());
        holder.ivCategoryIcon.setImageResource(transaction.getCategoryIconResId());

        String formattedAmount = formatter.format(transaction.getAmount());

        if (transaction.getType().equals("expense")) {
            holder.tvAmount.setText("-" + formattedAmount);
            holder.tvAmount.setTextColor(Color.RED);
            // Bạn cũng có thể set màu nền/màu icon tại đây nếu muốn
            // holder.iconContainer.setCardBackgroundColor(...);
            // holder.ivCategoryIcon.setColorFilter(...);
        } else {
            holder.tvAmount.setText("+" + formattedAmount);
            holder.tvAmount.setTextColor(Color.parseColor("#008000")); // Màu xanh lá
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    // Sử dụng getAdapterPosition() để lấy vị trí chính xác
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        onItemLongClickListener.onItemLongClick(currentPosition, transactionList.get(currentPosition));
                        return true; // Trả về true để consume (xử lý) sự kiện
                    }
                }
                return false; // Trả về false nếu không xử lý
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void filterList(List<Transaction> filteredList) {
        this.transactionList = filteredList;
        notifyDataSetChanged(); // Thông báo cho RecyclerView vẽ lại
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryIcon;
        TextView tvCategoryName;
        TextView tvAmount;
        MaterialCardView iconContainer;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            iconContainer = itemView.findViewById(R.id.icon_container);
        }
    }
}
