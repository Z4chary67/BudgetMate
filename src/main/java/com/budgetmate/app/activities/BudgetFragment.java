package com.budgetmate.app.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.budgetmate.app.R;
import com.budgetmate.app.adapters.BudgetAdapter;
import com.budgetmate.app.databinding.FragmentBudgetBinding;
import com.budgetmate.app.models.Budget;
import com.budgetmate.app.utils.NotificationHelper;
import com.budgetmate.app.viewmodels.BudgetViewModel;
import java.util.Calendar;

public class BudgetFragment extends Fragment {

    private FragmentBudgetBinding binding;
    private BudgetViewModel viewModel;
    private BudgetAdapter adapter;

    private static final String[] CATEGORIES = {
        "Housing", "Food & Dining", "Transportation", "Education",
        "Health", "Subscriptions", "Utilities", "Shopping", "Other"
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBudgetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(BudgetViewModel.class);

        adapter = new BudgetAdapter(budget -> {
            viewModel.delete(budget);
        });

        binding.rvBudgets.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvBudgets.setAdapter(adapter);

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        viewModel.getBudgetsByMonth(month, year).observe(getViewLifecycleOwner(), budgets -> {
            adapter.setBudgets(budgets);

            // Check and notify exceeded budgets
            double totalLimit = 0, totalSpent = 0;
            int warnings = 0;
            for (Budget b : budgets) {
                totalLimit += b.getLimitAmount();
                totalSpent += b.getSpentAmount();
                if (b.isExceeded()) {
                    NotificationHelper.showBudgetExceeded(requireContext(),
                            b.getCategory(), b.getSpentAmount() - b.getLimitAmount());
                } else if (b.getProgressPercent() >= 80) {
                    warnings++;
                    NotificationHelper.showBudgetWarning(requireContext(),
                            b.getCategory(), b.getProgressPercent());
                }
            }

            binding.tvTotalLimit.setText("₱" + String.format("%.0f", totalLimit));
            binding.tvTotalSpent.setText("₱" + String.format("%.0f", totalSpent));
            binding.tvTotalRemaining.setText("₱" + String.format("%.0f", totalLimit - totalSpent));
            binding.tvWarnings.setText(warnings + " warning(s) this month");
        });

        binding.btnAddBudget.setOnClickListener(v -> showAddBudgetDialog());
    }

    private void showAddBudgetDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_budget, null);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        EditText etLimit = dialogView.findViewById(R.id.etBudgetLimit);
        EditText etCustomLabel = dialogView.findViewById(R.id.etCustomLabel);

        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, CATEGORIES);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                etCustomLabel.setVisibility("Other".equals(CATEGORIES[pos]) ? View.VISIBLE : View.GONE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        new AlertDialog.Builder(requireContext())
                .setTitle("Add Budget Limit")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    String category = spinnerCategory.getSelectedItem().toString();
                    String customLabel = etCustomLabel.getText().toString().trim();
                    String limitStr = etLimit.getText().toString().trim();

                    if (!limitStr.isEmpty()) {
                        double limit = Double.parseDouble(limitStr);
                        Calendar cal = Calendar.getInstance();
                        Budget budget = new Budget(category, customLabel, limit,
                                cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
                        viewModel.insert(budget);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
