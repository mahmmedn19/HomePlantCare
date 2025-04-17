package com.project.homeplantcare.data.models;

public class ResultApi {
    private String imageUrl;
    private String analysisDate;
    private String result;
    private String plantName;
    private String diseaseName;

    public ResultApi() {
    }

    public ResultApi(String imageUrl, String analysisDate, String result, String plantName, String diseaseName) {
        this.imageUrl = imageUrl;
        this.analysisDate = analysisDate;
        this.result = result;
        this.plantName = plantName;
        this.diseaseName = diseaseName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAnalysisDate() {
        return analysisDate;
    }
    public String getPlantName() {
        return plantName;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public String getResult() {
        return result;
    }
}
