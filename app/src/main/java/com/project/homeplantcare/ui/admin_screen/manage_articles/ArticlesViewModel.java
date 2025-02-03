package com.project.homeplantcare.ui.admin_screen.manage_articles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.project.homeplantcare.R;
import com.project.homeplantcare.models.ArticleItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ArticlesViewModel extends ViewModel {
    private final MutableLiveData<List<ArticleItem>> articlesLiveData = new MutableLiveData<>();
    private final List<ArticleItem> mockArticles = new ArrayList<>();

    @Inject
    public ArticlesViewModel() {
        loadMockData();
    }

    private void loadMockData() {
        mockArticles.add(new ArticleItem("How to Take Care of Indoor Plants", "Learn the best ways to take care of indoor plants.", "12 Jan 2024", R.drawable.plant_3));
        mockArticles.add(new ArticleItem("Best Plants for Small Spaces", "Discover plants that thrive in compact areas.", "10 Feb 2024", R.drawable.hoya4));
        mockArticles.add(new ArticleItem("Watering Tips for Beginners", "Avoid overwatering with these expert tips.", "5 March 2024", R.drawable.plant_4));

        articlesLiveData.setValue(mockArticles);
    }

    public LiveData<List<ArticleItem>> getArticlesLiveData() {
        return articlesLiveData;
    }

    public void deleteArticle(ArticleItem article) {
        mockArticles.remove(article);
        articlesLiveData.setValue(new ArrayList<>(mockArticles));
    }
}

