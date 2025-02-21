package com.project.homeplantcare.ui.admin_screen.add_disease;

import android.widget.Toast;
import android.view.View;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentAddDiseasesBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddDiseasesFragment extends BaseFragment<FragmentAddDiseasesBinding> {

    private AddDiseasesViewModel viewModel;
    private String diseaseId;

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
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        diseaseId = getArguments() != null ? getArguments().getString("diseaseId") : null;
        setupToolbar();
        setupListeners();
        observeViewModel();
    }

    private void setupToolbar() {
        setToolbarVisibility(true);
        setToolbarTitle(diseaseId != null ? "Edit Disease" : "Add Disease");
        binding.btnSave.setText(diseaseId != null ? "Update Disease" : "Save Disease");
        showBackButton(true);

        if (diseaseId != null) {
            viewModel.getDiseaseById(diseaseId).observe(getViewLifecycleOwner(), result -> {
                if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                    populateFields(result.getData());
                } else {
                    showToast("Error: " + result.getErrorMessage());
                }
            });
        }
    }

    private void setupListeners() {
        binding.btnSave.setOnClickListener(view -> {
            if (validateFields()) {
                DiseaseItem diseaseItem = new DiseaseItem();
                diseaseItem.setName(binding.etDiseaseName.getText().toString());
                diseaseItem.setSymptoms(binding.etDiseaseSymptoms.getText().toString());
                diseaseItem.setRemedies(binding.etDiseaseRemedies.getText().toString());

                if (diseaseId == null) {
                    viewModel.addDisease(diseaseItem);
                } else {
                    viewModel.updateDisease(diseaseId, diseaseItem);
                }
            } else {
                showToast("Please fill all fields correctly.");
            }
        });
    }

    private void observeViewModel() {
        viewModel.getOperationResult().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                showToast(result.getData());
                Navigation.findNavController(requireView()).navigateUp();
            } else if (result.getStatus() == Result.Status.ERROR) {
                showToast("Error: " + result.getErrorMessage());
            }
        });
    }

    private void populateFields(DiseaseItem disease) {
        binding.etDiseaseName.setText(disease.getName());
        binding.etDiseaseSymptoms.setText(disease.getSymptoms());
        binding.etDiseaseRemedies.setText(disease.getRemedies());
    }

    private boolean validateFields() {
        return !isEmpty(binding.etDiseaseName.getText().toString()) &&
                !isEmpty(binding.etDiseaseSymptoms.getText().toString()) &&
                !isEmpty(binding.etDiseaseRemedies.getText().toString());
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}