package com.project.homeplantcare.ui.admin_screen.profile_details;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentAdminProfileDetailsBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AdminProfileDetailsFragment extends BaseFragment<FragmentAdminProfileDetailsBinding> {


    @Override
    protected String getTAG() {
        return "AdminProfileDetailsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_admin_profile_details;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Profile Details");
        showBackButton(true);



    }
}