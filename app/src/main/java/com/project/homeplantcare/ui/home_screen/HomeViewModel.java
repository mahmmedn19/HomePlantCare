package com.project.homeplantcare.ui.home_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final AppRepository appRepository;
    private final MutableLiveData<PlantItem> plantItemLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<List<ArticleItem>>> articlesLiveData = new MutableLiveData<>();

    @Inject
    public HomeViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
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
                        plantItemLiveData.setValue(plant);
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


}
