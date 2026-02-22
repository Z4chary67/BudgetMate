package com.budgetmate.app.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.budgetmate.app.models.Transaction;
import com.budgetmate.app.repositories.TransactionRepository;
import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private final TransactionRepository repository;

    public TransactionViewModel(Application application) {
        super(application);
        repository = new TransactionRepository(application);
    }

    public void insert(Transaction transaction) { repository.insert(transaction); }
    public void delete(Transaction transaction) { repository.delete(transaction); }

    public LiveData<List<Transaction>> getAllTransactions() { return repository.getAllTransactions(); }
    public LiveData<List<Transaction>> getByType(String type) { return repository.getByType(type); }
    public LiveData<List<Transaction>> getByDateRange(long start, long end) { return repository.getByDateRange(start, end); }
    public LiveData<Double> getTotalIncome() { return repository.getTotalIncome(); }
    public LiveData<Double> getTotalExpenses() { return repository.getTotalExpenses(); }
}
