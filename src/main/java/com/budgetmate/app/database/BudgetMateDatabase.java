package com.budgetmate.app.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.budgetmate.app.models.Budget;
import com.budgetmate.app.models.SavingGoal;
import com.budgetmate.app.models.Transaction;

@Database(
    entities = {Transaction.class, Budget.class, SavingGoal.class},
    version = 1,
    exportSchema = false
)
public abstract class BudgetMateDatabase extends RoomDatabase {

    private static volatile BudgetMateDatabase INSTANCE;

    public abstract TransactionDao transactionDao();
    public abstract BudgetDao budgetDao();
    public abstract SavingGoalDao savingGoalDao();

    public static BudgetMateDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BudgetMateDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            BudgetMateDatabase.class,
                            "budgetmate_db"
                    ).fallbackToDestructiveMigration()
                     .build();
                }
            }
        }
        return INSTANCE;
    }
}
