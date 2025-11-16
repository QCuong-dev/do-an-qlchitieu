package com.example.qlchitieu.Adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlchitieu.CalendarViewHolder;
import com.example.qlchitieu.R;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {

    private final ArrayList<String> daysOfMonth;

    private final OnItemListener onItemListener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    private final LocalDate today;
    private final LocalDate selectedDate; // Tháng/năm đang được hiển thị

    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener, LocalDate selectedDate) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.selectedDate = selectedDate;
        this.today = LocalDate.now();
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
        String dayText = daysOfMonth.get(position);
        holder.dayOfMonth.setText(dayText);

        if (dayText.equals("")) {
            // Ô trống
            holder.dayOfMonth.setBackground(null);
            holder.itemView.setClickable(false);
        } else {
            // Ô có ngày
            holder.itemView.setClickable(true);
            int day = Integer.parseInt(dayText);

            // 1. Kiểm tra xem có phải "hôm nay" không
            boolean isToday = today.getYear() == selectedDate.getYear() &&
                    today.getMonth() == selectedDate.getMonth() &&
                    today.getDayOfMonth() == day;

            // 2. Kiểm tra xem có phải "đang được chọn" (click) không
            boolean isSelected = (position == selectedPosition);


            // 3. Áp dụng style (Ưu tiên "Selected" > "Today" > "Normal")
            if (isSelected) {
                // Nếu là ô ĐƯỢC CHỌN (đè lên cả style "hôm nay")
                holder.dayOfMonth.setBackgroundResource(R.drawable.selected_day_background); // Giả sử bạn có file này
                holder.dayOfMonth.setTextColor(Color.WHITE);
            }
            else if (isToday) {
                // Nếu là ô HÔM NAY (nhưng không được chọn)
                holder.dayOfMonth.setBackgroundResource(R.drawable.today_background); // File chúng ta vừa tạo
                holder.dayOfMonth.setTextColor(Color.BLACK);
            }
            else {
                // Nếu là ô BÌNH THƯỜNG
                holder.dayOfMonth.setBackground(null);
                holder.dayOfMonth.setTextColor(Color.BLACK);
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
