package com.budgetmate.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PinManager {

    private static final String PREFS_NAME = "budgetmate_secure_prefs";
    private static final String KEY_PIN_HASH = "pin_hash";
    private static final String KEY_HAS_PIN = "has_pin";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private final SharedPreferences prefs;

    public PinManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void savePin(String pin) {
        prefs.edit()
             .putString(KEY_PIN_HASH, hashPin(pin))
             .putBoolean(KEY_HAS_PIN, true)
             .apply();
    }

    public boolean verifyPin(String pin) {
        String storedHash = prefs.getString(KEY_PIN_HASH, "");
        return storedHash.equals(hashPin(pin));
    }

    public boolean hasPin() {
        return prefs.getBoolean(KEY_HAS_PIN, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void saveUserName(String name) {
        prefs.edit().putString(KEY_USER_NAME, name).apply();
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "User");
    }

    public void logout() {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply();
    }

    private String hashPin(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pin.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return pin; // fallback (shouldn't happen)
        }
    }
}
