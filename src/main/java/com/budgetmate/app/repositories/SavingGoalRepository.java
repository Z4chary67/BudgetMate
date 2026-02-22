package com.budgetmate.app.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.budgetmate.app.database.BudgetMateDatabase;
import com.budgetmate.app.database.SavingGoalDao;
import com.budgetmate.app.models.SavingGoal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavingGoalRepository {

    private final SavingGoalDao goalDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SavingGoalRepository(Application application) {
        goalDao = BudgetMateDatabase.getInstance(application).savingGoalDao();
    }

    public void insert(SavingGoal goal) {
        executor.execute(() -> goalDao.insert(goal));
    }

    public void update(SavingGoal goal) {
        executor.execute(() -> goalDao.update(goal));
    }

    public void delete(SavingGoal goal) {
        executor.execute(() -> goalDao.delete(goal));
    }

    public LiveData<List<SavingGoal>> getAllGoals() {
        return goalDao.getAllGoals();
    }

    public LiveData<List<SavingGoal>> getRecentGoals() {
        return goalDao.getRecentGoals();
    }
}
