package com.project.homeplantcare.data.models;

public class ArticleItem {
    private String title;
    private String contentPreview;
    private String date;
    private int imageResId;

    public ArticleItem(String title, String contentPreview, String date, int imageResId) {
        this.title = title;
        this.contentPreview = contentPreview;
        this.date = date;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getContentPreview() {
        return contentPreview;
    }

    public String getDate() {
        return date;
    }

    public int getImageResId() {
        return imageResId;
    }
}
