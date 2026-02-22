package com.budgetmate.app.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.budgetmate.app.database.BudgetMateDatabase;
import com.budgetmate.app.database.BudgetDao;
import com.budgetmate.app.models.Budget;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BudgetRepository {

    private final BudgetDao budgetDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public BudgetRepository(Application application) {
        budgetDao = BudgetMateDatabase.getInstance(application).budgetDao();
    }

    public void insert(Budget budget) {
        executor.execute(() -> budgetDao.insert(budget));
    }

    public void update(Budget budget) {
        executor.execute(() -> budgetDao.update(budget));
    }

    public void delete(Budget budget) {
        executor.execute(() -> budgetDao.delete(budget));
    }

    public LiveData<List<Budget>> getBudgetsByMonth(int month, int year) {
        return budgetDao.getBudgetsByMonth(month, year);
    }

    public LiveData<List<Budget>> getAllBudgets() {
        return budgetDao.getAllBudgets();
    }
}
