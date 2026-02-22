package com.budgetmate.app.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.budgetmate.app.models.Budget;
import com.budgetmate.app.repositories.BudgetRepository;
import java.util.List;

public class BudgetViewModel extends AndroidViewModel {

    private final BudgetRepository repository;

    public BudgetViewModel(Application application) {
        super(application);
        repository = new BudgetRepository(application);
    }

    public void insert(Budget budget) { repository.insert(budget); }
    public void update(Budget budget) { repository.update(budget); }
    public void delete(Budget budget) { repository.delete(budget); }

    public LiveData<List<Budget>> getBudgetsByMonth(int month, int year) {
        return repository.getBudgetsByMonth(month, year);
    }

    public LiveData<List<Budget>> getAllBudgets() {
        return repository.getAllBudgets();
    }
}
