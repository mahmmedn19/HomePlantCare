package com.project.homeplantcare.ui;

import static com.project.homeplantcare.utils.LocalLang.setLocale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.ActivityMainBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.ui.onboarding.OnBoardingActivity;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements BaseFragment.ToolbarHandler {
    private ActivityMainBinding binding;
    private NavController navController;
    private static final String PREFS_NAME = "HomePlantCarePrefs";
    private static final String KEY_FIRST_TIME = "isFirstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        setLocale("en", this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Check if onboarding should be shown
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isFirstTime = preferences.getBoolean(KEY_FIRST_TIME, true);

        if (isFirstTime) {
            // Redirect to OnBoardingActivity
            Intent intent = new Intent(this, OnBoardingActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void setToolbarTitle(String title) {
        binding.toolbar.setTitle(title);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        binding.toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        //navigationBar color
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
    }

    @Override
    public void showBackButton(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);

            binding.toolbar.setNavigationOnClickListener(v -> {
                if (show) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void setToolbarVisibility(boolean isVisible) {
        if (isVisible) {
            binding.toolbar.setVisibility(View.VISIBLE);
        } else {
            binding.toolbar.setVisibility(View.GONE);
        }
    }
}