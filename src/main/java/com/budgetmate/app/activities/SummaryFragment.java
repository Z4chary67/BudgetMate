package com.budgetmate.app.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import com.budgetmate.app.databinding.FragmentSummaryBinding;
import com.budgetmate.app.models.Transaction;
import com.budgetmate.app.viewmodels.TransactionViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.*;
import java.util.*;

public class SummaryFragment extends Fragment {

    private FragmentSummaryBinding binding;
    private TransactionViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSummaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(TransactionViewModel.class);

        viewModel.getAllTransactions().observe(getViewLifecycleOwner(), transactions -> {
            if (transactions != null) {
                setupDonutChart(transactions);
                setupPieChart(transactions);
                setupLineChart(transactions);
            }
        });

        // Period toggle buttons
        binding.btnWeek.setOnClickListener(v -> {
            binding.btnWeek.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(Color.parseColor("#7C6CF8")));
            binding.btnMonth.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(Color.parseColor("#1A2235")));
            binding.btnYear.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(Color.parseColor("#1A2235")));
        });

        binding.btnMonth.setOnClickListener(v -> {
            binding.btnMonth.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(Color.parseColor("#7C6CF8")));
            binding.btnWeek.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(Color.parseColor("#1A2235")));
            binding.btnYear.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(Color.parseColor("#1A2235")));
        });

        binding.btnYear.setOnClickListener(v -> {
            binding.btnYear.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(Color.parseColor("#7C6CF8")));
            binding.btnWeek.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(Color.parseColor("#1A2235")));
            binding.btnMonth.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(Color.parseColor("#1A2235")));
        });
    }

    private void setupDonutChart(List<Transaction> transactions) {
        Map<String, Float> categoryMap = new LinkedHashMap<>();
        for (Transaction t : transactions) {
            if ("expense".equals(t.getType())) {
                categoryMap.merge(t.getCategory(), (float) t.getAmount(), Float::sum);
            }
        }

        if (categoryMap.isEmpty()) {
            binding.donutChart.setNoDataText("No expenses yet");
            binding.donutChart.setNoDataTextColor(Color.LTGRAY);
            return;
        }

        List<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categoryMap.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{
            Color.parseColor("#FFC14E"), Color.parseColor("#7C6CF8"),
            Color.parseColor("#38B6FF"), Color.parseColor("#FF6B6B"),
            Color.parseColor("#00E5A0"), Color.parseColor("#C084FC")
        });
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(10f);
        dataSet.setSliceSpace(2f);

        PieData data = new PieData(dataSet);
        binding.donutChart.setData(data);
        binding.donutChart.setHoleRadius(55f);
        binding.donutChart.setTransparentCircleRadius(60f);
        binding.donutChart.setHoleColor(Color.parseColor("#161F30"));
        binding.donutChart.getDescription().setEnabled(false);
        binding.donutChart.setCenterText("Expenses");
        binding.donutChart.setCenterTextColor(Color.WHITE);
        binding.donutChart.setCenterTextSize(12f);
        binding.donutChart.getLegend().setTextColor(Color.LTGRAY);
        binding.donutChart.setBackgroundColor(Color.TRANSPARENT);
        binding.donutChart.animateY(1000);
        binding.donutChart.invalidate();
    }

    private void setupPieChart(List<Transaction> transactions) {
        double totalIncome = 0, totalExpense = 0;
        for (Transaction t : transactions) {
            if ("income".equals(t.getType())) totalIncome += t.getAmount();
            else totalExpense += t.getAmount();
        }

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) totalIncome, "Income"));
        entries.add(new PieEntry((float) totalExpense, "Expenses"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(new int[]{
            Color.parseColor("#7C6CF8"), Color.parseColor("#FF6B6B")
        });
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(11f);
        dataSet.setSliceSpace(3f);

        PieData data = new PieData(dataSet);
        binding.pieChart.setData(data);
        binding.pieChart.setHoleRadius(50f);
        binding.pieChart.setHoleColor(Color.parseColor("#161F30"));
        binding.pieChart.getDescription().setEnabled(false);
        binding.pieChart.getLegend().setTextColor(Color.LTGRAY);
        binding.pieChart.setBackgroundColor(Color.TRANSPARENT);
        binding.pieChart.animateY(1000);
        binding.pieChart.invalidate();
    }

    private void setupLineChart(List<Transaction> transactions) {
        Calendar cal = Calendar.getInstance();
        Map<Integer, Float> incomeByMonth = new LinkedHashMap<>();
        Map<Integer, Float> expenseByMonth = new LinkedHashMap<>();

        for (int i = 5; i >= 0; i--) {
            Calendar c = (Calendar) cal.clone();
            c.add(Calendar.MONTH, -i);
            int key = c.get(Calendar.YEAR) * 100 + c.get(Calendar.MONTH);
            incomeByMonth.put(key, 0f);
            expenseByMonth.put(key, 0f);
        }

        for (Transaction t : transactions) {
            Calendar txnCal = Calendar.getInstance();
            txnCal.setTimeInMillis(t.getDate());
            int key = txnCal.get(Calendar.YEAR) * 100 + txnCal.get(Calendar.MONTH);
            if (incomeByMonth.containsKey(key)) {
                if ("income".equals(t.getType())) {
                    incomeByMonth.merge(key, (float) t.getAmount(), Float::sum);
                } else {
                    expenseByMonth.merge(key, (float) t.getAmount(), Float::sum);
                }
            }
        }

        List<Entry> incomeEntries = new ArrayList<>();
        List<Entry> expenseEntries = new ArrayList<>();
        int i = 0;
        for (Integer key : incomeByMonth.keySet()) {
            incomeEntries.add(new Entry(i, incomeByMonth.getOrDefault(key, 0f)));
            expenseEntries.add(new Entry(i, expenseByMonth.getOrDefault(key, 0f)));
            i++;
        }

        LineDataSet incomeSet = new LineDataSet(incomeEntries, "Income");
        incomeSet.setColor(Color.parseColor("#7C6CF8"));
        incomeSet.setCircleColor(Color.parseColor("#7C6CF8"));
        incomeSet.setLineWidth(2.5f);
        incomeSet.setCircleRadius(4f);
        incomeSet.setDrawFilled(true);
        incomeSet.setFillColor(Color.parseColor("#7C6CF8"));
        incomeSet.setFillAlpha(30);
        incomeSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        incomeSet.setValueTextColor(Color.TRANSPARENT);

        LineDataSet expenseSet = new LineDataSet(expenseEntries, "Expenses");
        expenseSet.setColor(Color.parseColor("#FF6B6B"));
        expenseSet.setCircleColor(Color.parseColor("#FF6B6B"));
        expenseSet.setLineWidth(2.5f);
        expenseSet.setCircleRadius(4f);
        expenseSet.setDrawFilled(true);
        expenseSet.setFillColor(Color.parseColor("#FF6B6B"));
        expenseSet.setFillAlpha(30);
        expenseSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        expenseSet.setValueTextColor(Color.TRANSPARENT);

        LineData lineData = new LineData(incomeSet, expenseSet);
        binding.lineChart.setData(lineData);
        binding.lineChart.getDescription().setEnabled(false);
        binding.lineChart.getXAxis().setTextColor(Color.LTGRAY);
        binding.lineChart.getAxisLeft().setTextColor(Color.LTGRAY);
        binding.lineChart.getAxisRight().setEnabled(false);
        binding.lineChart.getLegend().setTextColor(Color.LTGRAY);
        binding.lineChart.setBackgroundColor(Color.TRANSPARENT);
        binding.lineChart.animateXY(1000, 1000);
        binding.lineChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
