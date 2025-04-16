package com.project.homeplantcare.data.models;

public class ArticleItem {
    private String articleId;
    private String title;
    private String contentPreview;
    private String date;
    private String imageResId;

    // Empty constructor
    public ArticleItem() {
    }

    // Constructor with all fields
    public ArticleItem(String articleId, String title, String contentPreview, String date, String imageResId) {
        this.articleId = articleId;
        this.title = title;
        this.contentPreview = contentPreview;
        this.date = date;
        this.imageResId = imageResId;
    }

    // Constructor without articleId (for new articles)
    public ArticleItem(String title, String contentPreview, String date, String imageResId) {
        this.title = title;
        this.contentPreview = contentPreview;
        this.date = date;
        this.imageResId = imageResId;
    }

    // Getters and Setters for all properties
    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentPreview() {
        return contentPreview;
    }

    public void setContentPreview(String contentPreview) {
        this.contentPreview = contentPreview;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageResId() {
        return imageResId;
    }

    public void setImageResId(String imageResId) {
        this.imageResId = imageResId;
    }
}
