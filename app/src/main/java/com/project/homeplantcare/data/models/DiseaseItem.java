package com.project.homeplantcare.data.models;

public class DiseaseItem {
    private String diseaseId;
    private String name;
    private String symptoms;
    private String remedies;
    private String imageResId;

    // Empty constructor
    public DiseaseItem() {
    }

    // Constructor with all fields
    public DiseaseItem(String diseaseId, String name, String symptoms, String remedies, String imageResId) {
        this.diseaseId = diseaseId;
        this.name = name;
        this.symptoms = symptoms;
        this.remedies = remedies;
        this.imageResId = imageResId;
    }

    // Constructor with all fields
    public DiseaseItem(String name, String symptoms, String remedies, String imageResId) {
        this.name = name;
        this.symptoms = symptoms;
        this.remedies = remedies;
        this.imageResId = imageResId;
    }

    // Getters and Setters
    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getRemedies() {
        return remedies;
    }

    public void setRemedies(String remedies) {
        this.remedies = remedies;
    }

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

}
