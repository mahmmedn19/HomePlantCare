package com.project.homeplantcare.data.repo.app_repo;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.models.HistoryItem;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.models.ResultApi;
import com.project.homeplantcare.data.repo.network.ApiService;
import com.project.homeplantcare.data.repo.network.ResponseModel;
import com.project.homeplantcare.data.utils.AuthUtils;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.data.utils.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppRepositoryImpl implements AppRepository {

    private final FirebaseFirestore firestore;
    private final ApiService apiService;
    private static AppRepositoryImpl instance;

    @Inject
    public AppRepositoryImpl(FirebaseFirestore db, ApiService apiService) {

        this.firestore = db;
        this.apiService = apiService;
    }

    public static synchronized AppRepositoryImpl getInstance(ApiService apiService) {
        if (instance == null) {
            instance = new AppRepositoryImpl(
                    FirebaseFirestore.getInstance(),
                    apiService
            );
        }
        return instance;
    }

    @Override
    public LiveData<Result<List<PlantItem>>> getAllPlants() {
        MutableLiveData<Result<List<PlantItem>>> result = new MutableLiveData<>();
        result.postValue(Result.loading());

        // Step 1: Fetch all diseases
        firestore.collection("diseases").get().addOnSuccessListener(diseaseSnapshot -> {
            List<DiseaseItem> allDiseases = diseaseSnapshot.toObjects(DiseaseItem.class);

            // Step 2: Fetch all plants
            firestore.collection("plants").get().addOnSuccessListener(querySnapshot -> {
                if (querySnapshot.isEmpty()) {
                    result.postValue(Result.success(Collections.emptyList()));
                    return;
                }

                List<PlantItem> plants = new ArrayList<>();
                for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                    try {
                        PlantItem plantItem = document.toObject(PlantItem.class);
                        if (plantItem != null) {
                            plantItem.setPlantId(document.getId());

                            if (plantItem.getDiseases() == null) {
                                plantItem.setDiseases(new ArrayList<>());
                            } else {
                                // Merge disease data with full details (including images)
                                plantItem.setDiseases(
                                        mergePlantDiseasesWithFullDetails(plantItem.getDiseases(), allDiseases)
                                );
                            }

                            plants.add(plantItem);
                        }
                    } catch (Exception e) {
                        Log.e("Firestore", "Error parsing plant document: " + document.getId(), e);
                    }
                }

                result.postValue(Result.success(plants));
            }).addOnFailureListener(e -> {
                result.postValue(Result.error("Failed to load plants: " + e.getMessage()));
            });

        }).addOnFailureListener(e -> {
            result.postValue(Result.error("Failed to load diseases: " + e.getMessage()));
        });

        return result;
    }

    private List<DiseaseItem> mergePlantDiseasesWithFullDetails(List<DiseaseItem> plantDiseases, List<DiseaseItem> allDiseases) {
        List<DiseaseItem> enrichedDiseases = new ArrayList<>();
        for (DiseaseItem plantDisease : plantDiseases) {
            for (DiseaseItem fullDisease : allDiseases) {
                if (plantDisease.getDiseaseId() != null &&
                        plantDisease.getDiseaseId().equals(fullDisease.getDiseaseId())) {
                    enrichedDiseases.add(fullDisease); // Use full version with image
                    break;
                }
            }
        }
        return enrichedDiseases;
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
                .addOnSuccessListener(aVoid -> result.setValue(Result.success(link)))
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

    @Override
    public LiveData<Result<Pair<String, Boolean>>> uploadImage(File imageFile) {
        MutableLiveData<Result<Pair<String, Boolean>>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        if (imageFile == null || !imageFile.exists()) {
            result.postValue(Result.error("Invalid file: File does not exist"));
            return result;
        }

        try {
            // ✅ Prepare image for API upload
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", imageFile.getName(), requestFile);

            apiService.uploadImage(body).enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                    if (!response.isSuccessful() || response.body() == null) {
                        result.setValue(Result.error("Server error: " + response.code()));
                        return;
                    }

                    // ✅ Successfully received prediction
                    String predictedClass = response.body().getPredictedClass();
                    String[] parts = predictedClass.split("_", 2);
                    String plantName = parts.length > 0 ? parts[0] : "Unknown";
                    String diseaseName = parts.length > 1 ? parts[1].replace("-", " ") : "no disease";
                    if (plantName.equalsIgnoreCase("Unknown")) {
                        result.setValue(Result.success(new Pair<>(plantName, false))); // ✅ Return plantName + Not Saved
                        return;
                    }

                    if (plantName.isEmpty()) {
                        result.setValue(Result.error("Invalid response: No plant name detected"));
                        return;
                    }

                    Log.d("UploadImage", "Prediction Received: " + plantName);

                    // ✅ Fetch Plant ID by Name
                    getPlantIdByName(plantName).observeForever(plantIdResult -> {
                        if (plantIdResult.getStatus() == Result.Status.SUCCESS) {
                            String plantId = plantIdResult.getData().getPlantId();
                            if (plantId == null || plantId.isEmpty()) {
                                result.setValue(Result.success(new Pair<>(predictedClass, false))); // ✅ Return plantName + Not Saved
                                return;
                            }

                            String imageBase64 = ImageUtils.encodeImageFileToBase64(imageFile);
                            String analysisDate = String.valueOf(System.currentTimeMillis());

                            // ✅ Prepare Firestore data
                            Map<String, Object> analysisData = new HashMap<>();
                            analysisData.put("imageUrl", imageBase64);
                            analysisData.put("analysisDate", analysisDate);
                            analysisData.put("plantName", plantName);
                            analysisData.put("diseaseName", diseaseName);
                            analysisData.put("plantId", plantId);

                            // ✅ Save analysis result to Firestore
                            firestore.collection("user")
                                    .document(Objects.requireNonNull(AuthUtils.getCurrentUserId()))
                                    .collection("analysis_results")
                                    .document(plantId)
                                    .set(analysisData)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("UploadImage", "Analysis saved for " + plantName);
                                        result.setValue(Result.success(new Pair<>(predictedClass, true))); // ✅ Return plantName + Saved
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("UploadImage", "Failed to save analysis: " + e.getMessage());
                                        result.setValue(Result.success(new Pair<>(predictedClass, false))); // ✅ Return plantName + Not Saved
                                    });
                        }
                    });
                }

                @Override
                public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                    result.setValue(Result.error("Network error: " + t.getMessage()));
                }
            });

        } catch (Exception e) {
            result.postValue(Result.error("Unexpected error: " + e.getMessage()));
        }

        return result;
    }


    @Override
    public LiveData<Result<PlantItem>> getPlantIdByName(String plantName) {
        MutableLiveData<Result<PlantItem>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        firestore.collection("plants")
                .whereGreaterThanOrEqualTo("name", plantName) // Search plant by name
                .whereLessThanOrEqualTo("name", plantName + "\uf8ff") // Ensure it matches the name
                .limit(1) // Limit to first match
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        PlantItem plantItem = document.toObject(PlantItem.class);
                        if (plantItem != null) {
                            plantItem.setPlantId(document.getId()); // Ensure ID is set
                            result.setValue(Result.success(plantItem));
                        } else {
                            result.setValue(Result.error("Failed to parse plant data"));
                        }
                    } else {
                        result.setValue(Result.error("Plant not found"));
                    }
                })
                .addOnFailureListener(e -> result.setValue(Result.error("Error fetching plant: " + e.getMessage())));

        return result;
    }


    @Override
    public LiveData<Result<Boolean>> isPlantInHistory(String userId, String plantId) {
        MutableLiveData<Result<Boolean>> result = new MutableLiveData<>();
        firestore.collection("user").document(userId)
                .collection("history").document(plantId)
                .get()
                .addOnSuccessListener(documentSnapshot -> result.setValue(Result.success(documentSnapshot.exists())))
                .addOnFailureListener(e -> result.setValue(Result.error("Error checking history: " + e.getMessage())));
        return result;
    }

    @Override
    public LiveData<Result<String>> addToHistory(String userId, String plantId) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        // Step 2: Fetch Full Plant Details
        firestore.collection("plants").document(plantId).get()
                .addOnSuccessListener(plantDocument -> {
                    if (!plantDocument.exists()) {
                        result.setValue(Result.error("Plant details not found"));
                        return;
                    }

                    PlantItem plantItem = plantDocument.toObject(PlantItem.class);
                    if (plantItem == null) {
                        result.setValue(Result.error("Failed to parse plant data"));
                        return;
                    }

                    // Ensure the Plant ID is set
                    plantItem.setPlantId(plantDocument.getId());

                    // Step 3: Fetch Latest Analysis Data
                    getLatestAnalysisResult(userId, plantId).observeForever(analysisResult -> {
                        if (analysisResult.getStatus() == Result.Status.SUCCESS) {
                            ResultApi analysisData = analysisResult.getData();
                            String plantName = analysisData.getPlantName();
                            String diseaseName = analysisData.getDiseaseName();

                            // Step 4: Prepare and Save History Entry
                            Map<String, Object> historyData = new HashMap<>();
                            historyData.put("plantId", plantItem.getPlantId());
                            historyData.put("plantName", plantName);
                            historyData.put("diseaseName", diseaseName);
                            historyData.put("analysisDate", analysisData.getAnalysisDate());
                            historyData.put("imageUrl", analysisData.getImageUrl());
                            historyData.put("timestamp", System.currentTimeMillis());

                            String historyId = UUID.randomUUID().toString(); // Generate a unique ID for the history entry

                            firestore.collection("user").document(userId)
                                    .collection("history").document(historyId) // Use the unique ID
                                    .set(historyData)
                                    .addOnSuccessListener(aVoid -> result.setValue(Result.success("History saved successfully.")))
                                    .addOnFailureListener(e -> result.setValue(Result.error("Failed to save history: " + e.getMessage())));

                        } else {
                            result.setValue(Result.error("Failed to retrieve analysis result"));
                        }
                    });

                })
                .addOnFailureListener(e -> result.setValue(Result.error("Error fetching plant details: " + e.getMessage())));

        return result;
    }

    public LiveData<Result<ResultApi>> getLatestAnalysisResult(String userId, String plantId) {
        MutableLiveData<Result<ResultApi>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        firestore.collection("user").document(userId)
                .collection("analysis_results")
                .document(plantId) // Assuming the document ID is the plant ID
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        ResultApi analysisData = document.toObject(ResultApi.class);
                        result.setValue(Result.success(analysisData));
                    } else {
                        result.setValue(Result.error("No analysis data found"));
                    }
                })
                .addOnFailureListener(e -> result.setValue(Result.error("Error fetching analysis data: " + e.getMessage())));

        return result;
    }

    @Override
    public LiveData<Result<List<HistoryItem>>> getUserHistory(String userId) {
        MutableLiveData<Result<List<HistoryItem>>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        firestore.collection("user").document(userId)
                .collection("history")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<HistoryItem> historyList = new ArrayList<>();
                    if (querySnapshot.isEmpty()) {
                        result.setValue(Result.success(historyList)); // Return empty list if no history
                        return;
                    }

                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String plantId = document.getString("plantId");
                        String analysisDate = document.getString("analysisDate");
                        String imageUrl = document.getString("imageUrl");
                        String diseaseName = document.getString("diseaseName");

                        firestore.collection("plants").document(plantId)
                                .get()
                                .addOnSuccessListener(plantDoc -> {
                                    if (plantDoc.exists()) {
                                        String plantName = plantDoc.getString("name");

                                        // Create a HistoryItem object
                                        HistoryItem historyItem = new HistoryItem(
                                                document.getId(),
                                                plantId,
                                                plantName,
                                                diseaseName,
                                                analysisDate,
                                                imageUrl
                                        );

                                        historyList.add(historyItem);
                                        result.setValue(Result.success(historyList));
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    result.setValue(Result.error("Failed to fetch plant details: " + e.getMessage()));
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    result.setValue(Result.error("Failed to load history: " + e.getMessage()));
                });

        return result;
    }


    @Override
    public LiveData<Result<Boolean>> isPlantFavorite(String userId, String plantId) {
        MutableLiveData<Result<Boolean>> result = new MutableLiveData<>();
        firestore.collection("user").document(userId)
                .collection("favorites").document(plantId)
                .get()
                .addOnSuccessListener(documentSnapshot -> result.setValue(Result.success(documentSnapshot.exists())))
                .addOnFailureListener(e -> result.setValue(Result.error("Error checking favorite: " + e.getMessage())));
        return result;
    }

    @Override
    public LiveData<Result<String>> addToFavorites(String userId, String plantId) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        Map<String, Object> data = new HashMap<>();
        data.put("plantId", plantId);
        firestore.collection("user").document(userId)
                .collection("favorites").document(plantId)
                .set(data)
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Added to favorites")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to add to favorites")));
        return result;
    }

    @Override
    public LiveData<Result<List<PlantItem>>> getUserFavorites(String userId) {
        MutableLiveData<Result<List<PlantItem>>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        firestore.collection("user").document(userId)
                .collection("favorites")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<PlantItem> favoritePlants = new ArrayList<>();
                    if (querySnapshot.isEmpty()) {
                        result.setValue(Result.success(favoritePlants)); // Empty list
                        return;
                    }

                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        String plantId = document.getString("plantId");

                        // Fetch plant details
                        firestore.collection("plants").document(plantId)
                                .get()
                                .addOnSuccessListener(plantDoc -> {
                                    if (plantDoc.exists()) {
                                        PlantItem plant = plantDoc.toObject(PlantItem.class);
                                        if (plant != null) {
                                            plant.setPlantId(plantId);
                                            favoritePlants.add(plant);
                                            result.setValue(Result.success(favoritePlants));
                                        }
                                    }
                                })
                                .addOnFailureListener(e -> result.setValue(Result.error("Failed to fetch plant details.")));
                    }
                })
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to load favorites.")));

        return result;
    }

    @Override
    public LiveData<Result<String>> removeFromFavorites(String userId, String plantId) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        firestore.collection("user").document(userId)
                .collection("favorites").document(plantId)
                .delete()
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Removed from favorites")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to remove from favorites")));
        return result;
    }


    @Override
    public LiveData<Result<String>> removeFromHistory(String userId, String plantId) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        firestore.collection("user").document(userId)
                .collection("history").document(plantId)
                .delete()
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Removed from history")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to remove from history")));
        return result;
    }

    public void addFakeArticlesToFirestore() {
        List<ArticleItem> fakeArticles = new ArrayList<>();

        for (ArticleItem article : fakeArticles) {
            if (article.getArticleId() == null || article.getArticleId().isEmpty()) {
                article.setArticleId(UUID.randomUUID().toString()); // Assign a new random ID
            }
            firestore.collection("articles").document(article.getArticleId()).set(article)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Fake article added: " + article.getTitle()))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error adding fake article: " + article.getTitle(), e));
        }
    }
}
