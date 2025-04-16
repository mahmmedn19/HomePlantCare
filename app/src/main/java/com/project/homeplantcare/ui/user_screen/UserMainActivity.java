package com.project.homeplantcare.ui.user_screen;

import static com.project.homeplantcare.utils.LocalLang.setLocale;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.ActivityUserMainBinding;
import com.project.homeplantcare.ui.MainViewModel;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.SharedPrefUtils;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserMainActivity extends AppCompatActivity implements BaseFragment.ToolbarHandler {
    private ActivityUserMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        setLocale("en", this);
        // Apply system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Setup Toolbar
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Setup Navigation Component
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_host);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNav, navController);
        }

        // Dynamic Toolbar Back Button Handling
        navController.addOnDestinationChangedListener((@NonNull NavController controller, @NonNull androidx.navigation.NavDestination destination, Bundle arguments) -> {
            if (destination.getId() == R.id.homeFragment) {
                binding.toolbar.setNavigationIcon(null); // Hide back button on Home
            } else {
                binding.toolbar.setNavigationIcon(R.drawable.ic_back);
                binding.toolbar.setNavigationOnClickListener(v -> navController.navigateUp());
            }
        });

        // Hide Bottom Navigation on Specific Screens
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() != R.id.homeFragment &&
                    destination.getId() != R.id.cameraFragment &&
                    destination.getId() != R.id.historyFragment &&
                    destination.getId() != R.id.favoritesFragment &&
                    destination.getId() != R.id.profileFragment) {
                binding.bottomNav.setVisibility(View.GONE);
            } else {
                binding.bottomNav.setVisibility(View.VISIBLE);
            }
        });

        // Handle Bottom Navigation Item Clicks
        binding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.homeFragment) {
                navController.navigate(R.id.homeFragment);
            } else if (item.getItemId() == R.id.cameraFragment) {
                navController.navigate(R.id.cameraFragment);
            } else if (item.getItemId() == R.id.historyFragment) {
                navController.navigate(R.id.historyFragment);
            } else if (item.getItemId() == R.id.favoritesFragment) {
                navController.navigate(R.id.favoritesFragment);
            } else if (item.getItemId() == R.id.profileFragment) {
                navController.navigate(R.id.profileFragment);
            }
            return true;
        });
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
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
            binding.toolbar.setNavigationOnClickListener(v -> {
                if (show) {
                    getOnBackPressedDispatcher().onBackPressed();
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
