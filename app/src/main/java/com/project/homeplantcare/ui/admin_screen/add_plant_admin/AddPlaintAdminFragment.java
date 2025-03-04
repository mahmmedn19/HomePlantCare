package com.project.homeplantcare.ui.admin_screen.add_plant_admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.databinding.DialogDiseaseSelectionBinding;
import com.project.homeplantcare.databinding.FragmentAddPlantAdminBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.DialogUtils;
import com.project.homeplantcare.utils.InputValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddPlaintAdminFragment extends BaseFragment<FragmentAddPlantAdminBinding> {

    private AddPlantViewModel viewModel;
    private DiseasesSelectionAdapter selectedDiseasesAdapter;
    private String plantId = null;
    private Bitmap selectedBitmap;

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
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);

        // Initialize ViewModel FIRST
        viewModel = new ViewModelProvider(this).get(AddPlantViewModel.class);

        plantId = getArguments() != null ? getArguments().getString("plantId") : null;

        if (plantId != null) {
            setToolbarTitle("Edit Plant");
            binding.save.setText("Update Plant");
            viewModel.getPlantById(plantId);  // Now it should not be null
        } else {
            setToolbarTitle("Add Plant");
            binding.save.setText("Save Plant");
        }

        showBackButton(true);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setupRecyclerView();
        setupListeners();
        observeViewModel();

        if (plantId != null) {
            viewModel.getPlantItemLiveData().observe(getViewLifecycleOwner(), plant -> {
                if (plant != null) {
                    populatePlantData(plant);
                }
            });
        }
    }


    private void setupRecyclerView() {
        binding.recyclerDiseases.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        selectedDiseasesAdapter = new DiseasesSelectionAdapter(new ArrayList<>(), viewModel, false);
        binding.recyclerDiseases.setAdapter(selectedDiseasesAdapter);
    }

    private void setupListeners() {
        binding.save.setOnClickListener(v -> {
            if (validateFields()) {
                if (plantId != null) {
                    viewModel.updatePlant(plantId, getPlantFromForm());
                } else {
                    viewModel.addPlant(getPlantFromForm());
                }
            } else {
                showToast("Please fill all required fields.");
            }
        });

        binding.btnUploadImage.setOnClickListener(v -> openGallery());

        binding.btnSelectDiseases.setOnClickListener(v -> showDiseaseSelectionDialog());
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private PlantItem getPlantFromForm() {
        // Create a new PlantItem object from the form data
        PlantItem plant = new PlantItem();
        plant.setName(Objects.requireNonNull(binding.etPlantName.getText()).toString());
        plant.setDescription(Objects.requireNonNull(binding.etPlantDescription.getText()).toString());
        plant.setLightRequirements(Objects.requireNonNull(binding.etLightRequirement.getText()).toString());
        plant.setWaterRequirements(Objects.requireNonNull(binding.etWaterRequirement.getText()).toString());
        plant.setSoilRequirements(Objects.requireNonNull(binding.etSoilRequirement.getText()).toString());
        plant.setWeatherRequirements(Objects.requireNonNull(binding.etWeatherRequirement.getText()).toString());
        plant.setDiseases(viewModel.getSelectedDiseases().getValue());

        // If updating and no new image is selected, retain the existing image
        if (selectedBitmap != null) {
            String encodedImage = ImageUtils.encodeImageToBase64(selectedBitmap);
            plant.setImageResId(encodedImage);
        } else if (plantId != null) {
            // Preserve the existing image in case of an update
            PlantItem existingPlant = viewModel.getPlantItemLiveData().getValue();
            if (existingPlant != null) {
                plant.setImageResId(existingPlant.getImageResId());
            }
        }

        return plant;
    }

    private void populatePlantData(PlantItem plant) {
        // Populate the form fields with the existing plant data
        binding.etPlantName.setText(plant.getName());
        binding.etPlantDescription.setText(plant.getDescription());
        binding.etLightRequirement.setText(plant.getLightRequirements());
        binding.etWaterRequirement.setText(plant.getWaterRequirements());
        binding.etSoilRequirement.setText(plant.getSoilRequirements());
        binding.etWeatherRequirement.setText(plant.getWeatherRequirements());


        // If updating and no new image is selected, retain the existing image
        if (selectedBitmap != null) {
            String encodedImage = ImageUtils.encodeImageToBase64(selectedBitmap);
            plant.setImageResId(encodedImage);
        } else if (plantId != null) {
            // Preserve the existing image in case of an update
            PlantItem existingPlant = viewModel.getPlantItemLiveData().getValue();
            if (existingPlant != null) {
                plant.setImageResId(existingPlant.getImageResId());
            }
        }
        // Set the diseases (if any)
        selectedDiseasesAdapter.updateList(plant.getDiseases());
    }

    private void showDiseaseSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        DialogDiseaseSelectionBinding dialogBinding = DialogDiseaseSelectionBinding.inflate(LayoutInflater.from(requireContext()));

        builder.setView(dialogBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();

        DiseasesSelectionAdapter allDiseasesAdapter = new DiseasesSelectionAdapter(new ArrayList<>(Objects.requireNonNull(viewModel.getDiseaseList().getValue())), viewModel, true);
        dialogBinding.recyclerDiseases.setLayoutManager(new LinearLayoutManager(requireContext()));
        dialogBinding.recyclerDiseases.setAdapter(allDiseasesAdapter);

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

        dialogBinding.btnConfirmSelection.setOnClickListener(v -> {
            selectedDiseasesAdapter.updateList(viewModel.getSelectedDiseases().getValue());
            dialog.dismiss();
        });
    }
    private boolean validateFields() {
        boolean isValidName = InputValidator.validateField(binding.plantNameLayout, binding.etPlantName.getText().toString(), "Plant name is required");
        boolean isValidDescription = InputValidator.validateField(binding.plantDescriptionLayout, binding.etPlantDescription.getText().toString(), "Description is required");
        boolean isValidLight = InputValidator.validateField(binding.lightRequirementLayout, binding.etLightRequirement.getText().toString(), "Light requirement is required");
        boolean isValidWater = InputValidator.validateField(binding.waterRequirementLayout, binding.etWaterRequirement.getText().toString(), "Water requirement is required");
        boolean isValidSoil = InputValidator.validateField(binding.soilRequirementLayout, binding.etSoilRequirement.getText().toString(), "Soil requirement is required");
        boolean isValidWeather = InputValidator.validateField(binding.weatherRequirementLayout, binding.etWeatherRequirement.getText().toString(), "Weather requirement is required");

        return isValidName && isValidDescription && isValidLight && isValidWater && isValidSoil && isValidWeather;
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

        viewModel.getSelectedDiseases().observe(getViewLifecycleOwner(), selectedDiseases -> selectedDiseasesAdapter.updateList(selectedDiseases));
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
}
