package com.example.qlchitieu;

import com.github.mikephil.charting.formatter.ValueFormatter;

public class LabelFormatter extends ValueFormatter {
    private final String[] labels = new String[]{"Ăn uống", "Du lịch", "Mua sắm", "Đi lại", "Khác"};
    @Override
    public String getFormattedValue(float value) {
        // Chuyển giá trị float (0, 1, 2, ...) thành chỉ mục mảng
        int index = (int) value;

        // Đảm bảo chỉ mục nằm trong giới hạn của mảng nhãn
        if (index >= 0 && index < labels.length) {
            return labels[index];
        }

        // Trả về giá trị mặc định nếu không khớp
        return String.valueOf(index);
    }
}
