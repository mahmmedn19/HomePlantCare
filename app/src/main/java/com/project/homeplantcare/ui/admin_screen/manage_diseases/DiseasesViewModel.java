package com.project.homeplantcare.ui.admin_screen.manage_diseases;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.utils.Result;

import java.util.List;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DiseasesViewModel extends ViewModel {

    private final AppRepository appRepository;

    public DiseasesViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }


    public LiveData<Result<List<DiseaseItem>>> getAllDiseases() {
        return appRepository.getAllDiseases();
    }

    // Delete a disease
    public void deleteDisease(DiseaseItem disease) {
        appRepository.deleteDisease(disease.getDiseaseId()).observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                // Reload diseases after deleting
                getAllDiseases();
            }
        });
    }
}
