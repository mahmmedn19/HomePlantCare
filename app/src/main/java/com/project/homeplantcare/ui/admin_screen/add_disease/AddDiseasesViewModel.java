package com.project.homeplantcare.ui.admin_screen.add_disease;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.utils.Result;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddDiseasesViewModel extends ViewModel {

    private final AppRepository appRepository;
    private final MutableLiveData<Result<DiseaseItem>> diseaseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<String>> operationResult = new MutableLiveData<>();

    @Inject
    public AddDiseasesViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public void addDisease(DiseaseItem diseaseItem) {
        appRepository.addDisease(diseaseItem).observeForever(operationResult::setValue);
    }

    public void updateDisease(String diseaseId, DiseaseItem diseaseItem) {
        if (diseaseId == null || diseaseId.trim().isEmpty()) {
            operationResult.setValue(Result.error("Invalid disease ID."));
            return;
        }

        appRepository.updateDisease(diseaseId, diseaseItem).observeForever(operationResult::setValue);
    }

    public LiveData<Result<DiseaseItem>> getDiseaseById(String diseaseId) {
        appRepository.getAllDiseases().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                DiseaseItem foundDisease = result.getData().stream()
                        .filter(disease -> disease.getDiseaseId().equals(diseaseId))
                        .findFirst()
                        .orElse(null);

                if (foundDisease != null) {
                    diseaseLiveData.setValue(Result.success(foundDisease));
                } else {
                    diseaseLiveData.setValue(Result.error("Disease not found."));
                }
            } else {
                diseaseLiveData.setValue(Result.error("Failed to fetch disease data."));
            }
        });
        return diseaseLiveData;
    }

    public LiveData<Result<String>> getOperationResult() {
        return operationResult;
    }
}