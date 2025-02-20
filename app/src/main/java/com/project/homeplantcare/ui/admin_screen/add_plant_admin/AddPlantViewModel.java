package com.project.homeplantcare.ui.admin_screen.add_plant_admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.repo.app_repo.AppRepositoryImpl;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.data.utils.Result;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddPlantViewModel extends ViewModel {

    private final AppRepository appRepository;
    private final MutableLiveData<List<DiseaseItem>> selectedDiseases = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<DiseaseItem>> diseaseList = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isPlantSaved = new MutableLiveData<>(false);
    private final MutableLiveData<PlantItem> plantItemLiveData = new MutableLiveData<>();

    @Inject
    public AddPlantViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        fetchDiseases();
    }
    // Fetch all diseases from the repository
    private void fetchDiseases() {
        appRepository.getAllDiseases().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                diseaseList.setValue(result.getData());
            }
        });
    }

    // Toggle disease selection
    public void toggleDiseaseSelection(DiseaseItem disease) {
        List<DiseaseItem> currentSelected = selectedDiseases.getValue();
        if (currentSelected == null) currentSelected = new ArrayList<>();
        if (currentSelected.contains(disease)) {
            currentSelected.remove(disease);
        } else {
            currentSelected.add(disease);
        }
        selectedDiseases.setValue(currentSelected);
    }

    // Add a new plant
    public void addPlant(PlantItem plantItem) {
        // Add the selected diseases to the plant
        plantItem.setDiseases(selectedDiseases.getValue());

        appRepository.addPlant(plantItem).observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                isPlantSaved.setValue(true);
            } else {
                isPlantSaved.setValue(false);
            }
        });
    }

    // Update an existing plant
    public void updatePlant(String plantId, PlantItem plantItem) {
        plantItem.setDiseases(selectedDiseases.getValue());
        appRepository.updatePlant(plantId, plantItem).observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                isPlantSaved.setValue(true);
            } else {
                isPlantSaved.setValue(false);
            }
        });
    }

    // Fetch plant by ID
    public void getPlantById(String plantId) {
        appRepository.getAllPlants().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                for (PlantItem plant : result.getData()) {
                    if (plant.getPlantId().equals(plantId)) {
                        plantItemLiveData.setValue(plant);
                        break;
                    }
                }
            }
        });
    }


    public LiveData<Boolean> getIsPlantSaved() {
        return isPlantSaved;
    }

    public LiveData<List<DiseaseItem>> getSelectedDiseases() {
        return selectedDiseases;
    }

    public LiveData<List<DiseaseItem>> getDiseaseList() {
        return diseaseList;
    }

    public LiveData<PlantItem> getPlantItemLiveData() {
        return plantItemLiveData;
    }
}
