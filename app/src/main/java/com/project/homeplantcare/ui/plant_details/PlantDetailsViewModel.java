package com.project.homeplantcare.ui.plant_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PlantDetailsViewModel extends ViewModel {

    private final AppRepository repository;
    private final MutableLiveData<Result<PlantItem>> plantDetails = new MutableLiveData<>();
    private final MutableLiveData<List<DiseaseItem>> diseases = new MutableLiveData<>();

    @Inject
    public PlantDetailsViewModel(AppRepository repository) {
        this.repository = repository;
    }

    // Fetch plant details and diseases
    public void fetchPlantDetails(String plantId) {
        repository.getAllPlants().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                PlantItem plant = findPlantById(result.getData(), plantId);
                if (plant != null) {
                    plantDetails.setValue(Result.success(plant)); // Set plant details
                    diseases.setValue(plant.getDiseases()); // Set diseases directly
                } else {
                    plantDetails.setValue(Result.error("Plant not found"));
                    diseases.setValue(null);
                }
            } else {
                plantDetails.setValue(Result.error("Failed to load plants"));
                diseases.setValue(null);
            }
        });
    }

    // Helper method to find a plant by ID
    private PlantItem findPlantById(List<PlantItem> plants, String plantId) {
        for (PlantItem plant : plants) {
            if (plant.getPlantId().equals(plantId)) {
                return plant;
            }
        }
        return null;
    }

    public LiveData<Result<PlantItem>> getPlantDetails() {
        return plantDetails;
    }

    public LiveData<List<DiseaseItem>> getDiseases() {
        return diseases;
    }

    public LiveData<Result<Boolean>> isPlantFavorite(String userId, String plantId) {
        return repository.isPlantFavorite(userId, plantId);
    }

    public LiveData<Result<String>> addToFavorites(String userId, String plantId) {
        return repository.addToFavorites(userId, plantId);
    }

    public LiveData<Result<String>> removeFromFavorites(String userId, String plantId) {
        return repository.removeFromFavorites(userId, plantId);
    }

    public LiveData<Result<Boolean>> isPlantInHistory(String userId, String plantId) {
        return repository.isPlantInHistory(userId, plantId);
    }

    public LiveData<Result<String>> addToHistory(String userId, String plantId) {
        return repository.addToHistory(userId, plantId);
    }

    public LiveData<Result<String>> removeFromHistory(String userId, String plantId) {
        return repository.removeFromHistory(userId, plantId);
    }
}
