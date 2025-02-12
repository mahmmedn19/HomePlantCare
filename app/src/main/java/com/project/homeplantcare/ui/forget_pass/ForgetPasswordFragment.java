package com.project.homeplantcare.ui.forget_pass;

import android.view.View;
import android.widget.Toast;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentForgetPasswordBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.InputValidator;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ForgetPasswordFragment extends BaseFragment<FragmentForgetPasswordBinding> {

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
        return null;
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
    }

    private void handleResetPassword() {
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();

        // Validate email before proceeding
        if (!InputValidator.validateEmail(binding.emailInputLayout, email)) {
            return; // Stop further processing if validation fails
        }

        // Proceed with password reset logic
        showToast("Password reset instructions sent to: " + email);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
