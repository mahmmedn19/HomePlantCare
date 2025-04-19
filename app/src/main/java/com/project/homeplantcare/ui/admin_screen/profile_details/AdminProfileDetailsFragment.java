package com.project.homeplantcare.ui.admin_screen.profile_details;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.AdminProfile;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentAdminProfileDetailsBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AdminProfileDetailsFragment extends BaseFragment<FragmentAdminProfileDetailsBinding> {

    private AdminProfileViewModel viewModel;

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
        viewModel = new ViewModelProvider(this).get(AdminProfileViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Profile Details");
        showBackButton(true);

        observeAdminProfile();

        // Disable editing name field completely
        binding.etAdminName.setEnabled(false);
        binding.etAdminName.setFocusable(false);
        binding.etAdminName.setClickable(false);

    }

    private void observeAdminProfile() {
        viewModel.getAdminProfile().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.etAdminName.setEnabled(false);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                binding.progressBar.setVisibility(View.GONE);

                AdminProfile adminProfile = result.getData();
                if (adminProfile != null) {
                    binding.etAdminName.setText(adminProfile.getAdminName());
                    binding.etAdminEmail.setText(adminProfile.getAdminEmail());
                }

            } else if (result.getStatus() == Result.Status.ERROR) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}