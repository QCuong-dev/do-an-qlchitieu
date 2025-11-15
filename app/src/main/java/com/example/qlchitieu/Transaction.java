package com.example.qlchitieu;

import java.time.LocalDate;

public class Transaction {
    private String categoryName;
    private double amount;
    private String type; // "expense" (chi tiêu) hoặc "income" (thu nhập)
    private int categoryIconResId; // Resource ID cho icon (ví dụ: R.drawable.ic_car)
    private LocalDate date; // Ngày của giao dịch

    public Transaction(String categoryName, double amount, String type, int categoryIconResId, LocalDate date) {
        this.categoryName = categoryName;
        this.amount = amount;
        this.type = type;
        this.categoryIconResId = categoryIconResId;
        this.date = date;
    }

    // --- Getters ---
    public String getCategoryName() {
        return categoryName;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public int getCategoryIconResId() {
        return categoryIconResId;
    }

    public LocalDate getDate() {
        return date;
    }
}
