// AddArticlesViewModel.java
package com.project.homeplantcare.ui.admin_screen.add_articles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.ArticleItem;
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
    private final MutableLiveData<Boolean> isArticleSaved = new MutableLiveData<>(false);
    private final MutableLiveData<ArticleItem> articleItemLiveData = new MutableLiveData<>();

    @Inject
    public AddArticlesViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void addArticle(ArticleItem article) {
        appRepository.addArticle(article).observeForever(result -> {
            isArticleSaved.setValue(result.getStatus() == Result.Status.SUCCESS);
        });
    }

    public void updateArticle(String articleId, ArticleItem article) {
        appRepository.updateArticle(articleId, article).observeForever(result -> {
            isArticleSaved.setValue(result.getStatus() == Result.Status.SUCCESS);
        });
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

    public LiveData<Boolean> getIsArticleSaved() {
        return isArticleSaved;
    }

    public LiveData<ArticleItem> getArticleItemLiveData() {
        return articleItemLiveData;
    }
}