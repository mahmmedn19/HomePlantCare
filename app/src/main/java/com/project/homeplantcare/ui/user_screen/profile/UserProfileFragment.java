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


        viewModel.getUserProfile().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                if (result.getData() != null) {
                    binding.userName.getEditText().setText(result.getData().getUsername());
                    binding.userEmail.getEditText().setText(result.getData().getEmail());
                } else {
                    showToast("User data is null!");
                }
            }
        });

        binding.btnUpdateProfile.setOnClickListener(v -> handleUpdateProfile());
        binding.btnSaveNewPassword.setOnClickListener(v -> handleChangePassword());
    }

    private void handleUpdateProfile() {
        String newName = Objects.requireNonNull(binding.userName.getEditText()).getText().toString().trim();
        if (newName.isEmpty()) {
            showToast("Name cannot be empty.");
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