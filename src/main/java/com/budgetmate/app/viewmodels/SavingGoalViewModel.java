package com.budgetmate.app.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.budgetmate.app.models.SavingGoal;
import com.budgetmate.app.repositories.SavingGoalRepository;
import java.util.List;

public class SavingGoalViewModel extends AndroidViewModel {

    private final SavingGoalRepository repository;

    public SavingGoalViewModel(Application application) {
        super(application);
        repository = new SavingGoalRepository(application);
    }

    public void insert(SavingGoal goal) { repository.insert(goal); }
    public void update(SavingGoal goal) { repository.update(goal); }
    public void delete(SavingGoal goal) { repository.delete(goal); }

    public LiveData<List<SavingGoal>> getAllGoals() { return repository.getAllGoals(); }
    public LiveData<List<SavingGoal>> getRecentGoals() { return repository.getRecentGoals(); }

    public void addAmountToGoal(SavingGoal goal, double amount) {
        goal.setSavedAmount(goal.getSavedAmount() + amount);
        repository.update(goal);
    }
}
