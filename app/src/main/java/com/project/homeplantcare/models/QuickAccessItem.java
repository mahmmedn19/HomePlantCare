package com.project.homeplantcare.models;

public class QuickAccessItem {
    private int iconResId;
    private String label;

    public QuickAccessItem(int iconResId, String label) {
        this.iconResId = iconResId;
        this.label = label;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getLabel() {
        return label;
    }
}
