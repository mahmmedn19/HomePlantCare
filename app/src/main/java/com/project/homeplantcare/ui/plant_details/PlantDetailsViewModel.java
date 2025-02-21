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
    private LiveData<Result<List<DiseaseItem>>> diseases;

    @Inject
    public PlantDetailsViewModel(AppRepository repository) {
        this.repository = repository;
    }

    public void fetchPlantDetails(String plantId) {
        repository.getAllPlants().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                for (PlantItem plant : result.getData()) {
                    if (plant.getPlantId().equals(plantId)) {
                        plantDetails.setValue(Result.success(plant));
                        break;
                    }
                }
            } else {
                plantDetails.setValue(Result.error("Plant not found"));
            }
        });
    }

    public LiveData<Result<PlantItem>> getPlantDetails() {
        return plantDetails;
    }

    public LiveData<Result<List<DiseaseItem>>> getAllDiseases() {
        if (diseases == null) {
            diseases = repository.getAllDiseases();
        }
        return diseases;
    }
}