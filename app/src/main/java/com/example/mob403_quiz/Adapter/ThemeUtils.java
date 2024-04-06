package com.example.mob403_quiz.Adapter;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mob403_quiz.R;

public class ThemeUtils {
    // Function to set progress bar color based on theme
    public static void setProgressBarColor(Fragment fragment, ProgressBar progressBar) {
        // Get current night mode configuration
        int currentNightMode = fragment.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        // Check if the current theme is in night mode
        boolean isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES;

        // Define colors for both dark and light themes
        int colorDarkTheme = ContextCompat.getColor(fragment.requireContext(), R.color.colorDarkTheme);
        int colorLightTheme = ContextCompat.getColor(fragment.requireContext(), R.color.colorLightTheme);

        // Set color based on the current theme
        int color = isNightMode ? colorDarkTheme : colorLightTheme;
        progressBar.setProgressTintList(ColorStateList.valueOf(color));
    }

    // Function to set progress bar color based on theme for activities
    public static void setProgressBarColor(Activity activity, ProgressBar progressBar) {
        // Get current night mode configuration
        int currentNightMode = activity.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        // Check if the current theme is in night mode
        boolean isNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES;

        // Define colors for both dark and light themes
        int colorDarkTheme = ContextCompat.getColor(activity, R.color.colorDarkTheme);
        int colorLightTheme = ContextCompat.getColor(activity, R.color.colorLightTheme);

        // Set color based on the current theme
        int color = isNightMode ? colorDarkTheme : colorLightTheme;
        progressBar.setProgressTintList(ColorStateList.valueOf(color));
    }
}
