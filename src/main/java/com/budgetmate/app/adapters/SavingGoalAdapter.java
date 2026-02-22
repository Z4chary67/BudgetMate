package com.budgetmate.app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.budgetmate.app.databinding.ItemSavingGoalBinding;
import com.budgetmate.app.models.SavingGoal;
import com.budgetmate.app.utils.CurrencyFormatter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SavingGoalAdapter extends RecyclerView.Adapter<SavingGoalAdapter.ViewHolder> {

    public interface OnDeleteClickListener { void onDelete(SavingGoal goal); }
    public interface OnAddAmountListener { void onAddAmount(SavingGoal goal, double amount, String source); }

    private List<SavingGoal> goals;
    private final OnDeleteClickListener deleteListener;
    private final OnAddAmountListener addAmountListener;

    public SavingGoalAdapter(OnDeleteClickListener deleteListener, OnAddAmountListener addAmountListener) {
        this.goals = new ArrayList<>();
        this.deleteListener = deleteListener;
        this.addAmountListener = addAmountListener;
    }

    public void setGoals(List<SavingGoal> goals) {
        this.goals = goals != null ? goals : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemSavingGoalBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(goals.get(position));
    }

    @Override
    public int getItemCount() { return goals.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSavingGoalBinding b;

        ViewHolder(ItemSavingGoalBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        void bind(SavingGoal goal) {
            b.tvGoalName.setText(goal.getEmoji() + " " + goal.getName());
            b.tvSavedAmount.setText(CurrencyFormatter.format(goal.getSavedAmount()));
            b.tvTargetAmount.setText(CurrencyFormatter.format(goal.getTargetAmount()));
            b.tvPercent.setText(goal.getProgressPercent() + "%");
            b.progressBar.setProgress(goal.getProgressPercent());

            String deadline = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    .format(new Date(goal.getDeadline()));
            b.tvDeadline.setText("🗓️ " + deadline);

            b.tvRemaining.setText("Remaining: " + CurrencyFormatter.format(goal.getRemainingAmount()));

            if (deleteListener != null) {
                b.btnDelete.setOnClickListener(v -> deleteListener.onDelete(goal));
            }

            if (addAmountListener != null) {
                b.btnAdd.setOnClickListener(v -> {
                    String amountStr = b.etAmount.getText().toString().trim();
                    String source = b.spinnerSource.getSelectedItem().toString();
                    if (!amountStr.isEmpty()) {
                        try {
                            double amount = Double.parseDouble(amountStr);
                            addAmountListener.onAddAmount(goal, amount, source);
                            b.etAmount.setText("");
                        } catch (NumberFormatException ignored) {}
                    }
                });
            }
        }
    }
}
