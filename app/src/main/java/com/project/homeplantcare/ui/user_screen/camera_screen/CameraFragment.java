package com.project.homeplantcare.ui.user_screen.camera_screen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentCameraBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CameraFragment extends BaseFragment<FragmentCameraBinding> {

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
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

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = createImageFile();
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(requireContext(),
                        "com.project.homeplantcare.fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, REQUEST_CAMERA);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private File createImageFile() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            return File.createTempFile("IMG_" + timeStamp, ".jpg", storageDir);
        } catch (IOException e) {
            Log.e("CameraFragment", "Error creating image file", e);
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Glide.with(this).load(imageUri).into(binding.imgPreview);
            } else if (requestCode == REQUEST_GALLERY && data != null) {
                imageUri = data.getData();
                Glide.with(this).load(imageUri).into(binding.imgPreview);
            }
        }
    }

    private void uploadImage() {
        if (imageUri == null) {
            Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.uploadImage(imageUri).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                String plantName = result.getData(); // Get plant name from API
                searchPlantByNameAndNavigate(plantName);
            } else if (result.getStatus() == Result.Status.ERROR) {
                Toast.makeText(requireContext(), "Upload failed: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchPlantByNameAndNavigate(String plantName) {
        viewModel.getPlantIdByName(plantName).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                String plantId = result.getData();

                Bundle bundle = new Bundle();
                bundle.putBoolean("isAnaylsis", true);
                bundle.putString("plantId", plantId);

                Navigation.findNavController(requireView()).navigate(R.id.action_cameraFragment_to_plantDetailsFragment2, bundle);
            } else {
                Toast.makeText(requireContext(), "Plant not found: " + plantName, Toast.LENGTH_SHORT).show();
            }
        });
    }
}