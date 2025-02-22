package com.project.homeplantcare.ui.plant_details;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentPlantDetailsBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlantDetailsFragment extends BaseFragment<FragmentPlantDetailsBinding> {

    private PlantDetailsViewModel viewModel;
    private DiseasesAdapter diseasesAdapter;
    private List<DiseaseItem> diseaseList = new ArrayList<>();

    @Override
    protected String getTAG() {
        return "PlantDetailsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_plant_details;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(PlantDetailsViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Plant Details");
        showBackButton(true);

        // Retrieve plantId from arguments
        String plantId = getArguments() != null ? getArguments().getString("plantId") : null;

        if (plantId != null) {
            viewModel.fetchPlantDetails(plantId);
            observePlantDetails();
            observeDiseases();
        } else {
            showToast("Invalid Plant ID");
        }

        setupDiseasesRecyclerView();
    }

    private void observePlantDetails() {
        viewModel.getPlantDetails().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                binding.setPlant(result.getData());
                String base64Image = result.getData().getImageResId(); // Make sure the article has imageResId
                if (base64Image != null && !base64Image.isEmpty()) {
                    Bitmap bitmap = ImageUtils.decodeBase64ToImage(base64Image);
                    binding.imgPlant.setImageBitmap(bitmap);
                }
            }
        });
    }

    private void observeDiseases() {
        viewModel.getDiseases().observe(getViewLifecycleOwner(), diseases -> {
            if (diseases != null && !diseases.isEmpty()) {
                binding.recyclerDiseases.setVisibility(View.VISIBLE);
                binding.tvNoDiseases.setVisibility(View.GONE);
                diseaseList.clear();
                diseaseList.addAll(diseases);
                diseasesAdapter.notifyDataSetChanged();
            } else {
                binding.recyclerDiseases.setVisibility(View.GONE);
                binding.tvNoDiseases.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupDiseasesRecyclerView() {
        diseasesAdapter = new DiseasesAdapter(diseaseList);
        binding.recyclerDiseases.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerDiseases.setAdapter(diseasesAdapter);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
