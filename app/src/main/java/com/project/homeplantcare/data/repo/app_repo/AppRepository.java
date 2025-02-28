package com.project.homeplantcare.data.repo.app_repo;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.models.HistoryItem;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.utils.Result;

import java.io.File;
import java.util.List;

public interface AppRepository {

    // Fetch all plants from Firestore
    LiveData<Result<List<PlantItem>>> getAllPlants();

    // Add a new plant to Firestore
    LiveData<Result<String>> addPlant(PlantItem plantItem);

    // Update an existing plant in Firestore
    LiveData<Result<String>> updatePlant(String plantId, PlantItem plantItem);

    // Delete a plant from Firestore
    LiveData<Result<String>> deletePlant(String plantId);

    // Fetch all diseases from Firestore
    LiveData<Result<List<DiseaseItem>>> getAllDiseases();

    // Add a new disease to Firestore
    LiveData<Result<String>> addDisease(DiseaseItem diseaseItem);

    // Update an existing disease in Firestore
    LiveData<Result<String>> updateDisease(String diseaseId, DiseaseItem diseaseItem);

    // Delete a disease from Firestore
    LiveData<Result<String>> deleteDisease(String diseaseId);

    // Fetch all articles from Firestore
    LiveData<Result<List<ArticleItem>>> getAllArticles();

    // Add a new article to Firestore
    LiveData<Result<String>> addArticle(ArticleItem articleItem);

    // Update an existing article in Firestore
    LiveData<Result<String>> updateArticle(String articleId, ArticleItem articleItem);

    // Delete an article from Firestore
    LiveData<Result<String>> deleteArticle(String articleId);

    /**
     * Add an AI-generated link to Firestore under the admin collection.
     * @param link The AI-generated link to be stored.
     * @return LiveData<Result<String>> indicating the success or failure of the operation.
     */
    LiveData<Result<String>> addAILink(String link);


    LiveData<Result<String>> getSingleAILink() ;

    LiveData<Result<String>> uploadImage(File imageUri);

    LiveData<Result<PlantItem>> getPlantIdByName(String plantName);

    LiveData<Result<Boolean>> isPlantInHistory(String userId, String plantId);

    LiveData<Result<String>> addToHistory(String userId, String plantId);

    LiveData<Result<String>> removeFromHistory(String userId, String plantId);

    LiveData<Result<List<HistoryItem>>> getUserHistory(String userId);

    LiveData<Result<Boolean>> isPlantFavorite(String userId, String plantId);

    LiveData<Result<String>> addToFavorites(String userId, String plantId);

    LiveData<Result<String>> removeFromFavorites(String userId, String plantId);
    LiveData<Result<List<PlantItem>>> getUserFavorites(String userId);

}
