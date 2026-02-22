package com.budgetmate.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.budgetmate.app.databinding.ActivityLoginBinding;
import com.budgetmate.app.utils.PinManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private PinManager pinManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pinManager = new PinManager(this);

        binding.btnLogin.setOnClickListener(v -> attemptLogin());

        binding.btnLoginWithPin.setOnClickListener(v -> {
            startActivity(new Intent(this, PinLoginActivity.class));
        });

        binding.tvGoToSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }

    private void attemptLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // For offline app: just validate non-empty and go to PIN setup if first time
        pinManager.setLoggedIn(true);

        if (!pinManager.hasPin()) {
            startActivity(new Intent(this, PinSetupActivity.class));
        } else {
            goToMain();
        }
        finish();
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
