package com.budgetmate.app.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.budgetmate.app.R;
import com.budgetmate.app.adapters.SavingGoalAdapter;
import com.budgetmate.app.databinding.FragmentSavingGoalsBinding;
import com.budgetmate.app.models.SavingGoal;
import com.budgetmate.app.viewmodels.SavingGoalViewModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SavingGoalsFragment extends Fragment {

    private FragmentSavingGoalsBinding binding;
    private SavingGoalViewModel viewModel;
    private SavingGoalAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSavingGoalsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SavingGoalViewModel.class);

        adapter = new SavingGoalAdapter(
            goal -> viewModel.delete(goal),
            (goal, amount, source) -> {
                viewModel.addAmountToGoal(goal, amount);
                Toast.makeText(requireContext(),
                    "₱" + String.format("%.2f", amount) + " added from " + source,
                    Toast.LENGTH_SHORT).show();
            }
        );

        binding.rvGoals.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvGoals.setAdapter(adapter);

        viewModel.getAllGoals().observe(getViewLifecycleOwner(), goals -> {
            adapter.setGoals(goals);
        });

        binding.btnAddGoal.setOnClickListener(v -> showAddGoalDialog());
    }

    private void showAddGoalDialog() {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_add_goal, null);

        EditText etName       = dialogView.findViewById(R.id.etGoalName);
        EditText etEmoji      = dialogView.findViewById(R.id.etEmoji);
        EditText etTarget     = dialogView.findViewById(R.id.etTargetAmount);
        EditText etDeadline   = dialogView.findViewById(R.id.etDeadline);

        new AlertDialog.Builder(requireContext())
            .setTitle("New Saving Goal")
            .setView(dialogView)
            .setPositiveButton("Add", (dialog, which) -> {
                String name       = etName.getText().toString().trim();
                String emoji      = etEmoji.getText().toString().trim();
                String targetStr  = etTarget.getText().toString().trim();
                String deadlineStr = etDeadline.getText().toString().trim();

                if (name.isEmpty() || targetStr.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill name and amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                double target = Double.parseDouble(targetStr);
                if (emoji.isEmpty()) emoji = "🎯";

                long deadlineMs = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000); // default 30 days
                if (!deadlineStr.isEmpty()) {
                    try {
                        Date d = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(deadlineStr);
                        if (d != null) deadlineMs = d.getTime();
                    } catch (ParseException ignored) {}
                }

                SavingGoal goal = new SavingGoal(name, target, deadlineMs, emoji);
                viewModel.insert(goal);
                Toast.makeText(requireContext(), "Goal added!", Toast.LENGTH_SHORT).show();
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
