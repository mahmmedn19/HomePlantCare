package com.project.homeplantcare.data.models;

public class ResultApi {
    private String imageUrl;
    private String analysisDate;
    private String result;

    public ResultApi() {
    }

    public ResultApi(String imageUrl, String analysisDate, String result) {
        this.imageUrl = imageUrl;
        this.analysisDate = analysisDate;
        this.result = result;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAnalysisDate() {
        return analysisDate;
    }

    public String getResult() {
        return result;
    }
}
