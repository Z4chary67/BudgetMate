package com.budgetmate.app.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.budgetmate.app.adapters.TransactionAdapter;
import com.budgetmate.app.databinding.FragmentTransactionsBinding;
import com.budgetmate.app.models.Transaction;
import com.budgetmate.app.viewmodels.TransactionViewModel;
import java.util.Calendar;
import java.util.List;

public class TransactionsFragment extends Fragment {

    private FragmentTransactionsBinding binding;
    private TransactionViewModel viewModel;
    private TransactionAdapter adapter;
    private String currentFilter = "all";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        adapter = new TransactionAdapter(transaction -> {
            viewModel.delete(transaction);
        });

        binding.rvTransactions.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTransactions.setAdapter(adapter);

        // Observe all transactions by default
        observeTransactions();

        // Build calendar strip for current week
        buildCalendarStrip();

        // Filter buttons
        binding.btnFilterAll.setOnClickListener(v -> {
            currentFilter = "all";
            updateFilterButtons();
            observeTransactions();
        });

        binding.btnFilterIncome.setOnClickListener(v -> {
            currentFilter = "income";
            updateFilterButtons();
            observeTransactions();
        });

        binding.btnFilterExpense.setOnClickListener(v -> {
            currentFilter = "expense";
            updateFilterButtons();
            observeTransactions();
        });
    }

    private void observeTransactions() {
        // Remove previous observers first
        viewModel.getAllTransactions().removeObservers(getViewLifecycleOwner());
        viewModel.getByType("income").removeObservers(getViewLifecycleOwner());
        viewModel.getByType("expense").removeObservers(getViewLifecycleOwner());

        if ("all".equals(currentFilter)) {
            viewModel.getAllTransactions().observe(getViewLifecycleOwner(), this::updateList);
        } else {
            viewModel.getByType(currentFilter).observe(getViewLifecycleOwner(), this::updateList);
        }
    }

    private void updateList(List<Transaction> transactions) {
        adapter.setTransactions(transactions);
    }

    private void updateFilterButtons() {
        int activeColor  = android.graphics.Color.parseColor("#00E5A0");
        int inactiveColor = android.graphics.Color.parseColor("#1A2235");
        int activeText   = android.graphics.Color.parseColor("#0A0E1A");
        int inactiveText = android.graphics.Color.parseColor("#6B7A99");

        binding.btnFilterAll.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf("all".equals(currentFilter) ? activeColor : inactiveColor));
        binding.btnFilterAll.setTextColor("all".equals(currentFilter) ? activeText : inactiveText);

        binding.btnFilterIncome.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf("income".equals(currentFilter) ? activeColor : inactiveColor));
        binding.btnFilterIncome.setTextColor("income".equals(currentFilter) ? activeText : inactiveText);

        binding.btnFilterExpense.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf("expense".equals(currentFilter) ? activeColor : inactiveColor));
        binding.btnFilterExpense.setTextColor("expense".equals(currentFilter) ? activeText : inactiveText);
    }

    private void buildCalendarStrip() {
        Calendar cal = Calendar.getInstance();
        // Go back to start of week
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

        String[] days = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        int today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < 7; i++) {
            int dayNum = cal.get(Calendar.DAY_OF_MONTH);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;

            LinearLayout dayView = new LinearLayout(requireContext());
            dayView.setOrientation(LinearLayout.VERTICAL);
            dayView.setGravity(android.view.Gravity.CENTER);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                dpToPx(44), LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(dpToPx(4), 0, dpToPx(4), 0);
            dayView.setLayoutParams(params);
            dayView.setPadding(dpToPx(4), dpToPx(8), dpToPx(4), dpToPx(8));

            if (dayNum == today) {
                dayView.setBackgroundColor(android.graphics.Color.parseColor("#00E5A0"));
                dayView.getBackground();
            } else {
                dayView.setBackgroundResource(com.budgetmate.app.R.drawable.bg_category_icon);
            }

            TextView tvNum = new TextView(requireContext());
            tvNum.setText(String.valueOf(dayNum));
            tvNum.setTextSize(16);
            tvNum.setGravity(android.view.Gravity.CENTER);
            tvNum.setTextColor(dayNum == today ?
                android.graphics.Color.parseColor("#0A0E1A") :
                android.graphics.Color.parseColor("#F0F4FF"));
            tvNum.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView tvDay = new TextView(requireContext());
            tvDay.setText(days[dayOfWeek % 7]);
            tvDay.setTextSize(9);
            tvDay.setGravity(android.view.Gravity.CENTER);
            tvDay.setTextColor(dayNum == today ?
                android.graphics.Color.parseColor("#0A0E1A") :
                android.graphics.Color.parseColor("#6B7A99"));

            dayView.addView(tvDay);
            dayView.addView(tvNum);
            binding.calendarStrip.addView(dayView);

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private int dpToPx(int dp) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
