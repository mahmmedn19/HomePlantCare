package com.project.homeplantcare.ui.user_screen.fav_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FavViewModel extends ViewModel {
    private final AppRepository appRepository;

    @Inject
    public FavViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public LiveData<Result<List<PlantItem>>> getFavorites(String userId) {
        return appRepository.getUserFavorites(userId);
    }

    public LiveData<Result<String>> removeFavorite(String userId, String plantId) {
        return appRepository.removeFromFavorites(userId, plantId);
    }
}
