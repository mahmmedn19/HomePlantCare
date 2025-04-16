package com.project.homeplantcare.ui.admin_screen.manage_articles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.repo.app_repo.AppRepositoryImpl;
import com.project.homeplantcare.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ArticlesViewModel extends ViewModel {

    private final AppRepository appRepository;

    @Inject
    public ArticlesViewModel(AppRepositoryImpl appRepository) {
        this.appRepository = appRepository;
    }


    // Fetch all articles from Firestore
    public LiveData<Result<List<ArticleItem>>> getAllArticles() {
        return appRepository.getAllArticles();
    }


    // Delete an article
    public void deleteArticle(String articleId) {
        appRepository.deleteArticle(articleId).observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                // Reload articles after deleting
                getAllArticles();
            }
        });
    }
}
