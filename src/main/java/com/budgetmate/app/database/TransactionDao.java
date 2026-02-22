package com.budgetmate.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.budgetmate.app.models.Transaction;
import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    void insert(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    LiveData<List<Transaction>> getAllTransactions();

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    LiveData<List<Transaction>> getByType(String type);

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    LiveData<List<Transaction>> getByDateRange(long startDate, long endDate);

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'income'")
    LiveData<Double> getTotalIncome();

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense'")
    LiveData<Double> getTotalExpenses();

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'expense' AND category = :category AND date BETWEEN :startDate AND :endDate")
    double getSpentByCategory(String category, long startDate, long endDate);

    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT 5")
    LiveData<List<Transaction>> getRecentTransactions();
}
