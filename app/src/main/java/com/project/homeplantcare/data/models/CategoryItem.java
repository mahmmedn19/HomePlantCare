package com.project.homeplantcare.data.models;

public class CategoryItem {
    private String name;
    private boolean isSelected;

    public CategoryItem(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
