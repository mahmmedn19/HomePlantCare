package com.project.homeplantcare.data.models;

import java.util.List;

public class HistoryItem {
    private String historyId;
    private String plantId;
    private String plantName;
    private List<DiseaseItem> diseases; // List of diseases related to this plant
    private String analysisDate;
    private String imageUrl;

    public HistoryItem() {
    }

    public HistoryItem(String plantName, List<DiseaseItem> diseases, String analysisDate, String imageUrl) {
        this.plantName = plantName;
        this.diseases = diseases;
        this.analysisDate = analysisDate;
        this.imageUrl = imageUrl;
    }

    public HistoryItem(String historyId, String plantId, String plantName, List<DiseaseItem> diseases, String analysisDate, String imageUrl) {
        this.historyId = historyId;
        this.plantId = plantId;
        this.plantName = plantName;
        this.diseases = diseases;
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


    public List<DiseaseItem> getDiseaseName() {
        return diseases;
    }

    public String getAnalysisDate() {
        return analysisDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
