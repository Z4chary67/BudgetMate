package com.budgetmate.app.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.budgetmate.app.databinding.ItemBudgetBinding;
import com.budgetmate.app.models.Budget;
import com.budgetmate.app.utils.CurrencyFormatter;
import java.util.ArrayList;
import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.ViewHolder> {

    public interface OnDeleteClickListener { void onDelete(Budget budget); }

    private List<Budget> budgets = new ArrayList<>();
    private final OnDeleteClickListener deleteListener;

    public BudgetAdapter(OnDeleteClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public void setBudgets(List<Budget> budgets) {
        this.budgets = budgets != null ? budgets : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemBudgetBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(budgets.get(position));
    }

    @Override
    public int getItemCount() { return budgets.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBudgetBinding b;

        ViewHolder(ItemBudgetBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        void bind(Budget budget) {
            String label = "Other".equals(budget.getCategory()) && budget.getCustomLabel() != null
                    ? budget.getCustomLabel()
                    : budget.getCategory();

            b.tvCategory.setText(getCategoryEmoji(budget.getCategory()) + "  " + label);
            b.tvSpent.setText(CurrencyFormatter.format(budget.getSpentAmount()));
            b.tvLimit.setText(CurrencyFormatter.format(budget.getLimitAmount()));
            b.progressBar.setProgress(budget.getProgressPercent());

            if (budget.isExceeded()) {
                b.tvStatus.setText("EXCEEDED");
                b.tvStatus.setTextColor(Color.parseColor("#FF6B6B"));
                b.progressBar.setProgressTintList(
                        android.content.res.ColorStateList.valueOf(Color.parseColor("#FF6B6B")));
            } else if (budget.getProgressPercent() >= 80) {
                b.tvStatus.setText("CAUTION");
                b.tvStatus.setTextColor(Color.parseColor("#FFC14E"));
                b.progressBar.setProgressTintList(
                        android.content.res.ColorStateList.valueOf(Color.parseColor("#FFC14E")));
            } else {
                b.tvStatus.setText("ON TRACK");
                b.tvStatus.setTextColor(Color.parseColor("#00E5A0"));
                b.progressBar.setProgressTintList(
                        android.content.res.ColorStateList.valueOf(Color.parseColor("#00E5A0")));
            }

            if (deleteListener != null) {
                b.btnDelete.setOnClickListener(v -> deleteListener.onDelete(budget));
            }
        }

        private String getCategoryEmoji(String category) {
            if (category == null) return "💰";
            switch (category) {
                case "Housing": return "🏠";
                case "Food & Dining": return "🍔";
                case "Transportation": return "🚗";
                case "Education": return "🎓";
                case "Health": return "🏥";
                case "Subscriptions": return "📱";
                case "Utilities": return "💡";
                case "Shopping": return "🛒";
                default: return "💰";
            }
        }
    }
}
