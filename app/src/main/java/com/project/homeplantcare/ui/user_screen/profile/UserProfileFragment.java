package com.project.homeplantcare.ui.user_screen.profile;

import android.content.Intent;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentUserProfileBinding;
import com.project.homeplantcare.ui.MainActivity;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.DialogUtils;
import com.project.homeplantcare.utils.InputValidator;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserProfileFragment extends BaseFragment<FragmentUserProfileBinding> {

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
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("User Profile");
        showBackButton(false);

        // Enable real-time error removal for text fields
        InputValidator.clearErrorOnTextChange(binding.userName);
        InputValidator.clearErrorOnTextChange(binding.oldPassword);
        InputValidator.clearErrorOnTextChange(binding.newPassword);
        InputValidator.clearErrorOnTextChange(binding.confirmPassword);

        binding.btnUpdateProfile.setOnClickListener(v -> handleUpdateProfile());
        binding.btnSaveNewPassword.setOnClickListener(v -> handleChangePassword());
        binding.btnLogout.setOnClickListener(v -> {
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
            );
        });
    }

    private void handleUpdateProfile() {
        String userName = binding.userName.getEditText().getText().toString().trim();

        if (!InputValidator.validateUsername(binding.userName, userName)) {
            return; // Stop if validation fails
        }

        showToast("Profile updated successfully!");
    }

    private void handleChangePassword() {
        String oldPass = Objects.requireNonNull(binding.oldPassword.getEditText()).getText().toString().trim();
        String newPass = Objects.requireNonNull(binding.newPassword.getEditText()).getText().toString().trim();
        String confirmPass = Objects.requireNonNull(binding.confirmPassword.getEditText()).getText().toString().trim();

        if (!InputValidator.validatePassword(binding.newPassword, newPass) ||
                !InputValidator.validateConfirmPassword(binding.confirmPassword, newPass, confirmPass)) {
            return; // Stop if validation fails
        }

        showToast("Password changed successfully!");
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
