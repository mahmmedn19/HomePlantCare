package com.project.homeplantcare.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentLoginBinding;
import com.project.homeplantcare.ui.admin_screen.AdminMainActivity;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.ui.user_screen.UserMainActivity;
import com.project.homeplantcare.utils.InputValidator;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends BaseFragment<FragmentLoginBinding> {

    @Override
    protected String getTAG() {
        return "LoginFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_login;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Login");
        showBackButton(true);

        // Initialize real-time validation on email and password fields
        InputValidator.clearErrorOnTextChange(binding.emailInputLayout);
        InputValidator.clearErrorOnTextChange(binding.passwordInputLayout);

        binding.cbAdmin.setChecked(true);

        binding.tvForgotPassword.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        );

        binding.cbUser.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.cbAdmin.setChecked(false);
                binding.btnRegister.setVisibility(View.VISIBLE);
            }
        });

        binding.cbAdmin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.cbUser.setChecked(false);
                binding.btnRegister.setVisibility(View.GONE);
            }
        });

        binding.btnLogin.setOnClickListener(v -> handleLogin());

        binding.btnRegister.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
        );
    }

    private void handleLogin() {
        // Validate email and password before proceeding
        boolean isEmailValid = InputValidator.validateEmail(binding.emailInputLayout, Objects.requireNonNull(binding.etEmail.getText()).toString().trim());
        boolean isPasswordValid = InputValidator.validateData(binding.passwordInputLayout, Objects.requireNonNull(binding.etPassword.getText()).toString().trim());

        if (!isEmailValid || !isPasswordValid) {
            return; // Stop login if validation fails
        }

        if (!binding.cbAdmin.isChecked() && !binding.cbUser.isChecked()) {
            showToast("Please select a user type");
            return;
        }

        if (binding.cbAdmin.isChecked()) {
            Intent intent = new Intent(requireContext(), AdminMainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        } else {
            Intent intent = new Intent(requireContext(), UserMainActivity.class);
            startActivity(intent);
            requireActivity().finish();
            showToast("User login");
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
