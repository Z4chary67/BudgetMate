package com.budgetmate.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "budgets")
public class Budget {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String category;    // Housing, Food & Dining, Transportation, Education, Health, Subscriptions, Utilities, Shopping, Other
    private String customLabel; // used when category = "Other"
    private double limitAmount;
    private double spentAmount;
    private int month;          // 1-12
    private int year;

    public Budget(String category, String customLabel, double limitAmount, int month, int year) {
        this.category = category;
        this.customLabel = customLabel;
        this.limitAmount = limitAmount;
        this.spentAmount = 0;
        this.month = month;
        this.year = year;
    }

    public boolean isExceeded() {
        return spentAmount > limitAmount;
    }

    public double getRemainingAmount() {
        return limitAmount - spentAmount;
    }

    public int getProgressPercent() {
        if (limitAmount == 0) return 0;
        return (int) Math.min((spentAmount / limitAmount) * 100, 100);
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getCustomLabel() { return customLabel; }
    public void setCustomLabel(String customLabel) { this.customLabel = customLabel; }
    public double getLimitAmount() { return limitAmount; }
    public void setLimitAmount(double limitAmount) { this.limitAmount = limitAmount; }
    public double getSpentAmount() { return spentAmount; }
    public void setSpentAmount(double spentAmount) { this.spentAmount = spentAmount; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
}
