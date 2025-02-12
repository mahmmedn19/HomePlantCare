package com.project.homeplantcare.ui.register;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentRegisterBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.InputValidator;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterFragment extends BaseFragment<FragmentRegisterBinding> {

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
        return null;
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

        // Registration success
        showToast("Registration successful!");

        // Navigate to HomeFragment after successful registration
        Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_homeFragment);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
