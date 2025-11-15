package com.example.qlchitieu.Adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlchitieu.CalendarViewHolder;
import com.example.qlchitieu.R;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final ArrayList<String> daysOfMonth;

    private final OnItemListener onItemListener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
//        holder.dayOfMonth.setText(daysOfMonth.get(position));
        String dayText = daysOfMonth.get(position);
        holder.dayOfMonth.setText(dayText);

        // 2. Logic để xử lý ô trống và ô được chọn
        if(dayText.equals("")) {
            // Nếu là ô trống, ẩn đi và không cho click
            holder.itemView.setClickable(false);
            holder.dayOfMonth.setBackground(null); // Xóa mọi background
        } else {
            // Nếu là ô có ngày
            holder.itemView.setClickable(true);

            if(position == selectedPosition) {
                // Nếu là ô ĐƯỢC CHỌN
                holder.dayOfMonth.setBackgroundResource(R.drawable.selected_day_background); // Set nền xám
                holder.dayOfMonth.setTextColor(Color.WHITE); // Set chữ màu trắng
            } else {
                // Nếu là ô KHÔNG ĐƯỢC CHỌN
                holder.dayOfMonth.setBackground(null); // Xóa background (quay về mặc định)
                holder.dayOfMonth.setTextColor(Color.BLACK); // Set chữ màu đen (mặc định)
            }
        }

    }

    // 3. Thêm phương thức để Activity có thể cập nhật vị trí đã chọn
    public void setSelectedPosition(int position) {
        if (position == selectedPosition) return; // Không thay đổi nếu click vào ô cũ

        int oldPosition = selectedPosition; // Lưu lại vị trí cũ
        selectedPosition = position;      // Cập nhật vị trí mới

        // Thông báo cho adapter "vẽ" lại chỉ 2 ô đã thay đổi (hiệu quả hơn)
        if (oldPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(oldPosition);
        }
        notifyItemChanged(selectedPosition);
    }


    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }
    public interface  OnItemListener
    {
        void onItemClick(int position, String dayText);
    }
}
