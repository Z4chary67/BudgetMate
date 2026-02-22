package com.budgetmate.app.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    private static final NumberFormat format = NumberFormat.getNumberInstance(new Locale("en", "PH"));

    static {
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
    }

    public static String format(double amount) {
        return "₱ " + format.format(amount);
    }

    public static String formatShort(double amount) {
        if (amount >= 1_000_000) return String.format("₱%.1fM", amount / 1_000_000);
        if (amount >= 1_000) return String.format("₱%.1fK", amount / 1_000);
        return format(amount);
    }
}
