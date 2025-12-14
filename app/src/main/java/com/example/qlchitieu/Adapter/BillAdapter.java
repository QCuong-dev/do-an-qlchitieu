package com.example.qlchitieu.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlchitieu.Bill;
import com.example.qlchitieu.R;
import com.example.qlchitieu.model.Transaction;

import java.text.DecimalFormat;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder>{
    private List<Transaction> billList;
    private DecimalFormat formatter = new DecimalFormat("#,### đ"); // Định dạng tiền tệ

    public BillAdapter(List<Transaction> billList) {
        this.billList = billList;
    }

    public void updateData(List<Transaction> newBillList) {
        this.billList = newBillList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillAdapter.BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Liên kết với layout item_bill.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapter.BillViewHolder holder, int position) {
        Transaction bill = billList.get(position);

        // 1. Xử lý Logic Nhóm Ngày (Header Date)
        // Nếu item này có ngày trùng với item trước đó -> Ẩn Header ngày đi
        if (position > 0 && bill.getDate().equals(billList.get(position - 1).getDate())) {
            holder.layoutDateHeader.setVisibility(View.GONE);
            // Có thể chỉnh margin top nhỏ lại cho các item con
        } else {
            holder.layoutDateHeader.setVisibility(View.VISIBLE);
            holder.tvDay.setText(bill.getDate());
            holder.tvMonth.setText(bill.getDayOfWeek()); // Tái sử dụng id tvMonth cho Thứ
        }

        // 2. Hiển thị thông tin cơ bản
        holder.tvName.setText(bill.getCategory_name());
        holder.tvNote.setText(bill.getNote());
        holder.imgCategory.setImageResource(bill.getIconResId());

        // 3. Xử lý Màu sắc & Dấu +/- cho Tiền
        if (bill.getType().equals("income")) {
            holder.tvAmount.setText("+ " + formatter.format(bill.getAmount()));
            holder.tvAmount.setTextColor(Color.parseColor("#4CAF50")); // Màu Xanh
        } else {
            holder.tvAmount.setText("- " + formatter.format(bill.getAmount()));
            holder.tvAmount.setTextColor(Color.parseColor("#F44336")); // Màu Đỏ
        }
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonth, tvDay, tvName, tvAmount, tvNote;
        ImageView imgCategory;
        LinearLayout layoutDateHeader; // Layout chứa ngày tháng để ẩn hiện

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ View (Đảm bảo ID khớp với file item_bill.xml mới)
            tvDay = itemView.findViewById(R.id.tvDay);
            tvMonth = itemView.findViewById(R.id.tvMonth); // Trong XML mới đây là Thứ
            tvName = itemView.findViewById(R.id.tvBillName);
            tvAmount = itemView.findViewById(R.id.tvBillAmount);
            tvNote = itemView.findViewById(R.id.tvNote);
            imgCategory = itemView.findViewById(R.id.imgCategory);

            // Layout bao quanh header ngày tháng (bạn cần thêm id này vào thẻ LinearLayout đầu tiên trong item_bill.xml nếu chưa có)
            // Ví dụ trong XML: <LinearLayout android:id="@+id/layoutDateHeader" ... >
            layoutDateHeader = (LinearLayout) tvDay.getParent();
        }
    }
}
