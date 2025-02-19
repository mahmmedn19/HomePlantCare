package com.project.homeplantcare.data.models;

public class PlantItem {
    private int plantId;
    private String name;
    private String description;
    private String lightRequirements;
    private String waterRequirements;
    private String soilRequirements;
    private String weatherRequirements;
    private String growthDate;
    private int imageResId;

    // Constructor with minimal fields
    public PlantItem(String lightRequirements, String name, int imageResId) {
        this.lightRequirements = lightRequirements;
        this.name = name;
        this.imageResId = imageResId;
    }

    // Constructor with all fields
    public PlantItem(int plantId, String name, String description, String lightRequirements,
                     String waterRequirements, String soilRequirements, String weatherRequirements,
                     String growthDate, int imageResId) {
        this.plantId = plantId;
        this.name = name;
        this.description = description;
        this.lightRequirements = lightRequirements;
        this.waterRequirements = waterRequirements;
        this.soilRequirements = soilRequirements;
        this.weatherRequirements = weatherRequirements;
        this.growthDate = growthDate;
        this.imageResId = imageResId;
    }

    // Minimal constructor for new plants
    public PlantItem(String lightRequirements,String name, String description , int imageResId) {
        this.name = name;
        this.description = description;
        this.lightRequirements = lightRequirements;
        this.imageResId = imageResId;
    }

    // Getters & Setters
    public int getPlantId() {
        return plantId;
    }

    public void setPlantId(int plantId) {
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

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
