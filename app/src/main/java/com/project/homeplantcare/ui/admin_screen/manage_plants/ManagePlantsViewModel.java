package com.project.homeplantcare.ui.admin_screen.manage_plants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.repo.app_repo.AppRepositoryImpl;
import com.project.homeplantcare.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ManagePlantsViewModel extends ViewModel {

    private final AppRepository appRepository;

    @Inject
    public ManagePlantsViewModel(AppRepositoryImpl appRepository) {
        this.appRepository = appRepository;
    }

    // Get all plants from Firestore
    public LiveData<Result<List<PlantItem>>> getAllPlants() {
        return appRepository.getAllPlants();
    }

    // Delete a plant
    public void deletePlant(PlantItem plantItem) {
        appRepository.deletePlant(plantItem.getPlantId()).observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                // Reload plants after deleting
                getAllPlants();
            }
        }
        );
    }
}
