package com.project.homeplantcare.ui.plant_details;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentPlantDetailsBinding;
import com.project.homeplantcare.models.DiseaseItem;
import com.project.homeplantcare.models.PlantItem;
import com.project.homeplantcare.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlantDetailsFragment extends BaseFragment<FragmentPlantDetailsBinding> {

    private List<DiseaseItem> diseaseList;

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
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Plant Details");
        showBackButton(true);

        // Create a fake PlantItem object
        PlantItem fakePlant = new PlantItem(
                1,
                "Aloe Vera",
                "Aloe Vera is a species of plant well known for its medicinal and skincare uses. It is easy to maintain and grows well indoors.",
                "Indoor",      // Light Requirements
                "Medium",      // Water Requirements
                "Well-Drained", // Soil Requirements
                "Warm",        // Weather Requirements
                "2024-01-01",  // Growth Date (Sample Date)
                R.drawable.plant_6 // Replace with actual drawable resource
        );

        // Bind the PlantItem to the layout
        binding.setPlant(fakePlant);

        // Load Diseases
        diseaseList = generateFakeDiseases();
        setupDiseasesRecyclerView();
    }

    private List<DiseaseItem> generateFakeDiseases() {
        List<DiseaseItem> diseases = new ArrayList<>();
        diseases.add(new DiseaseItem(1, "Leaf Spot", "Dark spots on leaves", "Remove affected leaves\napply fungicide"));
        diseases.add(new DiseaseItem(2, "Root Rot", "Wilting and yellowing leaves", "Improve drainage\nreduce watering"));
        diseases.add(new DiseaseItem(3, "Powdery Mildew", "White powdery substance on leaves", "Increase airflow, apply fungicide"));
        return diseases;
    }

    private void setupDiseasesRecyclerView() {
        DiseasesAdapter diseasesAdapter = new DiseasesAdapter(diseaseList);
        binding.recyclerDiseases.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerDiseases.setAdapter(diseasesAdapter);
    }
}
