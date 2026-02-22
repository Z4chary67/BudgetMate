package com.budgetmate.app.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.budgetmate.app.R;
import com.budgetmate.app.adapters.TransactionAdapter;
import com.budgetmate.app.adapters.SavingGoalAdapter;
import com.budgetmate.app.databinding.FragmentDashboardBinding;
import com.budgetmate.app.utils.CurrencyFormatter;
import com.budgetmate.app.utils.PinManager;
import com.budgetmate.app.viewmodels.DashboardViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel viewModel;
    private TransactionAdapter transactionAdapter;
    private SavingGoalAdapter goalAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Greeting
        String name = new PinManager(requireContext()).getUserName();
        String timeGreeting = getTimeGreeting();
        binding.tvGreeting.setText(timeGreeting + ", " + name + "!");

        // Adapters
        transactionAdapter = new TransactionAdapter(null);
        binding.rvRecentTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecentTransactions.setAdapter(transactionAdapter);

        goalAdapter = new SavingGoalAdapter(null, null);
        binding.rvSavingGoals.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSavingGoals.setAdapter(goalAdapter);

        // Observe
        viewModel.getTotalBalance().observe(getViewLifecycleOwner(), balance -> {
            binding.tvTotalBalance.setText(CurrencyFormatter.format(balance != null ? balance : 0));
        });

        viewModel.getTotalIncome().observe(getViewLifecycleOwner(), income -> {
            binding.tvTotalIncome.setText(CurrencyFormatter.format(income != null ? income : 0));
        });

        viewModel.getTotalExpenses().observe(getViewLifecycleOwner(), expenses -> {
            binding.tvTotalExpenses.setText(CurrencyFormatter.format(expenses != null ? expenses : 0));
        });

        viewModel.getRecentTransactions().observe(getViewLifecycleOwner(), transactions -> {
            transactionAdapter.setTransactions(transactions);
        });

        viewModel.getRecentGoals().observe(getViewLifecycleOwner(), goals -> {
            goalAdapter.setGoals(goals);
        });

        // Quick Actions
        binding.btnAddIncome.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_dashboard_to_addTransaction));
        binding.btnAddExpense.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_dashboard_to_addTransaction));
        binding.btnBudgetLimit.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.budgetFragment));
        binding.btnSavingGoal.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.savingGoalsFragment));
        binding.btnSummary.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.summaryFragment));

        binding.tvSeeAllTransactions.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.transactionsFragment));

        binding.tvSeeAllGoals.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.savingGoalsFragment));

        // Open drawer
        binding.btnMenu.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });
    }

    private String getTimeGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 12) return "Good morning";
        if (hour < 17) return "Good afternoon";
        return "Good evening";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
