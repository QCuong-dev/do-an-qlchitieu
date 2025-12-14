package com.example.qlchitieu;

public class Bill {

    private String dayOfWeek;  // Ví dụ: "Thứ 5"
    private String date;       // Ví dụ: "10/10/2024"
    private String name;       // Tên: "Đi chợ siêu thị"
    private String note;       // Ghi chú: "Mua đồ ăn"
    private long amount;       // Số tiền: 600000 (Dùng long để tính toán)
    private boolean isIncome;  // true = Thu (Xanh), false = Chi (Đỏ)
    private int iconResId;     // Resource ID của icon (R.drawable.ic_bill)

    public Bill(String dayOfWeek, String date, String name, String note, long amount, boolean isIncome, int iconResId) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.name = name;
        this.note = note;
        this.amount = amount;
        this.isIncome = isIncome;
        this.iconResId = iconResId;
    }

    // Getter methods
    public String getDayOfWeek() { return dayOfWeek; }
    public String getDate() { return date; }
    public String getName() { return name; }
    public String getNote() { return note; }
    public long getAmount() { return amount; }
    public boolean isIncome() { return isIncome; }
    public int getIconResId() { return iconResId; }
}
