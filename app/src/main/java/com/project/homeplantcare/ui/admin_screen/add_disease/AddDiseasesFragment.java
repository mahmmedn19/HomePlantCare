package com.project.homeplantcare.ui.admin_screen.add_disease;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentAddDiseasesBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.InputValidator;

import java.io.IOException;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddDiseasesFragment extends BaseFragment<FragmentAddDiseasesBinding> {

    private AddDiseasesViewModel viewModel;
    private String diseaseId;
    private Bitmap selectedBitmap;

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
        binding.btnUploadImage.setOnClickListener(v -> openGallery());
        setToolbarTitle(diseaseId != null ? "Edit Disease" : "Add Disease");
        binding.btnSave.setText(diseaseId != null ? "Update Disease" : "Save Disease");
        showBackButton(true);

        if (diseaseId != null) {
            viewModel.getDiseaseById(diseaseId).observe(getViewLifecycleOwner(), result -> {
                if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                    populateFields(result.getData());
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
                if (selectedBitmap != null) {
                    String encodedImage = ImageUtils.encodeImageToBase64(selectedBitmap);
                    diseaseItem.setImageResId(encodedImage);
                } else if (diseaseId != null) {
                    // Preserve the existing image if updating without a new image
                    DiseaseItem existingDisease = Objects.requireNonNull(viewModel.getDiseaseById(diseaseId).getValue()).getData();
                    if (existingDisease != null) {
                        diseaseItem.setImageResId(existingDisease.getImageResId());
                    }
                }
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
        if (disease.getImageResId() != null && !disease.getImageResId().isEmpty()) {
            Bitmap decodedBitmap = ImageUtils.decodeBase64ToImage(disease.getImageResId());
            binding.imgPreview.setImageBitmap(decodedBitmap);
        }
    }

    private boolean validateFields() {
        boolean isValidName = InputValidator.validateField(binding.tilDiseaseName, Objects.requireNonNull(binding.etDiseaseName.getText()).toString(), "Name is required");
        boolean isValidSymptoms = InputValidator.validateField(binding.tilDiseaseSymptoms, Objects.requireNonNull(binding.etDiseaseSymptoms.getText()).toString(), "Symptoms are required");
        boolean isValidRemedies = InputValidator.validateField(binding.tilDiseaseRemedies, Objects.requireNonNull(binding.etDiseaseRemedies.getText()).toString(), "Remedies are required");

        return isValidName && isValidSymptoms && isValidRemedies;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                        binding.imgPreview.setImageBitmap(selectedBitmap);
                    } catch (IOException e) {
                        Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}