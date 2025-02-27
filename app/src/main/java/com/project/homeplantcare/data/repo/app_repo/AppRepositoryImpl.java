package com.project.homeplantcare.data.repo.app_repo;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.utils.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class AppRepositoryImpl implements AppRepository {

    private final FirebaseFirestore firestore;

    @Inject
    public AppRepositoryImpl(FirebaseFirestore db) {
        this.firestore = db;
    }

    @Override
    public LiveData<Result<List<PlantItem>>> getAllPlants() {
        MutableLiveData<Result<List<PlantItem>>> result = new MutableLiveData<>();
        result.postValue(Result.loading());

        firestore.collection("plants").get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        result.postValue(Result.success(Collections.emptyList())); // Return empty list if no plants
                        return;
                    }

                    List<PlantItem> plants = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        try {
                            PlantItem plantItem = document.toObject(PlantItem.class);
                            if (plantItem != null) {
                                plantItem.setPlantId(document.getId()); // Ensure ID is set
                                if (plantItem.getDiseases() == null) {
                                    plantItem.setDiseases(new ArrayList<>()); // Prevent NullPointerException
                                }
                                plants.add(plantItem);
                            } else {
                                Log.e("Firestore", "Null object parsed from document: " + document.getId());
                            }
                        } catch (Exception e) {
                            Log.e("Firestore", "Error parsing plant document: " + document.getId(), e);
                        }
                    }

                    result.postValue(Result.success(plants)); // Update LiveData with the parsed list
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Failed to fetch plants", e);
                    result.postValue(Result.error("Failed to load plants: " + e.getMessage()));
                });

        return result;
    }


    @Override
    public LiveData<Result<String>> addPlant(PlantItem plantItem) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        result.postValue(Result.loading());

        firestore.collection("plants").add(plantItem)
                .addOnSuccessListener(documentReference -> {
                    if (documentReference != null) {
                        // Set generated document ID
                        String generatedId = documentReference.getId();
                        plantItem.setPlantId(generatedId);

                        // Update Firestore with the plant ID
                        firestore.collection("plants").document(generatedId).set(plantItem)
                                .addOnSuccessListener(aVoid -> {
                                    result.postValue(Result.success("Plant added successfully with ID: " + generatedId));
                                })
                                .addOnFailureListener(e -> {
                                    result.postValue(Result.error("Failed to update plant ID: " + e.getMessage()));
                                    Log.e("Firestore", "Failed to set plant ID in Firestore: ", e);
                                });
                    } else {
                        result.postValue(Result.error("DocumentReference is null. Failed to add plant."));
                        Log.e("Firestore", "DocumentReference is null.");
                    }
                })
                .addOnFailureListener(e -> {
                    result.postValue(Result.error("Failed to add plant: " + e.getMessage()));
                    Log.e("Firestore", "Error adding plant to Firestore: ", e);
                });

        return result;
    }

    @Override
    public LiveData<Result<String>> updatePlant(String plantId, PlantItem plantItem) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();

        // Validate input
        if (plantId == null || plantId.trim().isEmpty() || plantItem == null) {
            result.postValue(Result.error("Invalid plant ID or plant data."));
            Log.e("Firestore", "Attempted to update plant with invalid ID or null data.");
            return result;
        }

        result.postValue(Result.loading());

        // Check if plant exists before updating
        firestore.collection("plants").document(plantId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Ensure the plant ID is correctly set inside the PlantItem object
                        plantItem.setPlantId(plantId);

                        // Update plant data
                        firestore.collection("plants").document(plantId).set(plantItem)
                                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Plant updated successfully.")))
                                .addOnFailureListener(e -> {
                                    result.setValue(Result.error("Failed to update plant: " + e.getMessage()));
                                    Log.e("Firestore", "Error updating plant with ID " + plantId + ": ", e);
                                });
                    } else {
                        result.setValue(Result.error("Plant not found."));
                        Log.e("Firestore", "Attempted to update a non-existing plant with ID: " + plantId);
                    }
                })
                .addOnFailureListener(e -> {
                    result.setValue(Result.error("Failed to check plant existence: " + e.getMessage()));
                    Log.e("Firestore", "Error fetching plant with ID " + plantId + ": ", e);
                });

        return result;
    }

    @Override
    public LiveData<Result<String>> deletePlant(String plantId) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        if (plantId == null || plantId.trim().isEmpty()) {
            result.setValue(Result.error("Invalid plant ID"));
            return result;
        }

        firestore.collection("plants").document(plantId).delete()
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Plant deleted successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to delete plant: " + e.getMessage())));

        return result;
    }

    // Fetch all diseases from Firestore
    @Override
    public LiveData<Result<List<DiseaseItem>>> getAllDiseases() {
        MutableLiveData<Result<List<DiseaseItem>>> result = new MutableLiveData<>();
        result.setValue(Result.loading());
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
        result.setValue(Result.loading());

        if (diseaseItem.getName() == null) diseaseItem.setName("Unknown Disease");
        if (diseaseItem.getSymptoms() == null) diseaseItem.setSymptoms("No symptoms provided.");
        if (diseaseItem.getRemedies() == null) diseaseItem.setRemedies("No remedies available.");

        firestore.collection("diseases").add(diseaseItem)
                .addOnSuccessListener(documentReference -> {
                    String newId = documentReference.getId();
                    diseaseItem.setDiseaseId(newId);

                    firestore.collection("diseases").document(newId)
                            .set(diseaseItem)
                            .addOnSuccessListener(aVoid -> result.setValue(Result.success("Disease added successfully with ID: " + newId)))
                            .addOnFailureListener(e -> result.setValue(Result.error("Failed to update disease: " + e.getMessage())));
                })
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to add disease: " + e.getMessage())));

        return result;
    }

    @Override
    public LiveData<Result<String>> updateDisease(String diseaseId, DiseaseItem diseaseItem) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();

        if (diseaseId == null || diseaseId.trim().isEmpty()) {
            result.setValue(Result.error("Invalid disease ID."));
            return result;
        }

        result.setValue(Result.loading());

        firestore.collection("diseases").document(diseaseId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        diseaseItem.setDiseaseId(diseaseId); // Ensure ID is not null
                        firestore.collection("diseases").document(diseaseId).set(diseaseItem)
                                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Disease updated successfully.")))
                                .addOnFailureListener(e -> result.setValue(Result.error("Failed to update disease: " + e.getMessage())));
                    } else {
                        result.setValue(Result.error("Disease not found."));
                    }
                })
                .addOnFailureListener(e -> result.setValue(Result.error("Error checking disease existence: " + e.getMessage())));

        return result;
    }

    @Override
    public LiveData<Result<String>> deleteDisease(String diseaseId) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        if (diseaseId == null || diseaseId.trim().isEmpty()) {
            result.setValue(Result.error("Invalid disease ID."));
            return result;
        }

        firestore.collection("diseases").document(diseaseId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        firestore.collection("diseases").document(diseaseId).delete()
                                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Disease deleted successfully.")))
                                .addOnFailureListener(e -> result.setValue(Result.error("Failed to delete disease: " + e.getMessage())));
                    } else {
                        result.setValue(Result.error("Disease not found."));
                    }
                })
                .addOnFailureListener(e -> result.setValue(Result.error("Error checking disease existence: " + e.getMessage())));

        return result;
    }

    // Fetch all articles from Firestore
    @Override
    public LiveData<Result<List<ArticleItem>>> getAllArticles() {
        MutableLiveData<Result<List<ArticleItem>>> result = new MutableLiveData<>();
        result.setValue(Result.loading());
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
        result.setValue(Result.loading());


        firestore.collection("articles").add(articleItem)
                .addOnSuccessListener(documentReference -> {
                    String newId = documentReference.getId();
                    articleItem.setArticleId(newId); // Set the generated ID

                    firestore.collection("articles").document(newId).set(articleItem)
                            .addOnSuccessListener(aVoid -> result.setValue(Result.success("Article added successfully with ID: " + newId)))
                            .addOnFailureListener(e -> result.setValue(Result.error("Failed to update article: " + e.getMessage())));
                })
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to add article: " + e.getMessage())));

        return result;
    }

    @Override
    public LiveData<Result<String>> updateArticle(String articleId, ArticleItem articleItem) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();

        if (articleId == null || articleId.trim().isEmpty()) {
            result.setValue(Result.error("Invalid article ID."));
            return result;
        }

        result.setValue(Result.loading());

        firestore.collection("articles").document(articleId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        articleItem.setArticleId(articleId); // Ensure ID is not null
                        firestore.collection("articles").document(articleId).set(articleItem)
                                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Article updated successfully.")))
                                .addOnFailureListener(e -> result.setValue(Result.error("Failed to update article: " + e.getMessage())));
                    } else {
                        result.setValue(Result.error("Article not found."));
                    }
                })
                .addOnFailureListener(e -> result.setValue(Result.error("Error checking article existence: " + e.getMessage())));

        return result;
    }

    @Override
    public LiveData<Result<String>> deleteArticle(String articleId) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        if (articleId == null || articleId.trim().isEmpty()) {
            result.setValue(Result.error("Invalid article ID."));
            return result;
        }

        firestore.collection("articles").document(articleId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        firestore.collection("articles").document(articleId).delete()
                                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Article deleted successfully.")))
                                .addOnFailureListener(e -> result.setValue(Result.error("Failed to delete article: " + e.getMessage())));
                    } else {
                        result.setValue(Result.error("Article not found."));
                    }
                })
                .addOnFailureListener(e -> result.setValue(Result.error("Error checking article existence: " + e.getMessage())));

        return result;
    }

    @Override
    public LiveData<Result<String>> addAILink(String link) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        firestore.collection("ai_links").document("single_ai_link") // Use a fixed document ID
                .set(Collections.singletonMap("link", link)) // Store as a key-value pair
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("AI link updated successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to update AI link: " + e.getMessage())));

        return result;
    }

    @Override
    public LiveData<Result<String>> getSingleAILink() {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        firestore.collection("ai_links").document("single_ai_link").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && documentSnapshot.contains("link")) {
                        String link = documentSnapshot.getString("link");
                        result.setValue(Result.success(link));
                    } else {
                        result.setValue(Result.error("No AI link found."));
                    }
                })
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to fetch AI link: " + e.getMessage())));

        return result;
    }

}
