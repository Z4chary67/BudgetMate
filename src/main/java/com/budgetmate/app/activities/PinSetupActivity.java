package com.budgetmate.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.budgetmate.app.databinding.ActivityPinSetupBinding;
import com.budgetmate.app.utils.PinManager;

public class PinSetupActivity extends AppCompatActivity {

    private ActivityPinSetupBinding binding;
    private PinManager pinManager;
    private StringBuilder pinInput = new StringBuilder();
    private StringBuilder confirmPinInput = new StringBuilder();
    private boolean confirmingPin = false;
    private String firstPin = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPinSetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pinManager = new PinManager(this);

        setupPinPad();
        updateDots();
    }

    private void setupPinPad() {
        // Bind each button: 0-9, delete, confirm
        int[] keyIds = {
            binding.btn0.getId(), binding.btn1.getId(), binding.btn2.getId(),
            binding.btn3.getId(), binding.btn4.getId(), binding.btn5.getId(),
            binding.btn6.getId(), binding.btn7.getId(), binding.btn8.getId(),
            binding.btn9.getId()
        };

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
        binding.btnConfirm.setOnClickListener(v -> onConfirm());
    }

    private void onDigit(String digit) {
        StringBuilder current = confirmingPin ? confirmPinInput : pinInput;
        if (current.length() < 4) {
            current.append(digit);
            updateDots();

            if (current.length() == 4 && !confirmingPin) {
                // Auto-advance to confirm step
                firstPin = pinInput.toString();
                confirmingPin = true;
                binding.tvPinTitle.setText("Confirm Your PIN");
                binding.tvPinSubtitle.setText("Re-enter your 4-digit PIN");
                updateDots();
            }
        }
    }

    private void onDelete() {
        StringBuilder current = confirmingPin ? confirmPinInput : pinInput;
        if (current.length() > 0) {
            current.deleteCharAt(current.length() - 1);
            updateDots();
        }
    }

    private void onConfirm() {
        if (confirmingPin && confirmPinInput.length() == 4) {
            if (firstPin.equals(confirmPinInput.toString())) {
                pinManager.savePin(firstPin);
                Toast.makeText(this, "PIN set successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "PINs do not match. Try again.", Toast.LENGTH_SHORT).show();
                pinInput.setLength(0);
                confirmPinInput.setLength(0);
                confirmingPin = false;
                binding.tvPinTitle.setText("Set Your PIN");
                binding.tvPinSubtitle.setText("Enter a 4-digit PIN to secure your app");
                updateDots();
            }
        }
    }

    private void updateDots() {
        StringBuilder current = confirmingPin ? confirmPinInput : pinInput;
        int filled = current.length();
        // Update 4 dot views based on filled count
        binding.dot1.setActivated(filled >= 1);
        binding.dot2.setActivated(filled >= 2);
        binding.dot3.setActivated(filled >= 3);
        binding.dot4.setActivated(filled >= 4);
    }
}
