package com.project.homeplantcare.ui;

import static com.project.homeplantcare.utils.LocalLang.setLocale;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.ActivityMainBinding;
import com.project.homeplantcare.di.NetworkModule;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.SharedPrefUtils;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements BaseFragment.ToolbarHandler {
    private ActivityMainBinding binding;
    private NavController navController;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        setLocale("en", this);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // ✅ Fetch AI link and observe the LiveData
        viewModel.getSingleAILink().observe(this, result -> {
            if (Objects.requireNonNull(result.getStatus()) == Result.Status.SUCCESS) {
                String aiLink = result.getData();
                Log.d("AI_LINK", aiLink);
                if (aiLink != null) {
                    SharedPrefUtils.saveAiLink(this, aiLink); // ✅ Save AI link to SharedPreferences
                }
            }
        });
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // Change back button icon dynamically
        if (navController != null) {
            navController.addOnDestinationChangedListener((@NonNull NavController controller, @NonNull androidx.navigation.NavDestination destination, Bundle arguments) -> {
                if (destination.getId() == R.id.homeFragment) {
                    binding.toolbar.setNavigationIcon(null); // Hide back button on home
                } else {
                    binding.toolbar.setNavigationIcon(R.drawable.ic_back);
                }
            });
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
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // Set Custom Back Icon
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