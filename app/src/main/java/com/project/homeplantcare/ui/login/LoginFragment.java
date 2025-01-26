package com.project.homeplantcare.ui.login;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentLoginBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

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
        // Set toolbar title
        setToolbarTitle("Login");
        // Show back button
        showBackButton(false);
        // Checkbox logic
        binding.cbAdmin.setChecked(true);
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

        binding.btnLogin.setOnClickListener(v -> {
            // if not selected check whether show toast
            if (!binding.cbAdmin.isChecked() && !binding.cbUser.isChecked()) {
                showToast("Please select a user type");
                return;
            }
            // Login logic
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
        });

        binding.btnRegister.setOnClickListener(v -> {
            // Register logic
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
        });

    }
    private void showToast(String message) {
        // Show toast
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}