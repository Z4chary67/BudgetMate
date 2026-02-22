package com.budgetmate.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.budgetmate.app.R;
import com.budgetmate.app.utils.NotificationHelper;
import com.budgetmate.app.utils.PinManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Create notification channels on first run
        NotificationHelper.createChannels(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            PinManager pinManager = new PinManager(this);

            Intent intent;
            if (!pinManager.isLoggedIn()) {
                intent = new Intent(this, LoginActivity.class);
            } else if (pinManager.hasPin()) {
                intent = new Intent(this, PinLoginActivity.class);
            } else {
                intent = new Intent(this, MainActivity.class);
            }

            startActivity(intent);
            finish();
        }, 1800);
    }
}
