package com.project.homeplantcare.ui.user_screen.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.HistoryItem;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HistoryViewModel extends ViewModel {
    private final AppRepository appRepository;
    private final MutableLiveData<Result<List<HistoryItem>>> historyLiveData = new MutableLiveData<>();

    @Inject
    public HistoryViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public LiveData<Result<List<HistoryItem>>> getHistory(String userId) {
        historyLiveData.setValue(Result.loading());
        appRepository.getUserHistory(userId).observeForever(historyLiveData::setValue);
        return historyLiveData;
    }

    public LiveData<Result<String>> removeFromHistory(String userId, String plantId) {
        return appRepository.removeFromHistory(userId, plantId);
    }
}
