package com.project.homeplantcare.ui.onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.project.homeplantcare.R;
import com.project.homeplantcare.ui.MainActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OnBoardingActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "HomePlantCarePrefs";
    private static final String KEY_FIRST_TIME = "isFirstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if onboarding has been shown
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean(KEY_FIRST_TIME, true);

        if (!isFirstTime) {
            // If not the first time, redirect to MainActivity
            navigateToMain();
            return;
        }
        setContentView(R.layout.activity_on_boarding);


        findViewById(R.id.btn_get_started).setOnClickListener(v -> {
            // Save preference to indicate onboarding is complete
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_FIRST_TIME, false);
            editor.apply();

            // Navigate to MainActivity
            navigateToMain();
        });
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
