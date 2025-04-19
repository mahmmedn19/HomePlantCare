package com.project.homeplantcare.data.models;

import java.util.List;

public class HistoryItem {
    private String historyId;
    private String plantId;
    private String plantName;
    private String diseaseName; // Name of the disease
    private String analysisDate;
    private String imageUrl;

    public HistoryItem() {
    }

    public HistoryItem(String plantName, String analysisDate, String imageUrl) {
        this.plantName = plantName;
        this.analysisDate = analysisDate;
        this.imageUrl = imageUrl;
    }

    public HistoryItem(String historyId, String plantId, String plantName, String diseaseName, String analysisDate, String imageUrl) {
        this.historyId = historyId;
        this.plantId = plantId;
        this.plantName = plantName;
        this.diseaseName = diseaseName;
        this.analysisDate = analysisDate;
        this.imageUrl = imageUrl;
    }

    public String getHistoryId() {
        return historyId;
    }

    public String getPlantId() {
        return plantId;
    }

    public String getPlantName() {
        return plantName;
    }



    public String getDiseaseNameString() {
        return diseaseName;
    }

    public String getAnalysisDate() {
        return analysisDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
