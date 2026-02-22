package com.budgetmate.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saving_goals")
public class SavingGoal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private double targetAmount;
    private double savedAmount;
    private long deadline;      // epoch millis
    private String emoji;

    public SavingGoal(String name, double targetAmount, long deadline, String emoji) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.savedAmount = 0;
        this.deadline = deadline;
        this.emoji = emoji;
    }

    public int getProgressPercent() {
        if (targetAmount == 0) return 0;
        return (int) Math.min((savedAmount / targetAmount) * 100, 100);
    }

    public double getRemainingAmount() {
        return targetAmount - savedAmount;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }
    public double getSavedAmount() { return savedAmount; }
    public void setSavedAmount(double savedAmount) { this.savedAmount = savedAmount; }
    public long getDeadline() { return deadline; }
    public void setDeadline(long deadline) { this.deadline = deadline; }
    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }
}
