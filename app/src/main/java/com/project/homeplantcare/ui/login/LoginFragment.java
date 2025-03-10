package com.project.homeplantcare.ui.login;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentLoginBinding;
import com.project.homeplantcare.ui.admin_screen.AdminMainActivity;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.ui.user_screen.UserMainActivity;
import com.project.homeplantcare.utils.InputValidator;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends BaseFragment<FragmentLoginBinding> {

    private LoginViewModel viewModel;


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
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        return viewModel;
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
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();

        // Validate email and password
        if (!InputValidator.validateEmail(binding.emailInputLayout, email) ||
                !InputValidator.validateData(binding.passwordInputLayout, password)) {
            return;
        }

        // Show loading spinner before making the login request
        binding.progressBar.setVisibility(View.VISIBLE);

        // Check which user type (admin or user) is selected and perform login accordingly
        if (binding.cbAdmin.isChecked()) {
            observeLogin(viewModel.loginAdmin(email, password), AdminMainActivity.class, "Admin login successful!");
        } else if (binding.cbUser.isChecked()) {
            observeLogin(viewModel.loginUser(email, password), UserMainActivity.class, "User login successful!");
        } else {
            // Show a toast if no user type is selected
            showToast("Please select a user type");
        }
    }

    private void observeLogin(LiveData<Result<String>> loginResult, Class<?> targetActivity, String successMessage) {
        loginResult.observe(getViewLifecycleOwner(), result -> {
            // Handle the different states of the login result
            if (result.getStatus() == Result.Status.LOADING) {
                // Show loading spinner and disable the register button
                showToast("Logging in...");  // Update toast with loading message
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.tvInvalidEmail.setVisibility(View.GONE);
                binding.btnRegister.setEnabled(false);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnRegister.setEnabled(true);
                binding.tvInvalidEmail.setVisibility(View.GONE);
                showToast(successMessage);
                startActivity(new Intent(requireContext(), targetActivity));
                requireActivity().finish();
            } else if (result.getStatus() == Result.Status.ERROR) {
                // Hide loading spinner and enable the register button
                binding.progressBar.setVisibility(View.GONE);
                binding.btnRegister.setEnabled(true);
                binding.tvInvalidEmail.setVisibility(View.VISIBLE);
                binding.tvInvalidEmail.setText(result.getErrorMessage());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}