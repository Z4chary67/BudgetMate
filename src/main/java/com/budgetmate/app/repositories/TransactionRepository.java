package com.budgetmate.app.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.budgetmate.app.database.BudgetMateDatabase;
import com.budgetmate.app.database.TransactionDao;
import com.budgetmate.app.models.Transaction;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionRepository {

    private final TransactionDao transactionDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public TransactionRepository(Application application) {
        BudgetMateDatabase db = BudgetMateDatabase.getInstance(application);
        transactionDao = db.transactionDao();
    }

    public void insert(Transaction transaction) {
        executor.execute(() -> transactionDao.insert(transaction));
    }

    public void delete(Transaction transaction) {
        executor.execute(() -> transactionDao.delete(transaction));
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }

    public LiveData<List<Transaction>> getByType(String type) {
        return transactionDao.getByType(type);
    }

    public LiveData<List<Transaction>> getByDateRange(long startDate, long endDate) {
        return transactionDao.getByDateRange(startDate, endDate);
    }

    public LiveData<Double> getTotalIncome() {
        return transactionDao.getTotalIncome();
    }

    public LiveData<Double> getTotalExpenses() {
        return transactionDao.getTotalExpenses();
    }

    public LiveData<List<Transaction>> getRecentTransactions() {
        return transactionDao.getRecentTransactions();
    }
}
