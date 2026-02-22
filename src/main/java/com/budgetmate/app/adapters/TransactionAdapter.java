package com.budgetmate.app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.budgetmate.app.databinding.ItemTransactionBinding;
import com.budgetmate.app.models.Transaction;
import com.budgetmate.app.utils.CurrencyFormatter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactions;
    private OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDelete(Transaction transaction);
    }

    public TransactionAdapter(OnDeleteClickListener deleteListener) {
        this.deleteListener = deleteListener;
        this.transactions = new ArrayList<>();
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions != null ? transactions : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction txn = transactions.get(position);
        holder.bind(txn);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransactionBinding binding;

        ViewHolder(ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Transaction txn) {
            binding.tvTitle.setText(txn.getTitle());
            binding.tvCategory.setText(txn.getCategory());

            String dateStr = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    .format(new Date(txn.getDate()));
            binding.tvDate.setText(dateStr);

            if ("income".equals(txn.getType())) {
                binding.tvAmount.setText("+" + CurrencyFormatter.format(txn.getAmount()));
                binding.tvAmount.setTextColor(0xFF00E5A0);
            } else {
                binding.tvAmount.setText("-" + CurrencyFormatter.format(txn.getAmount()));
                binding.tvAmount.setTextColor(0xFFFF6B6B);
            }

            // Emoji icon based on category
            binding.tvCategoryIcon.setText(getCategoryEmoji(txn.getCategory()));

            if (deleteListener != null) {
                binding.btnDelete.setOnClickListener(v -> deleteListener.onDelete(txn));
            }
        }

        private String getCategoryEmoji(String category) {
            if (category == null) return "💰";
            switch (category) {
                case "Food & Dining": return "🍔";
                case "Transportation": return "🚗";
                case "Housing": return "🏠";
                case "Health": return "🏥";
                case "Education": return "🎓";
                case "Shopping": return "🛒";
                case "Subscriptions": return "📱";
                case "Utilities": return "💡";
                case "Income": return "💼";
                default: return "💰";
            }
        }
    }
}
