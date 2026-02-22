package com.budgetmate.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.budgetmate.app.databinding.ActivitySignupBinding;
import com.budgetmate.app.utils.PinManager;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private PinManager pinManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pinManager = new PinManager(this);

        binding.btnSignup.setOnClickListener(v -> attemptSignup());
        binding.tvGoToLogin.setOnClickListener(v -> finish());
    }

    private void attemptSignup() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        pinManager.saveUserName(name);
        pinManager.setLoggedIn(true);

        // Go to PIN setup after registration
        startActivity(new Intent(this, PinSetupActivity.class));
        finish();
    }
}
