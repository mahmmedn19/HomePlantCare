package com.project.homeplantcare.ui.admin_screen.manage_user;

import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.User;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentManageUserBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageUserFragment extends BaseFragment<FragmentManageUserBinding>  {

    private UserViewModel userViewModel;
    private ManageUserAdapter userAdapter;

    @Override
    protected String getTAG() {
        return "ManageUserFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_user;
    }

    @Override
    protected ViewModel getViewModel() {
        return userViewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Manage Users");
        showBackButton(false);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        binding.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(requireContext()));

        loadUsers();
    }

    private void loadUsers() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.recyclerViewUsers.setVisibility(View.GONE);
        binding.emptyStateLayout.setVisibility(View.GONE);

        userViewModel.getAllUsers().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                List<User> users = result.getData();
                if (users.isEmpty()) {
                    binding.emptyStateLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerViewUsers.setVisibility(View.VISIBLE);
                    userAdapter = new ManageUserAdapter(users);
                    binding.recyclerViewUsers.setAdapter(userAdapter);
                }
                binding.progressBar.setVisibility(View.GONE);
            } else if (result.getStatus() == Result.Status.ERROR) {
                binding.emptyStateLayout.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }



}