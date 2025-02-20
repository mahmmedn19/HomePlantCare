package com.project.homeplantcare.data.models;

public class DiseaseItem {
    private String diseaseId;
    private String name;
    private String symptoms;
    private String remedies;

    // Empty constructor
    public DiseaseItem() {}

    // Constructor with all fields
    public DiseaseItem(String diseaseId, String name, String symptoms, String remedies) {
        this.diseaseId = diseaseId;
        this.name = name;
        this.symptoms = symptoms;
        this.remedies = remedies;
    }
    // Constructor with all fields
    public DiseaseItem(String name, String symptoms, String remedies) {
        this.name = name;
        this.symptoms = symptoms;
        this.remedies = remedies;
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
}
