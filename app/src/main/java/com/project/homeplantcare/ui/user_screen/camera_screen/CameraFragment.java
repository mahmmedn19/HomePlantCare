package com.project.homeplantcare.ui.user_screen.camera_screen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentCameraBinding;
import com.project.homeplantcare.ui.base.BaseFragment;
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

        binding.btnCapture.setOnClickListener(v -> openCamera());
        binding.btnUpload.setOnClickListener(v -> openGallery());
        binding.btnAnalyze.setOnClickListener(v -> uploadImage());
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

    private void openCamera() {
        ImagePicker.with(this)
                .cameraOnly()
                .crop()
                .compress(1024) // Compress to 1MB
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    imagePickerLauncher.launch(intent);
                    return null; // Java compatibility fix
                });
    }

    private void openGallery() {
        ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent(intent -> {
                    imagePickerLauncher.launch(intent);
                    return null; // Java compatibility fix
                });
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
            if (result.getStatus() == Result.Status.SUCCESS) {
                String plantName = result.getData(); // Get plant name from API
                searchPlantByNameAndNavigate(plantName);
            } else {
                Toast.makeText(requireContext(), "Upload failed: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchPlantByNameAndNavigate(String plantName) {
        viewModel.getPlantIdByName(plantName).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                String plantId = result.getData().getPlantId(); // ✅ Fixed reference to `plantId`

                Bundle bundle = new Bundle();
                bundle.putBoolean("isAnalysis", true);
                bundle.putString("plantId", plantId);

                Navigation.findNavController(requireView()).navigate(R.id.action_cameraFragment_to_plantDetailsFragment2, bundle);
            } else {
                Toast.makeText(requireContext(), "Plant not found: " + plantName, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
