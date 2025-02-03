package com.project.homeplantcare.ui.admin_screen.add_plant_admin;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentAddPlantAdminBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.DialogUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddPlaintAdminFragment extends BaseFragment<FragmentAddPlantAdminBinding> {

    private AddPlantViewModel viewModel;

    @Override
    protected String getTAG() {
        return "AddPlantFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_add_plant_admin;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Add Plant");
        showBackButton(true);

        viewModel = new ViewModelProvider(this).get(AddPlantViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupListeners();
        observeViewModel();
    }

    private void setupListeners() {
        binding.save.setOnClickListener(v -> {
            viewModel.savePlant();
        });

        binding.btnUploadImage.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Upload image functionality not implemented", Toast.LENGTH_SHORT).show();
        });
    }

    private void observeViewModel() {
        viewModel.getIsPlantSaved().observe(getViewLifecycleOwner(), isSaved -> {
            if (isSaved) {
                DialogUtils.showCustomDialog(requireContext(), "Success", "Plant added successfully!");
                Navigation.findNavController(requireView()).navigateUp();
            } else {
                Toast.makeText(requireContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
