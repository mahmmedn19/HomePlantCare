package com.project.homeplantcare.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.utils.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private final AppRepository aiRepository;
    private final MutableLiveData<Result<String>> aiLinkLiveData = new MutableLiveData<>();

    @Inject
    public MainViewModel(AppRepository aiRepository) {
        this.aiRepository = aiRepository;
        fetchAILink(); // Auto-fetch on ViewModel creation
    }

    public void fetchAILink() {
        aiLinkLiveData.setValue(Result.loading());

        aiRepository.getSingleAILink().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                aiLinkLiveData.setValue(Result.success(result.getData()));
            } else {
                aiLinkLiveData.setValue(Result.error(result.getErrorMessage()));
            }
        });
    }

    public LiveData<Result<String>> getSingleAILink() {
        return aiLinkLiveData;
    }
}
