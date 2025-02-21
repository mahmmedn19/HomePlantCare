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
    private final MutableLiveData<Result<List<PlantItem>>> filteredPlants = new MutableLiveData<>();
    private final LiveData<Result<List<PlantItem>>> allPlants;

    @Inject
    public HomeViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
        allPlants = appRepository.getAllPlants();
    }

    // Get all plants from Firestore
    public LiveData<Result<List<PlantItem>>> getAllPlants() {
        return appRepository.getAllPlants();
    }

    public void getPlantById(String plantId) {
        appRepository.getAllPlants().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                for (PlantItem plant : result.getData()) {
                    if (plant.getPlantId().equals(plantId)) {
                        // Handle plant by ID logic here
                        break;
                    }
                }
            }
        });
    }

    public LiveData<Result<List<ArticleItem>>> getAllArticles() {
        return appRepository.getAllArticles();
    }

    public void getArticleById(String articleId) {
        appRepository.getAllArticles().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                for (ArticleItem article : result.getData()) {
                    if (article.getArticleId().equals(articleId)) {
                        articlesLiveData.setValue(Result.success(List.of(article)));
                        break;
                    }
                }
            }
        });
    }

    public void filterPlants(String query) {
        filteredPlants.setValue(Result.loading());  // Show loading state while filtering

        // Fetch all plants data
        appRepository.getAllPlants().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                List<PlantItem> plants = result.getData();
                List<PlantItem> filteredList = new ArrayList<>();

                if (plants != null) {
                    for (PlantItem plant : plants) {
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
                }

                // Set the filtered list as the result
                filteredPlants.setValue(Result.success(filteredList));
            } else {
                // If there was an error, set the error state
                filteredPlants.setValue(Result.error(result.getErrorMessage()));
            }
        });
    }

    public LiveData<Result<List<PlantItem>>> getFilteredPlants() {
        return filteredPlants;
    }
}
