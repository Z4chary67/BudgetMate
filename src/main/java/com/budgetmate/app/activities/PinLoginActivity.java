package com.budgetmate.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.budgetmate.app.databinding.ActivityPinLoginBinding;
import com.budgetmate.app.utils.PinManager;

public class PinLoginActivity extends AppCompatActivity {

    private ActivityPinLoginBinding binding;
    private PinManager pinManager;
    private StringBuilder pinInput = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPinLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pinManager = new PinManager(this);

        binding.btn1.setOnClickListener(v -> onDigit("1"));
        binding.btn2.setOnClickListener(v -> onDigit("2"));
        binding.btn3.setOnClickListener(v -> onDigit("3"));
        binding.btn4.setOnClickListener(v -> onDigit("4"));
        binding.btn5.setOnClickListener(v -> onDigit("5"));
        binding.btn6.setOnClickListener(v -> onDigit("6"));
        binding.btn7.setOnClickListener(v -> onDigit("7"));
        binding.btn8.setOnClickListener(v -> onDigit("8"));
        binding.btn9.setOnClickListener(v -> onDigit("9"));
        binding.btn0.setOnClickListener(v -> onDigit("0"));
        binding.btnDelete.setOnClickListener(v -> onDelete());

        updateDots();
    }

    private void onDigit(String digit) {
        if (pinInput.length() < 4) {
            pinInput.append(digit);
            updateDots();

            if (pinInput.length() == 4) {
                verifyPin();
            }
        }
    }

    private void onDelete() {
        if (pinInput.length() > 0) {
            pinInput.deleteCharAt(pinInput.length() - 1);
            updateDots();
        }
    }

    private void verifyPin() {
        if (pinManager.verifyPin(pinInput.toString())) {
            pinManager.setLoggedIn(true);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
            pinInput.setLength(0);
            updateDots();
            // Shake animation hint
            binding.pinDotsContainer.animate()
                .translationX(16f).setDuration(50)
                .withEndAction(() -> binding.pinDotsContainer.animate()
                    .translationX(-16f).setDuration(50)
                    .withEndAction(() -> binding.pinDotsContainer.animate()
                        .translationX(0f).setDuration(50).start())
                    .start())
                .start();
        }
    }

    private void updateDots() {
        int filled = pinInput.length();
        binding.dot1.setActivated(filled >= 1);
        binding.dot2.setActivated(filled >= 2);
        binding.dot3.setActivated(filled >= 3);
        binding.dot4.setActivated(filled >= 4);
    }
}
