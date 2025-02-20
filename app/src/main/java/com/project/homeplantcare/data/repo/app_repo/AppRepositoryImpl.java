package com.project.homeplantcare.data.repo.app_repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

public class AppRepositoryImpl implements AppRepository {

    private final FirebaseFirestore firestore;

    @Inject
    public AppRepositoryImpl(FirebaseFirestore db) {
        this.firestore = db;
    }

    // Fetch all plants from Firestore
    @Override
    public LiveData<Result<List<PlantItem>>> getAllPlants() {
        MutableLiveData<Result<List<PlantItem>>> result = new MutableLiveData<>();
        firestore.collection("plants").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<PlantItem> plants = querySnapshot.toObjects(PlantItem.class);
                    result.setValue(Result.success(plants));
                })
                .addOnFailureListener(e -> result.setValue(Result.error(e.getMessage())));
        return result;
    }

    @Override
    public LiveData<Result<String>> addPlant(PlantItem plantItem) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        firestore.collection("plants").add(plantItem)
                .addOnSuccessListener(documentReference -> result.setValue(Result.success("Plant added successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to add plant: " + e.getMessage())));
        return result;
    }

    @Override
    public LiveData<Result<String>> updatePlant(String plantId, PlantItem plantItem) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        firestore.collection("plants").document(plantId).set(plantItem)
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Plant updated successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to update plant: " + e.getMessage())));
        return result;
    }

    @Override
    public LiveData<Result<String>> deletePlant(String plantId) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        firestore.collection("plants").document(plantId).delete()
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Plant deleted successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to delete plant: " + e.getMessage())));
        return result;
    }

    // Fetch all diseases from Firestore
    @Override
    public LiveData<Result<List<DiseaseItem>>> getAllDiseases() {
        MutableLiveData<Result<List<DiseaseItem>>> result = new MutableLiveData<>();
        firestore.collection("diseases").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<DiseaseItem> diseases = querySnapshot.toObjects(DiseaseItem.class);
                    result.setValue(Result.success(diseases));
                })
                .addOnFailureListener(e -> result.setValue(Result.error(e.getMessage())));
        return result;
    }

    @Override
    public LiveData<Result<String>> addDisease(DiseaseItem diseaseItem) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        firestore.collection("diseases").add(diseaseItem)
                .addOnSuccessListener(documentReference -> result.setValue(Result.success("Disease added successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to add disease: " + e.getMessage())));
        return result;
    }

    @Override
    public LiveData<Result<String>> updateDisease(String diseaseId, DiseaseItem diseaseItem) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        firestore.collection("diseases").document(diseaseId).set(diseaseItem)
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Disease updated successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to update disease: " + e.getMessage())));
        return result;
    }

    @Override
    public LiveData<Result<String>> deleteDisease(String diseaseId) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        firestore.collection("diseases").document(diseaseId).delete()
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Disease deleted successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to delete disease: " + e.getMessage())));
        return result;
    }

    // Fetch all articles from Firestore
    @Override
    public LiveData<Result<List<ArticleItem>>> getAllArticles() {
        MutableLiveData<Result<List<ArticleItem>>> result = new MutableLiveData<>();
        firestore.collection("articles").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<ArticleItem> articles = querySnapshot.toObjects(ArticleItem.class);
                    result.setValue(Result.success(articles));
                })
                .addOnFailureListener(e -> result.setValue(Result.error(e.getMessage())));
        return result;
    }

    @Override
    public LiveData<Result<String>> addArticle(ArticleItem articleItem) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        firestore.collection("articles").add(articleItem)
                .addOnSuccessListener(documentReference -> result.setValue(Result.success("Article added successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to add article: " + e.getMessage())));
        return result;
    }

    @Override
    public LiveData<Result<String>> updateArticle(String articleId, ArticleItem articleItem) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        firestore.collection("articles").document(articleId).set(articleItem)
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Article updated successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to update article: " + e.getMessage())));
        return result;
    }

    @Override
    public LiveData<Result<String>> deleteArticle(String articleId) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        firestore.collection("articles").document(articleId).delete()
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Article deleted successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to delete article: " + e.getMessage())));
        return result;
    }

    @Override
    public LiveData<Result<String>> addAILink(String link) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        firestore.collection("ai_links").add(link)
                .addOnSuccessListener(documentReference -> result.setValue(Result.success("AI link added successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to add AI link: " + e.getMessage())));
        return result;
    }

    @Override
    public LiveData<Result<List<String>>> getAllAILinks() {
        MutableLiveData<Result<List<String>>> result = new MutableLiveData<>();
        firestore.collection("ai_links").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<String> links = querySnapshot.toObjects(String.class);
                    result.setValue(Result.success(links));
                })
                .addOnFailureListener(e -> result.setValue(Result.error(e.getMessage())));
        return result;
    }
}
