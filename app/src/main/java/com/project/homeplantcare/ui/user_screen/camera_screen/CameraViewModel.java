package com.project.homeplantcare.ui.user_screen.camera_screen;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.utils.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CameraViewModel extends ViewModel {

    private final AppRepository appRepository;

    @Inject
    public CameraViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public LiveData<Result<String>> uploadImage(Uri imageUri) {
        return appRepository.uploadImage(imageUri);
    }

    public LiveData<Result<String>> getPlantIdByName(String plantName) {
        return appRepository.getPlantIdByName(plantName);
    }
}