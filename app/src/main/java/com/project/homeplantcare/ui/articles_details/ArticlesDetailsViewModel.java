package com.project.homeplantcare.ui.articles_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ArticlesDetailsViewModel extends ViewModel {

    private final AppRepository repository;
    private final MutableLiveData<Result<ArticleItem>> articleDetails = new MutableLiveData<>();

    @Inject
    public ArticlesDetailsViewModel(AppRepository repository) {
        this.repository = repository;
    }

    // Fetch article details by ID
    public void fetchArticleDetails(String articleId) {
        articleDetails.postValue(Result.loading()); // Show loading state
        new android.os.Handler().postDelayed(() -> {
        repository.getAllArticles().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                ArticleItem article = findArticleById(result.getData(), articleId);
                if (article != null) {
                    articleDetails.setValue(Result.success(article)); // Set article details
                } else {
                    articleDetails.setValue(Result.error("Article not found"));
                }
            } else {
                articleDetails.setValue(Result.error("Failed to load articles"));
            }
        });
        }, 1000);
    }

    // Helper method to find an article by ID
    private ArticleItem findArticleById(List<ArticleItem> articles, String articleId) {
        for (ArticleItem article : articles) {
            if (article.getArticleId().equals(articleId)) {
                return article;
            }
        }
        return null;
    }

    // Get the article details LiveData
    public LiveData<Result<ArticleItem>> getArticleDetails() {
        return articleDetails;
    }
}
