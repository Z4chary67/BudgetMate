package com.budgetmate.app.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.budgetmate.app.R;

public class NotificationHelper {

    public static final String CHANNEL_BUDGET = "budget_alerts";
    public static final String CHANNEL_GOALS = "goal_updates";

    private static int notifId = 1000;

    public static void createChannels(Context context) {
        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Budget Alerts Channel
        NotificationChannel budgetChannel = new NotificationChannel(
                CHANNEL_BUDGET,
                "Budget Alerts",
                NotificationManager.IMPORTANCE_HIGH
        );
        budgetChannel.setDescription("Notifications when you approach or exceed your budget limits.");
        nm.createNotificationChannel(budgetChannel);

        // Saving Goals Channel
        NotificationChannel goalsChannel = new NotificationChannel(
                CHANNEL_GOALS,
                "Saving Goal Updates",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        goalsChannel.setDescription("Progress updates for your saving goals.");
        nm.createNotificationChannel(goalsChannel);
    }

    /**
     * Shows an in-app / system notification for exceeded budget.
     */
    public static void showBudgetExceeded(Context context, String category, double exceeded) {
        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_BUDGET)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle("⚠️ Budget Exceeded!")
                .setContentText(category + " budget exceeded by ₱" + String.format("%.2f", exceeded))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        nm.notify(notifId++, builder.build());
    }

    /**
     * Shows a warning when spending reaches 80% of a budget category.
     */
    public static void showBudgetWarning(Context context, String category, int percent) {
        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_BUDGET)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle("⚡ Budget Caution")
                .setContentText("You've used " + percent + "% of your " + category + " budget.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        nm.notify(notifId++, builder.build());
    }
}
