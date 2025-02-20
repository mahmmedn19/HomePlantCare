// AddArticlesViewModel.java
package com.project.homeplantcare.ui.admin_screen.add_articles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.utils.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddArticlesViewModel extends ViewModel {

    private final AppRepository appRepository;
    private final MutableLiveData<String> title = new MutableLiveData<>("");
    private final MutableLiveData<String> content = new MutableLiveData<>("");
    private final MutableLiveData<String> date = new MutableLiveData<>(getCurrentDate());
    private final MutableLiveData<String> imageUrl = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isArticleSaved = new MutableLiveData<>(false);
    private final MutableLiveData<ArticleItem> articleItemLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public AddArticlesViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void addArticle(ArticleItem article) {
        if (validateFields()) {

            appRepository.addArticle(article).observeForever(result -> {
                isArticleSaved.setValue(result.getStatus() == Result.Status.SUCCESS);
            });
        } else {
            isArticleSaved.setValue(false);
        }
    }

    public void updateArticle(String articleId, ArticleItem article) {
        if (validateFields()) {
            appRepository.updateArticle(articleId, article).observeForever(result -> {
                isArticleSaved.setValue(result.getStatus() == Result.Status.SUCCESS);
            });
        } else {
            isArticleSaved.setValue(false);
        }
    }

    public void getArticleById(String articleId) {
        appRepository.getAllArticles().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                for (ArticleItem article : result.getData()) {
                    if (article.getArticleId().equals(articleId)) {
                        articleItemLiveData.setValue(article);
                        break;
                    }
                }
            }
        });
    }

    private boolean validateFields() {
        return title.getValue() != null && !title.getValue().isEmpty()
                && content.getValue() != null && !content.getValue().isEmpty()
                && date.getValue() != null && !date.getValue().isEmpty();
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public LiveData<String> getContent() {
        return content;
    }

    public LiveData<String> getDate() {
        return date;
    }

    public LiveData<String> getImageUrl() {
        return imageUrl;
    }

    public LiveData<Boolean> getIsArticleSaved() {
        return isArticleSaved;
    }

    public LiveData<ArticleItem> getArticleItemLiveData() {
        return articleItemLiveData;
    }

    public void setTitle(String newTitle) {
        title.setValue(newTitle);
    }

    public void setContent(String newContent) {
        content.setValue(newContent);
    }

    public void setDate(String newDate) {
        date.setValue(newDate);
    }

    public void setImageUrl(String newImageUrl) {
        imageUrl.setValue(newImageUrl);
    }

    public void clearArticleData() {
        title.setValue("");
        content.setValue("");
        date.setValue(getCurrentDate());
        imageUrl.setValue("");
    }
}