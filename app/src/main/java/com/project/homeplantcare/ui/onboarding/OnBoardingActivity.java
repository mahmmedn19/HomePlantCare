package com.project.homeplantcare.ui.onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.di.NetworkModule;
import com.project.homeplantcare.ui.MainActivity;
import com.project.homeplantcare.ui.MainViewModel;
import com.project.homeplantcare.utils.SharedPrefUtils;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OnBoardingActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // ✅ Fetch AI link and observe the LiveData
        viewModel.getSingleAILink().observe(this, result -> {
            if (Objects.requireNonNull(result.getStatus()) == Result.Status.SUCCESS) {
                String aiLink = result.getData();
                Log.d("AI_LINK", aiLink);
                if (aiLink != null) {
                    SharedPrefUtils.saveAiLink(this, aiLink);
                }
            }
        });
        findViewById(R.id.btn_get_started).setOnClickListener(v -> {
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