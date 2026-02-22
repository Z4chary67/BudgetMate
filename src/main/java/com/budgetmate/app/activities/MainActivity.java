package com.budgetmate.app.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.budgetmate.app.R;
import com.budgetmate.app.databinding.ActivityMainBinding;
import com.budgetmate.app.utils.PinManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Navigation Component
        NavHostFragment navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        // Bottom Nav
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);

        // Drawer Menu items
        binding.navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                // Show profile fragment or dialog
            } else if (id == R.id.nav_about) {
                // Show about dialog
            } else if (id == R.id.nav_settings) {
                // Show settings
            } else if (id == R.id.nav_logout) {
                logout();
            }
            binding.drawerLayout.closeDrawers();
            return true;
        });
    }

    public void openDrawer() {
        binding.drawerLayout.openDrawer(binding.navigationView);
    }

    private void logout() {
        new PinManager(this).logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
