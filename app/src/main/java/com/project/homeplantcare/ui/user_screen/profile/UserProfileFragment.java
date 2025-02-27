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
        return  viewModel;
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
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                Objects.requireNonNull(binding.userName.getEditText()).setText(result.getData().getUsername());
                Objects.requireNonNull(binding.userEmail.getEditText()).setText(result.getData().getEmail());
            }
        });

        viewModel.isUpdatingProfile().observe(getViewLifecycleOwner(), isLoading -> {
            binding.btnUpdateProfile.setEnabled(!isLoading);
            binding.progressUpdateProfile.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        viewModel.isUpdatingPassword().observe(getViewLifecycleOwner(), isLoading -> {
            binding.btnSaveNewPassword.setEnabled(!isLoading);
            binding.progressUpdatePassword.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        binding.btnUpdateProfile.setOnClickListener(v -> handleUpdateProfile());
        binding.btnSaveNewPassword.setOnClickListener(v -> handleChangePassword());
    }

    private void handleUpdateProfile() {
        String newName = binding.userName.getEditText().getText().toString().trim();

        if (!InputValidator.validateUsername(binding.userName, newName)) {
            return;
        }

        viewModel.updateUserProfile(newName).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                showToast("Profile updated successfully!");
                Navigation.findNavController(requireView()).popBackStack();
            }
        });
    }


    private void handleChangePassword() {
        String oldPass = binding.oldPassword.getEditText().getText().toString().trim();
        String newPass = binding.newPassword.getEditText().getText().toString().trim();
        String confirmPass = binding.confirmPassword.getEditText().getText().toString().trim();

        if (!InputValidator.validatePassword(binding.newPassword, newPass) ||
                !InputValidator.validateConfirmPassword(binding.confirmPassword, newPass, confirmPass)) {
            return;
        }

        viewModel.updateUserPassword(oldPass, newPass).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                showToast("Password changed successfully!");
            }
        });
    }


    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
