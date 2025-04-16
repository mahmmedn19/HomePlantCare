package com.project.homeplantcare.ui.user_screen.camera_screen;

import android.net.Uri;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.utils.Result;

import java.io.File;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CameraViewModel extends ViewModel {

    private final AppRepository appRepository;

    @Inject
    public CameraViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public LiveData<Result<Pair<String, Boolean>>>  uploadImage(File imageUri) {
        return appRepository.uploadImage(imageUri);
    }

    public LiveData<Result<PlantItem>> getPlantIdByName(String plantName) {
        return appRepository.getPlantIdByName(plantName);
    }
}