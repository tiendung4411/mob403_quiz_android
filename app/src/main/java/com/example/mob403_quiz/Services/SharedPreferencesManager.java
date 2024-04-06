package com.example.mob403_quiz.Services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mob403_quiz.Models.Users;
import com.google.gson.Gson;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_USER = "user";
    private static final String KEY_ROLE = "role"; // Add key for user role

    public static void saveUser(Context context, Users user, String role) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert Users object to JSON string
        Gson gson = new Gson();
        String json = gson.toJson(user);

        // Save JSON string in SharedPreferences
        editor.putString(KEY_USER, json);
        editor.putString(KEY_ROLE, role); // Save user role
        editor.apply();
    }

    public static Users getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Retrieve JSON string from SharedPreferences
        String json = sharedPreferences.getString(KEY_USER, null);

        if (json != null) {
            // Convert JSON string back to Users object
            Gson gson = new Gson();
            return gson.fromJson(json, Users.class);
        } else {
            return null;
        }
    }

    public static String getUserRole(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ROLE, ""); // Retrieve user role
    }

    public static void clearUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USER);
        editor.remove(KEY_ROLE); // Remove user role
        editor.apply();
    }
}
