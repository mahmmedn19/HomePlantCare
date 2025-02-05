package com.project.homeplantcare.ui.admin_screen;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.ActivityAdminMainBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AdminMainActivity extends AppCompatActivity implements BaseFragment.ToolbarHandler {
    private ActivityAdminMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.md_theme_surface));

        setSupportActionBar(binding.adminToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        // Setup Navigation Component
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_host_admin);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavAdmin, navController);
        }

        // Change back button icon dynamically
        if (navController != null) {
            navController.addOnDestinationChangedListener((@NonNull NavController controller, @NonNull androidx.navigation.NavDestination destination, Bundle arguments) -> {
                if (destination.getId() == R.id.nav_manage_plants) {
                    binding.adminToolbar.setNavigationIcon(null);
                } else {
                    binding.adminToolbar.setNavigationIcon(R.drawable.ic_back);
                }
            });
        }

        // Hide Bottom Navigation on Specific Screens
        assert navController != null;
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() != R.id.nav_manage_plants &&
                    destination.getId() != R.id.nav_manage_diseases &&
                    destination.getId() != R.id.nav_manage_articles &&
                    destination.getId() != R.id.nav_manage_profile) {
                binding.bottomNavAdmin.setVisibility(View.GONE);
            } else {
                binding.bottomNavAdmin.setVisibility(View.VISIBLE);
            }
        });

        // Bottom Navigation Item Selection
        binding.bottomNavAdmin.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_manage_plants) {
                navController.navigate(R.id.nav_manage_plants);
            } else if (item.getItemId() == R.id.nav_manage_diseases) {
                navController.navigate(R.id.nav_manage_diseases);
            } else if (item.getItemId() == R.id.nav_manage_articles) {
                navController.navigate(R.id.nav_manage_articles);
            }else if (item.getItemId() == R.id.nav_manage_profile) {
                navController.navigate(R.id.nav_manage_profile);
            }
            return true;
        });
    }

    @Override
    public void setToolbarTitle(String title) {
        binding.adminToolbar.setTitle(title);
        binding.adminToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
    }

    @Override
    public void showBackButton(boolean show) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(show);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // Set Custom Back Icon
            binding.adminToolbar.setNavigationOnClickListener(v -> {
                if (show) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void setToolbarVisibility(boolean isVisible) {
        if (isVisible) {
            binding.adminToolbar.setVisibility(View.VISIBLE);
        } else {
            binding.adminToolbar.setVisibility(View.GONE);
        }
    }
}
