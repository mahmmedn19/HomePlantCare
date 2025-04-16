package com.project.homeplantcare.ui.register;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentRegisterBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.InputValidator;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterFragment extends BaseFragment<FragmentRegisterBinding> {

    private RegisterViewModel viewModel;

    @Override
    protected String getTAG() {
        return "RegisterFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_register;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Register");
        showBackButton(true);

        // Enable real-time error removal on input fields
        InputValidator.clearErrorOnTextChange(binding.nameInputLayout);
        InputValidator.clearErrorOnTextChange(binding.emailInputLayout);
        InputValidator.clearErrorOnTextChange(binding.passwordInputLayout);
        InputValidator.clearErrorOnTextChange(binding.confirmPasswordInputLayout);

        binding.btnRegister.setOnClickListener(v -> handleRegister());

        binding.tvLogin.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
    }


    private void handleRegister() {
        String username = Objects.requireNonNull(binding.nameInputLayout.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(binding.emailInputLayout.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(binding.passwordInputLayout.getEditText()).getText().toString().trim();
        String confirmPassword = Objects.requireNonNull(binding.confirmPasswordInputLayout.getEditText()).getText().toString().trim();

        // Validate all fields before proceeding
        boolean isUsernameValid = InputValidator.validateUsername(binding.nameInputLayout, username);
        boolean isEmailValid = InputValidator.validateEmail(binding.emailInputLayout, email);
        boolean isPasswordValid = InputValidator.validatePassword(binding.passwordInputLayout, password);
        boolean isConfirmPasswordValid = InputValidator.validateConfirmPassword(binding.confirmPasswordInputLayout, password, confirmPassword);

        if (!isUsernameValid || !isEmailValid || !isPasswordValid || !isConfirmPasswordValid) {
            return; // Stop further processing if any validation fails
        }


        // Call the registerUser method in ViewModel
        observeRegister(viewModel.registerUser(email, password, username));
    }


    private void observeRegister(LiveData<Result<String>> registerResult) {
        registerResult.observe(getViewLifecycleOwner(), result -> {
            if (result == null) return;

            if (result.getStatus() == Result.Status.LOADING) {
                // Show loading spinner and disable the register button
                showToast("Registering...");
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnRegister.setEnabled(false);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnRegister.setEnabled(true);
                showToast(result.getData());
                // Navigate to HomeFragment after successful registration
                Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_homeFragment);
            } else if (result.getStatus() == Result.Status.ERROR) {
                // Hide loading spinner and enable the register button
                binding.progressBar.setVisibility(View.GONE);
                binding.btnRegister.setEnabled(true);
                showToast(result.getErrorMessage());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}

