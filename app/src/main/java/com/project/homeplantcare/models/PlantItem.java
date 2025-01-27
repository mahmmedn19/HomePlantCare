package com.project.homeplantcare.models;

public class PlantItem {
    private int plantId; // Unique ID for the plant
    private String name; // Plant name
    private String description; // Plant description
    private String lightRequirements; // Light requirements (e.g., "Indoor")
    private String waterRequirements; // Watering needs (e.g., "Medium")
    private String difficulty; // Maintenance difficulty (e.g., "Easy")
    private double price; // Plant price
    private int imageResId; // Resource ID for the plant image (local drawable for now)

    public PlantItem(String name, String description, double price, int imageResId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
    }

    public PlantItem(int plantId, String name, String description, String lightRequirements,
                     String waterRequirements, String difficulty, double price, int imageResId) {
        this.plantId = plantId;
        this.name = name;
        this.description = description;
        this.lightRequirements = lightRequirements;
        this.waterRequirements = waterRequirements;
        this.difficulty = difficulty;
        this.price = price;
        this.imageResId = imageResId;
    }

    // Partial Constructor (Without Image)
    public PlantItem(int plantId, String name, String description, String lightRequirements,
                     String waterRequirements, String difficulty, double price) {
        this.plantId = plantId;
        this.name = name;
        this.description = description;
        this.lightRequirements = lightRequirements;
        this.waterRequirements = waterRequirements;
        this.difficulty = difficulty;
        this.price = price;
        this.imageResId = 0; // Default or no image
    }

    // Minimal Constructor (ID, Name, and Price Only)
    public PlantItem(int plantId, String name, double price) {
        this.plantId = plantId;
        this.name = name;
        this.description = "";
        this.lightRequirements = "";
        this.waterRequirements = "";
        this.difficulty = "";
        this.price = price;
        this.imageResId = 0; // Default or no image
    }

    // Empty Constructor (Useful for Database Libraries like Room/Firebase)
    public PlantItem() {
        this.plantId = 0;
        this.name = "";
        this.description = "";
        this.lightRequirements = "";
        this.waterRequirements = "";
        this.difficulty = "";
        this.price = 0.0;
        this.imageResId = 0;
    }

    // Getters
    public int getPlantId() {
        return plantId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLightRequirements() {
        return lightRequirements;
    }

    public String getWaterRequirements() {
        return waterRequirements;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }

    // Setters
    public void setPlantId(int plantId) {
        this.plantId = plantId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLightRequirements(String lightRequirements) {
        this.lightRequirements = lightRequirements;
    }

    public void setWaterRequirements(String waterRequirements) {
        this.waterRequirements = waterRequirements;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
