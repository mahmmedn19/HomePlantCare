package com.project.homeplantcare.ui.plant_details;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentPlantDetailsBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlantDetailsFragment extends BaseFragment<FragmentPlantDetailsBinding> {

    private PlantDetailsViewModel viewModel;
    private List<DiseaseItem> diseaseList = new ArrayList<>();
    private DiseasesAdapter diseasesAdapter;

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
        } else {
            Toast.makeText(requireContext(), "Invalid Plant ID", Toast.LENGTH_SHORT).show();
        }

        setupDiseasesRecyclerView();
        observeDiseases();
    }

    private void observePlantDetails() {
        viewModel.getPlantDetails().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                binding.setPlant(result.getData());  // Data Binding
            } else {
                Toast.makeText(requireContext(), "Failed to load plant details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeDiseases() {
        viewModel.getAllDiseases().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                diseaseList.clear();
                diseaseList.addAll(result.getData());
                diseasesAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupDiseasesRecyclerView() {
        diseasesAdapter = new DiseasesAdapter(diseaseList);
        binding.recyclerDiseases.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerDiseases.setAdapter(diseasesAdapter);
    }
}