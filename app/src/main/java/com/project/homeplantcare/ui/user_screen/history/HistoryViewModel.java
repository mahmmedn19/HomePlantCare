package com.project.homeplantcare.ui.user_screen.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HistoryViewModel extends ViewModel {

    private final AppRepository appRepository;

    @Inject
    public HistoryViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public LiveData<Result<List<PlantItem>>> getHistory(String userId) {
        return appRepository.getUserHistory(userId);
    }
}