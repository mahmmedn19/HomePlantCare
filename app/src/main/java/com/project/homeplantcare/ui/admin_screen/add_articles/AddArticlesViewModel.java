package com.project.homeplantcare.ui.admin_screen.add_articles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddArticlesViewModel extends ViewModel {

    public final MutableLiveData<String> articleTitle = new MutableLiveData<>("");
    public final MutableLiveData<String> contentPreview = new MutableLiveData<>("");
    public final MutableLiveData<String> articleDate = new MutableLiveData<>("");
    public final MutableLiveData<String> imageUri = new MutableLiveData<>("");

    public void clearArticleData() {
        articleTitle.setValue("");
        contentPreview.setValue("");
        articleDate.setValue("");
        imageUri.setValue("");
    }
}
