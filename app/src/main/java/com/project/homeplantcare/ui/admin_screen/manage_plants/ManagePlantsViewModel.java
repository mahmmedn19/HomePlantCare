package com.project.homeplantcare.ui.admin_screen.manage_plants;

import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.PlantItem;

import java.util.ArrayList;
import java.util.List;

public class ManagePlantsViewModel extends ViewModel {

    private final List<PlantItem> mockPlants;

    public ManagePlantsViewModel() {
        mockPlants = new ArrayList<>();
        mockPlants.add(new PlantItem(1, "Aloe Vera", "Medicinal plant", "Indoor", "Medium", "Well-Drained", "Warm", "2024-01-01", R.drawable.plant_7));
        mockPlants.add(new PlantItem(2, "Snake Plant", "Air purifier", "Low Light", "Low", "Any", "Warm", "2023-12-15", R.drawable.plant_2));
        mockPlants.add(new PlantItem(3, "Money Plant", "Symbol of fortune", "Bright Indirect", "Medium", "Well-Drained", "Moderate", "2023-11-10", R.drawable.plant_6));
    }

    public List<PlantItem> getMockPlants() {
        return mockPlants;
    }

    public void deletePlant(PlantItem plant) {
        mockPlants.remove(plant);
    }
}
