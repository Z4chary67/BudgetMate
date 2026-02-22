package com.budgetmate.app.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.budgetmate.app.R;
import com.budgetmate.app.databinding.FragmentAddTransactionBinding;
import com.budgetmate.app.models.Transaction;
import com.budgetmate.app.viewmodels.TransactionViewModel;

public class AddTransactionFragment extends Fragment {

    private FragmentAddTransactionBinding binding;
    private TransactionViewModel viewModel;
    private String selectedType = "income";

    private static final String[] CATEGORIES = {
        "Housing", "Food & Dining", "Transportation", "Education",
        "Health", "Subscriptions", "Utilities", "Shopping", "Other"
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        // Category spinner
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            CATEGORIES
        );
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(catAdapter);

        // Type toggle
        binding.btnTypeIncome.setOnClickListener(v -> {
            selectedType = "income";
            updateTypeButtons();
        });

        binding.btnTypeExpense.setOnClickListener(v -> {
            selectedType = "expense";
            updateTypeButtons();
        });

        // Check if launched from "Add Expense" quick action
        if (getArguments() != null) {
            String type = getArguments().getString("type", "income");
            selectedType = type;
            updateTypeButtons();
        }

        updateTypeButtons();

        // Save
        binding.btnSave.setOnClickListener(v -> saveTransaction());
    }

    private void saveTransaction() {
        String title     = binding.etTitle.getText().toString().trim();
        String amountStr = binding.etAmount.getText().toString().trim();
        String note      = binding.etNote.getText().toString().trim();
        String category  = binding.spinnerCategory.getSelectedItem().toString();

        if (title.isEmpty()) {
            binding.etTitle.setError("Please enter a title");
            return;
        }
        if (amountStr.isEmpty()) {
            binding.etAmount.setError("Please enter an amount");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            binding.etAmount.setError("Invalid amount");
            return;
        }

        if (amount <= 0) {
            binding.etAmount.setError("Amount must be greater than 0");
            return;
        }

        Transaction transaction = new Transaction(
            title, amount, selectedType, category,
            System.currentTimeMillis(), note
        );

        viewModel.insert(transaction);
        Toast.makeText(requireContext(), "Transaction saved!", Toast.LENGTH_SHORT).show();

        // Go back
        Navigation.findNavController(requireView()).navigateUp();
    }

    private void updateTypeButtons() {
        int activeColor   = android.graphics.Color.parseColor("#00E5A0");
        int inactiveColor = android.graphics.Color.parseColor("#1A2235");
        int activeText    = android.graphics.Color.parseColor("#0A0E1A");
        int inactiveText  = android.graphics.Color.parseColor("#6B7A99");

        boolean isIncome = "income".equals(selectedType);

        binding.btnTypeIncome.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(isIncome ? activeColor : inactiveColor));
        binding.btnTypeIncome.setTextColor(isIncome ? activeText : inactiveText);

        binding.btnTypeExpense.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(!isIncome ? activeColor : inactiveColor));
        binding.btnTypeExpense.setTextColor(!isIncome ? activeText : inactiveText);

        binding.btnSave.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(isIncome ?
                android.graphics.Color.parseColor("#00E5A0") :
                android.graphics.Color.parseColor("#FF6B6B")));
        binding.btnSave.setTextColor(android.graphics.Color.parseColor("#0A0E1A"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
