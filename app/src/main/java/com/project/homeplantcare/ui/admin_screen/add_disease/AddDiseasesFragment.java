package com.project.homeplantcare.ui.admin_screen.add_disease;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentAddDiseasesBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddDiseasesFragment extends BaseFragment<FragmentAddDiseasesBinding> {

    private AddDiseasesViewModel viewModel;
    private String diseaseId = null;

    @Override
    protected String getTAG() {
        return "AddDiseasesFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_add_diseases;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(AddDiseasesViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();

        setToolbarVisibility(true);
        diseaseId = getArguments() != null ? getArguments().getString("diseaseId") : null;

        if (diseaseId != null) {
            setToolbarTitle("Edit Disease");
            binding.btnSave.setText("Update Disease");
            viewModel.getDiseaseById(diseaseId);
        } else {
            setToolbarTitle("Add Disease");
            binding.btnSave.setText("Save Disease");
        }

        showBackButton(true);
        binding.setLifecycleOwner(this);

        setupListeners();
        observeViewModel();
    }

    private void setupListeners() {
        binding.btnSave.setOnClickListener(view -> {
            if (diseaseId != null) {
                viewModel.updateDisease(diseaseId);
            } else {
                viewModel.addDisease();
            }
        });

        // Manually bind text change listeners to update the ViewModel
        binding.etDiseaseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No operation
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setDiseaseName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No operation
            }
        });

        binding.etDiseaseSymptoms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No operation
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setDiseaseSymptoms(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No operation
            }
        });

        binding.etDiseaseRemedies.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // No operation
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                viewModel.setDiseaseRemedies(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No operation
            }
        });
    }

    private void observeViewModel() {
        viewModel.getIsDiseaseSaved().observe(getViewLifecycleOwner(), isSaved -> {
            if (isSaved) {
                showToast(diseaseId != null ? "Disease updated successfully!" : "Disease added successfully!");
                Navigation.findNavController(requireView()).navigateUp();
            } else {
                showToast("Please fill all fields correctly.");
            }
        });

        viewModel.getDiseaseItemLiveData().observe(getViewLifecycleOwner(), diseaseItem -> {
            if (diseaseItem != null) {
                binding.etDiseaseName.setText(diseaseItem.getName());
                binding.etDiseaseSymptoms.setText(diseaseItem.getSymptoms());
                binding.etDiseaseRemedies.setText(diseaseItem.getRemedies());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
