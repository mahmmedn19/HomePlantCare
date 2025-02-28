package com.project.homeplantcare.ui.user_screen.profile;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentUserProfileBinding;
import com.project.homeplantcare.ui.MainActivity;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.DialogUtils;
import com.project.homeplantcare.utils.InputValidator;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserProfileFragment extends BaseFragment<FragmentUserProfileBinding> {
    private UserProfileViewModel viewModel;

    @Override
    protected String getTAG() {
        return "UserProfileFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_user_profile;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("User Profile");
        showBackButton(false);

        // Observe LiveData for profile, updates, and password change
        observeUserProfile();
        observeProfileUpdate();
        observePasswordUpdate();
        binding.btnLogout.setOnClickListener(v ->
                DialogUtils.showConfirmationDialog(
                requireContext(),
                "Logout",
                "Are you sure you want to logout?",
                "Yes",
                "No",
                (dialog, which) -> {
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
        ));
        binding.btnUpdateProfile.setOnClickListener(v -> handleUpdateProfile());
        binding.btnSaveNewPassword.setOnClickListener(v -> handleChangePassword());
    }

    private void observeUserProfile() {
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                if (result.getStatus() == Result.Status.LOADING) {
                    binding.progressUpdateProfile.setVisibility(View.VISIBLE);
                } else {
                    binding.progressUpdateProfile.setVisibility(View.GONE);
                }

                if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                    binding.userName.getEditText().setText(result.getData().getUsername());
                    binding.userEmail.getEditText().setText(result.getData().getEmail());
                } else if (result.getStatus() == Result.Status.ERROR) {
                    showToast("Error loading profile: " + result.getErrorMessage());
                }
            }
        });
    }

    private void observeProfileUpdate() {
        viewModel.isUpdatingProfile().observe(getViewLifecycleOwner(), isUpdating -> {
            if (isUpdating) {
                binding.progressUpdateProfile.setVisibility(View.VISIBLE);
                binding.btnUpdateProfile.setEnabled(false);
            } else {
                binding.progressUpdateProfile.setVisibility(View.GONE);
                binding.btnUpdateProfile.setEnabled(true);
            }
        });
    }

    private void observePasswordUpdate() {
        viewModel.isUpdatingPassword().observe(getViewLifecycleOwner(), isUpdating -> {
            if (isUpdating) {
                binding.progressUpdatePassword.setVisibility(View.VISIBLE);
                binding.btnSaveNewPassword.setEnabled(false);
            } else {
                binding.progressUpdatePassword.setVisibility(View.GONE);
                binding.btnSaveNewPassword.setEnabled(true);
            }
        });
    }

    private void handleUpdateProfile() {
        String newName = Objects.requireNonNull(binding.userName.getEditText()).getText().toString().trim();
        if (newName.isEmpty()) {
            showToast("Name cannot be empty.");
            return;
        }

        viewModel.updateUserProfile(newName).observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                if (result.getStatus() == Result.Status.LOADING) {
                    binding.progressUpdateProfile.setVisibility(View.VISIBLE);
                } else {
                    binding.progressUpdateProfile.setVisibility(View.GONE);
                }

                if (result.getStatus() == Result.Status.SUCCESS) {
                    showToast("Profile updated successfully!");
                    Navigation.findNavController(requireView()).popBackStack();
                } else if (result.getStatus() == Result.Status.ERROR) {
                    showToast("Failed to update profile: " + result.getErrorMessage());
                }
            }
        });
    }

    private void handleChangePassword() {
        String oldPass = Objects.requireNonNull(binding.oldPassword.getEditText()).getText().toString().trim();
        String newPass = Objects.requireNonNull(binding.newPassword.getEditText()).getText().toString().trim();
        String confirmPass = Objects.requireNonNull(binding.confirmPassword.getEditText()).getText().toString().trim();

        if (!InputValidator.validatePassword(binding.newPassword, newPass) ||
                !InputValidator.validateConfirmPassword(binding.confirmPassword, newPass, confirmPass)) {
            return;
        }

        viewModel.updateUserPassword(oldPass, newPass).observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                if (result.getStatus() == Result.Status.LOADING) {
                    binding.progressUpdatePassword.setVisibility(View.VISIBLE);
                } else {
                    binding.progressUpdatePassword.setVisibility(View.GONE);
                }

                if (result.getStatus() == Result.Status.SUCCESS) {
                    showToast("Password changed successfully!");
                } else if (result.getStatus() == Result.Status.ERROR) {
                    showToast("Failed to change password: " + result.getErrorMessage());
                }
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
