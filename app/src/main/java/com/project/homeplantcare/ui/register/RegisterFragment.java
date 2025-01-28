package com.project.homeplantcare.ui.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentRegisterBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

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
        // Set toolbar title
        setToolbarTitle("Register");
        // Show back button
        showBackButton(true);
        binding.btnRegister.setOnClickListener(v -> {
            // Register logic to home
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_homeFragment);
        });

        binding.tvLogin.setOnClickListener(v -> {
            // Navigate to login screen
            Navigation.findNavController(v).navigateUp();
        });
    }
}