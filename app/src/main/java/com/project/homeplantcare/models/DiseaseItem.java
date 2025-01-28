package com.project.homeplantcare.models;

public class DiseaseItem  {
    private int diseaseId;
    private String name;
    private String symptoms;
    private String remedies;

    public DiseaseItem(int diseaseId, String name, String symptoms, String remedies) {
        this.diseaseId = diseaseId;
        this.name = name;
        this.symptoms = symptoms;
        this.remedies = remedies;
    }

    public int getDiseaseId() { return diseaseId; }
    public String getName() { return name; }
    public String getSymptoms() { return symptoms; }
    public String getRemedies() { return remedies; }
}
