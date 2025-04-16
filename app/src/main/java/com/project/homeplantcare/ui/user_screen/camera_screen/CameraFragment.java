package com.project.homeplantcare.ui.user_screen.camera_screen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentCameraBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.utils.DialogUtils;
import com.project.homeplantcare.utils.FileUtils;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CameraFragment extends BaseFragment<FragmentCameraBinding> {

    private Uri imageUri;
    private CameraViewModel viewModel;

    @Override
    protected String getTAG() {
        return "CameraFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_camera;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(CameraViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("AI Camera");
        showBackButton(false);

        viewModel = new ViewModelProvider(this).get(CameraViewModel.class);
        binding.btnUpload.setOnClickListener(v -> openGallery());
        binding.btnAnalyze.setOnClickListener(v -> uploadImage());

        // Hide progress on start
        binding.progressAnalysis.setVisibility(View.GONE);
    }

    // ✅ Register activity result launcher for ImagePicker
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    if (imageUri != null) {
                        Glide.with(this).load(imageUri).into(binding.imgPreview);
                    }
                } else {
                    Toast.makeText(requireContext(), "Image selection failed", Toast.LENGTH_SHORT).show();
                }
            });



    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void uploadImage() {
        if (imageUri == null) {
            Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Convert Uri to File
        File imageFile = FileUtils.getFileFromUri(requireContext(), imageUri);
        if (imageFile == null || !imageFile.exists()) {
            Toast.makeText(requireContext(), "Failed to process image file", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Upload image as File
        viewModel.uploadImage(imageFile).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.progressAnalysis.setVisibility(View.VISIBLE);
                binding.tvAnalysisResult.setText("Analyzing...");
            }
            else if (result.getStatus() == Result.Status.SUCCESS) {
                binding.progressAnalysis.setVisibility(View.GONE);
                String plantName = result.getData().first;
                boolean hasDetails = result.getData().second;
                // ✅ If Plant is "Unknown", show "Not Found" dialog immediately
                if (plantName.equalsIgnoreCase("Unknown")) {
                    showNotFoundDialog();
                    binding.tvAnalysisResult.setText("Plant Not Found");
                    binding.progressAnalysis.setVisibility(View.GONE);
                    return;
                }

                binding.tvAnalysisResult.setText("Detected Plant: " + plantName);
                if (hasDetails) {
                    showNavigationDialog(plantName); // ✅ Show dialog to navigate to plant details
                } else {
                    showNavigationDialogNoPlantDetails(plantName); // ✅ Show dialog to search for plant details
                }
                // ✅ Call getPlantIdByName only if plantName is successfully received
            } else if (result.getStatus() == Result.Status.ERROR) {
                binding.progressAnalysis.setVisibility(View.GONE);
                binding.tvAnalysisResult.setText("Failed to analyze image");
            }
        });

    }

    private void showNavigationDialog(String plantName) {
        DialogUtils.showConfirmationDialog(
                requireContext(),
                "Plant Analysis",
                "We detected the plant: " + plantName + ". Do you want to view its details?",
                "Yes", "No",
                (dialog, which) -> searchPlantByNameAndNavigate(plantName),
                null
        );
    }
    // not found plant details
    private void showNavigationDialogNoPlantDetails(String plantName) {
        DialogUtils.showConfirmationDialog(
                requireContext(),
                "Plant Analysis",
                "No plant details found for: " + plantName + " try Another!",
                "Yes", "No",
                (dialog, which) -> Toast.makeText(requireContext(), "No plant details found.", Toast.LENGTH_SHORT).show(),
                null // Negative button will still appear but default to dismissing the dialog
        );
    }

    private void showNotFoundDialog() {
        DialogUtils.showConfirmationDialog(
                requireContext(),
                "Plant Not Found",
                "The detected plant is unknown. Try selecting another image.",
                "OK", null,
                (dialog, which) -> resetImageSelection()
        );
    }

    private void searchPlantByNameAndNavigate(String plantName) {
        viewModel.getPlantIdByName(plantName).observe(getViewLifecycleOwner(), plantResult -> {
            if (plantResult.getStatus() == Result.Status.LOADING) {
                binding.progressAnalysis.setVisibility(View.VISIBLE);
            }
            else if (plantResult.getStatus() == Result.Status.SUCCESS) {
                binding.progressAnalysis.setVisibility(View.GONE);
                String plantId = plantResult.getData().getPlantId();

                Bundle bundle = new Bundle();
                bundle.putBoolean("isAnalysis", true);
                bundle.putString("plantId", plantId);
                resetImageSelection();
                Navigation.findNavController(requireView()).navigate(R.id.action_cameraFragment_to_plantDetailsFragment2, bundle);
            }
            else if (plantResult.getStatus() == Result.Status.ERROR) {
                binding.progressAnalysis.setVisibility(View.GONE);
            }
        });
    }


    private void resetImageSelection() {
        imageUri = null;
        binding.imgPreview.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera, null));
        binding.progressAnalysis.setVisibility(View.GONE);
    }

}
