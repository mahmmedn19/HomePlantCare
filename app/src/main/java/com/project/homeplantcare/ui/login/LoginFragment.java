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

        if (!InputValidator.validateEmail(binding.emailInputLayout, email) ||
                !InputValidator.validateData(binding.passwordInputLayout, password)) {
            return;
        }

        if (binding.cbAdmin.isChecked()) {
            observeLogin(viewModel.loginAdmin(email, password), AdminMainActivity.class, "Admin login successful!");
        } else if (binding.cbUser.isChecked()) {
            observeLogin(viewModel.loginUser(email, password), UserMainActivity.class, "User login successful!");
        } else {
            showToast("Please select a user type");
        }
    }

    private void observeLogin(LiveData<Result<String>> loginResult, Class<?> targetActivity, String successMessage) {
        loginResult.observe(getViewLifecycleOwner(), result -> {
            switch (result.getStatus()) {
                case SUCCESS:
                    showToast(successMessage);
                    startActivity(new Intent(requireContext(), targetActivity));
                    requireActivity().finish();
                    break;
                case ERROR:
                    showToast(result.getErrorMessage());
                    break;
                case LOADING:
                    showToast("Logging in...");
                    break;
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
