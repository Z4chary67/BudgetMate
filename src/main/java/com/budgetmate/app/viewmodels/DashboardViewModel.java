package com.budgetmate.app.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import com.budgetmate.app.models.Budget;
import com.budgetmate.app.models.SavingGoal;
import com.budgetmate.app.models.Transaction;
import com.budgetmate.app.repositories.BudgetRepository;
import com.budgetmate.app.repositories.SavingGoalRepository;
import com.budgetmate.app.repositories.TransactionRepository;
import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private final TransactionRepository transactionRepo;
    private final BudgetRepository budgetRepo;
    private final SavingGoalRepository goalRepo;

    private final LiveData<Double> totalIncome;
    private final LiveData<Double> totalExpenses;
    private final LiveData<List<Transaction>> recentTransactions;
    private final LiveData<List<SavingGoal>> recentGoals;
    private final MediatorLiveData<Double> totalBalance = new MediatorLiveData<>();

    public DashboardViewModel(Application application) {
        super(application);
        transactionRepo = new TransactionRepository(application);
        budgetRepo = new BudgetRepository(application);
        goalRepo = new SavingGoalRepository(application);

        totalIncome = transactionRepo.getTotalIncome();
        totalExpenses = transactionRepo.getTotalExpenses();
        recentTransactions = transactionRepo.getRecentTransactions();
        recentGoals = goalRepo.getRecentGoals();

        // Compute balance = income - expenses
        totalBalance.addSource(totalIncome, income -> computeBalance());
        totalBalance.addSource(totalExpenses, expenses -> computeBalance());
    }

    private void computeBalance() {
        Double income = totalIncome.getValue();
        Double expenses = totalExpenses.getValue();
        double i = income != null ? income : 0;
        double e = expenses != null ? expenses : 0;
        totalBalance.setValue(i - e);
    }

    public LiveData<Double> getTotalIncome() { return totalIncome; }
    public LiveData<Double> getTotalExpenses() { return totalExpenses; }
    public LiveData<Double> getTotalBalance() { return totalBalance; }
    public LiveData<List<Transaction>> getRecentTransactions() { return recentTransactions; }
    public LiveData<List<SavingGoal>> getRecentGoals() { return recentGoals; }

    public void addTransaction(Transaction transaction) {
        transactionRepo.insert(transaction);
    }
}
