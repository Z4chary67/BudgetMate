package com.budgetmate.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.budgetmate.app.models.SavingGoal;
import java.util.List;

@Dao
public interface SavingGoalDao {

    @Insert
    void insert(SavingGoal goal);

    @Update
    void update(SavingGoal goal);

    @Delete
    void delete(SavingGoal goal);

    @Query("SELECT * FROM saving_goals ORDER BY deadline ASC")
    LiveData<List<SavingGoal>> getAllGoals();

    @Query("SELECT * FROM saving_goals ORDER BY deadline ASC LIMIT 3")
    LiveData<List<SavingGoal>> getRecentGoals();
}
