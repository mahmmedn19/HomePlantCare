package com.project.homeplantcare.ui.admin_screen.add_plant_admin;

import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.DialogDiseaseSelectionBinding;
import com.project.homeplantcare.databinding.FragmentAddPlantAdminBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.DialogUtils;

import java.util.ArrayList;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddPlaintAdminFragment extends BaseFragment<FragmentAddPlantAdminBinding> {

    private AddPlantViewModel viewModel;
    private DiseasesSelectionAdapter selectedDiseasesAdapter;

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

        setupRecyclerView();
        setupListeners();
        observeViewModel();
    }

    private void setupRecyclerView() {
        binding.recyclerDiseases.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        selectedDiseasesAdapter = new DiseasesSelectionAdapter(new ArrayList<>(), viewModel, false);
        binding.recyclerDiseases.setAdapter(selectedDiseasesAdapter);
    }


    private void setupListeners() {
        binding.save.setOnClickListener(v -> {
            viewModel.savePlant();
        });

        binding.btnUploadImage.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Upload image functionality not implemented", Toast.LENGTH_SHORT).show();
        });

        // 🔹 Open Disease Selection Dialog
        binding.btnSelectDiseases.setOnClickListener(v -> showDiseaseSelectionDialog());
    }

    private void showDiseaseSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DialogDiseaseSelectionBinding dialogBinding = DialogDiseaseSelectionBinding.inflate(LayoutInflater.from(requireContext()));

        builder.setView(dialogBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();

        // ✅ Enable clicking inside dialog
        DiseasesSelectionAdapter allDiseasesAdapter = new DiseasesSelectionAdapter(new ArrayList<>(Objects.requireNonNull(viewModel.getDiseaseList().getValue())), viewModel, true);
        dialogBinding.recyclerDiseases.setLayoutManager(new LinearLayoutManager(requireContext()));
        dialogBinding.recyclerDiseases.setAdapter(allDiseasesAdapter);

        // 🔎 Search functionality
        dialogBinding.etSearchDiseases.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                allDiseasesAdapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Confirm selection button
        dialogBinding.btnConfirmSelection.setOnClickListener(v -> {
            selectedDiseasesAdapter.updateList(viewModel.getSelectedDiseases().getValue());
            dialog.dismiss();
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

        viewModel.getSelectedDiseases().observe(getViewLifecycleOwner(), selectedDiseases -> {
            selectedDiseasesAdapter.updateList(selectedDiseases);
        });
    }
}
