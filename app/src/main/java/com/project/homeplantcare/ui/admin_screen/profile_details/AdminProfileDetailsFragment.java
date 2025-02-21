package com.project.homeplantcare.ui.admin_screen.profile_details;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.AdminProfile;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentAdminProfileDetailsBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.InputValidator;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AdminProfileDetailsFragment extends BaseFragment<FragmentAdminProfileDetailsBinding> {

    private AdminProfileViewModel viewModel;

    @Override
    protected String getTAG() {
        return "AdminProfileDetailsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_admin_profile_details;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(AdminProfileViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Profile Details");
        showBackButton(true);
        InputValidator.clearErrorOnTextChange(binding.adminName);

        observeAdminProfile();

        binding.btnSaveProfile.setOnClickListener(v -> {
            String adminName = Objects.requireNonNull(binding.etAdminName.getText()).toString().trim();
            if (!adminName.isEmpty()) {
                viewModel.updateAdminProfile(adminName);
            } else {
                Toast.makeText(requireContext(), "Admin name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeAdminProfile() {
        viewModel.getAdminProfile().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                binding.progressBar.setVisibility(View.GONE);
                AdminProfile adminProfile = result.getData();
                if (adminProfile != null) {
                    binding.etAdminName.setText(adminProfile.getAdminName());
                    binding.etAdminEmail.setText(adminProfile.getAdminEmail());
                }
                Navigation.findNavController(requireView()).navigateUp();
            } else if (result.getStatus() == Result.Status.ERROR) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getUpdateProfileLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
