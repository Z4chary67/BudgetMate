package com.budgetmate.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.budgetmate.app.models.Budget;
import com.budgetmate.app.models.SavingGoal;
import java.util.List;

@Dao
public interface BudgetDao {

    @Insert
    void insert(Budget budget);

    @Update
    void update(Budget budget);

    @Delete
    void delete(Budget budget);

    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year")
    LiveData<List<Budget>> getBudgetsByMonth(int month, int year);

    @Query("SELECT * FROM budgets")
    LiveData<List<Budget>> getAllBudgets();
}

// ─────────────────────────────────────────────────────────
// Saving Goal DAO (separate file normally, combined here for brevity)
// ─────────────────────────────────────────────────────────
// Put SavingGoalDao.java as its own file in the same package
