package com.project.homeplantcare.data.models;

import java.util.List;

public class PlantItem {

    private String plantId;
    private String name;
    private String description;
    private String lightRequirements;
    private String waterRequirements;
    private String soilRequirements;
    private String weatherRequirements;
    private String growthDate;
    private String imageResId; // Image stored as a Base64 string
    private List<DiseaseItem> diseases; // List of diseases related to this plant

    // Empty constructor
    public PlantItem() {}

    // Constructor with all fields
    public PlantItem(String plantId, String name, String description, String lightRequirements,
                     String waterRequirements, String soilRequirements, String weatherRequirements,
                     String growthDate, String imageResId, List<DiseaseItem> diseases) {
        this.plantId = plantId;
        this.name = name;
        this.description = description;
        this.lightRequirements = lightRequirements;
        this.waterRequirements = waterRequirements;
        this.soilRequirements = soilRequirements;
        this.weatherRequirements = weatherRequirements;
        this.growthDate = growthDate;
        this.imageResId = imageResId;
        this.diseases = diseases;
    }

    // Constructor for new plants (without plantId)
    public PlantItem(String name, String description, String lightRequirements,
                     String imageResId, List<DiseaseItem> diseases) {
        this.name = name;
        this.description = description;
        this.lightRequirements = lightRequirements;
        this.imageResId = imageResId;
        this.diseases = diseases;
    }

    // Getters and Setters
    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLightRequirements() {
        return lightRequirements;
    }

    public void setLightRequirements(String lightRequirements) {
        this.lightRequirements = lightRequirements;
    }

    public String getWaterRequirements() {
        return waterRequirements;
    }

    public void setWaterRequirements(String waterRequirements) {
        this.waterRequirements = waterRequirements;
    }

    public String getSoilRequirements() {
        return soilRequirements;
    }

    public void setSoilRequirements(String soilRequirements) {
        this.soilRequirements = soilRequirements;
    }

    public String getWeatherRequirements() {
        return weatherRequirements;
    }

    public void setWeatherRequirements(String weatherRequirements) {
        this.weatherRequirements = weatherRequirements;
    }

    public String getGrowthDate() {
        return growthDate;
    }

    public void setGrowthDate(String growthDate) {
        this.growthDate = growthDate;
    }

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }

    public List<DiseaseItem> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<DiseaseItem> diseases) {
        this.diseases = diseases;
    }
}
