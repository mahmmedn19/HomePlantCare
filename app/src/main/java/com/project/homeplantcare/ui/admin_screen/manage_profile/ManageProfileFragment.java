package com.project.homeplantcare.ui.admin_screen.manage_profile;

import android.content.Intent;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentManageProfileBinding;
import com.project.homeplantcare.ui.MainActivity;
import com.project.homeplantcare.ui.admin_screen.profile_details.AdminProfileViewModel;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.DialogUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageProfileFragment extends BaseFragment<FragmentManageProfileBinding> {
    private AdminProfileViewModel viewModel;

    @Override
    protected String getTAG() {
        return "ManageProfileFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_profile;
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
        setToolbarTitle("Manage Profile");
        showBackButton(false);

        binding.aiLinkLayout.setOnClickListener(v -> {
            // Navigate to Manage Links Fragment
            Navigation.findNavController(v).navigate(R.id.action_nav_manage_profile_to_nav_add_link);
        });
        binding.manageProfileLayout.setOnClickListener(v -> {
            // Navigate to Profile Details Fragment
            Navigation.findNavController(v).navigate(R.id.action_nav_manage_profile_to_nav_admin_profile_details);
        });

        binding.btnLogout.setOnClickListener(v -> {
            // Logout
            DialogUtils.showConfirmationDialog(
                    requireContext(),
                    "Logout",
                    "Are you sure you want to logout?",
                    "Yes", "Cancel",
                    (dialog, which) -> {
                        Intent intent = new Intent(requireContext(), MainActivity.class);
                        startActivity(intent);
                        requireActivity().finish();
                    }
            );
        });

        viewModel.getAdminProfile().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == com.project.homeplantcare.data.utils.Result.Status.SUCCESS) {
                binding.userName.setText(result.getData().getAdminName());
            }
        });

    }
}