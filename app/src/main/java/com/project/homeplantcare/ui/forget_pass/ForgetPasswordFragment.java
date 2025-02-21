package com.project.homeplantcare.ui.forget_pass;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentForgetPasswordBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.InputValidator;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ForgetPasswordFragment extends BaseFragment<FragmentForgetPasswordBinding> {

    private ForgotPasswordViewModel viewModel;

    @Override
    protected String getTAG() {
        return "ForgetPasswordFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_forget_password;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Forget Password");
        showBackButton(true);

        // Enable real-time error removal when typing
        InputValidator.clearErrorOnTextChange(binding.emailInputLayout);

        binding.btnSendResetLink.setOnClickListener(v -> handleResetPassword());
        observeResetPasswordResult();
    }

    private void handleResetPassword() {
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();

        // Validate email before proceeding
        if (!InputValidator.validateEmail(binding.emailInputLayout, email)) {
            return; // Stop further processing if validation fails
        }
        viewModel.resetPassword(email);

        // Proceed with password reset logic
        showToast("Password reset instructions sent to: " + email);
    }

    private void observeResetPasswordResult() {
        viewModel.getResetPasswordResult().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                Toast.makeText(requireContext(), result.getData(), Toast.LENGTH_LONG).show();
                requireActivity().onBackPressed();
            } else if (result.getStatus() == Result.Status.ERROR) {
                Toast.makeText(requireContext(), "Error: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            } else if (result.getStatus() == Result.Status.LOADING) {
                binding.loadingProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
