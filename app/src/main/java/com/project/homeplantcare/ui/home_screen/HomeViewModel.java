package com.project.homeplantcare.ui.home_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.utils.Result;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final AppRepository appRepository;
    private final MutableLiveData<Result<List<ArticleItem>>> articlesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<PlantItem>>> allPlantsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<PlantItem>>> filteredPlants = new MutableLiveData<>();

    @Inject
    public HomeViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    // Fetch all plants data initially
    public void loadAllPlants() {
        appRepository.getAllPlants().observeForever(result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                allPlantsLiveData.setValue(Result.loading());
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                allPlantsLiveData.setValue(Result.success(result.getData()));
                filterPlants("");  // Apply initial filter (all plants)
            } else {
                allPlantsLiveData.setValue(Result.error(result.getErrorMessage()));
            }
        });
    }

    // Get all plants data
    public LiveData<Result<List<PlantItem>>> getAllPlants() {
        return allPlantsLiveData;
    }

    // Filter the stored plants data based on the query
    public void filterPlants(String query) {
        // Set loading state while filtering
        filteredPlants.setValue(Result.loading());

        // Ensure that we have data
        List<PlantItem> allPlants = allPlantsLiveData.getValue() != null ? allPlantsLiveData.getValue().getData() : null;

        if (allPlants != null && !query.isEmpty()) {
            List<PlantItem> filteredList = new ArrayList<>();
            for (PlantItem plant : allPlants) {
                // Check if the plant name or disease name matches the search query
                if (plant.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(plant);
                } else {
                    for (DiseaseItem disease : plant.getDiseases()) {
                        if (disease.getName().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(plant);
                            break;
                        }
                    }
                }
            }

            // Set the filtered list in the LiveData as a success Result
            filteredPlants.setValue(Result.success(filteredList));
        } else {
            // If no query, show all plants
            filteredPlants.setValue(Result.success(allPlants));
        }
    }

    // Get filtered plants data
    public LiveData<Result<List<PlantItem>>> getFilteredPlants() {
        return filteredPlants;
    }

    // Get all articles data
    public LiveData<Result<List<ArticleItem>>> getAllArticles() {
        return appRepository.getAllArticles();
    }
}
