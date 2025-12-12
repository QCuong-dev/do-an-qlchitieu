package com.example.qlchitieu.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlchitieu.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CategoryStatAdapter extends RecyclerView.Adapter<CategoryStatAdapter.ViewHolder> {

    public static class CategoryStat {
        public String name;
        public double amount;
        public float percentage;
        public int color;

        public CategoryStat(String name, double amount, float percentage, int color) {
            this.name = name;
            this.amount = amount;
            this.percentage = percentage;
            this.color = color;
        }
    }
    private List<CategoryStat> list;

    public CategoryStatAdapter(List<CategoryStat> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Bước 1: Lấy LayoutInflater từ context của ViewGroup (parent)
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(parent.getContext());

        // Bước 2: "Thổi phồng" layout cho một item.
        // Hãy đảm bảo bạn có một file layout tên là "item_category_stat.xml" (hoặc tên khác) trong thư mục res/layout.
        View view = inflater.inflate(R.layout.item_category_stat, parent, false);

        // Bước 3: Tạo và trả về một ViewHolder mới từ view vừa tạo
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryStat item = list.get(position);
        holder.tvName.setText(item.name);
        holder.tvPercent.setText(String.format(Locale.getDefault(), "%.1f%%", item.percentage));

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.tvAmount.setText(formatter.format(item.amount) + " đ");

        holder.viewColor.setBackgroundColor(item.color);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPercent, tvAmount;
        View viewColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            tvPercent = itemView.findViewById(R.id.tvPercentage);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            viewColor = itemView.findViewById(R.id.viewColorIndicator);
        }
    }
}
