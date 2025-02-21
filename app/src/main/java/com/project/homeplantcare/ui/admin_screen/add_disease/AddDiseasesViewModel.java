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

    private final MutableLiveData<String> diseaseName = new MutableLiveData<>("");
    private final MutableLiveData<String> diseaseSymptoms = new MutableLiveData<>("");
    private final MutableLiveData<String> diseaseRemedies = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isDiseaseSaved = new MutableLiveData<>(false);
    private final MutableLiveData<DiseaseItem> diseaseItemLiveData = new MutableLiveData<>();

    @Inject
    public AddDiseasesViewModel(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    // Add disease
    public void addDisease() {
        if (validateFields()) {
            DiseaseItem diseaseItem = new DiseaseItem(
                    diseaseName.getValue(),
                    diseaseSymptoms.getValue(),
                    diseaseRemedies.getValue()
            );

            appRepository.addDisease(diseaseItem).observeForever(result -> {
                if (result.getStatus() == Result.Status.SUCCESS) {
                    isDiseaseSaved.setValue(true);
                } else {
                    isDiseaseSaved.setValue(false);
                }
            });
        } else {
            isDiseaseSaved.setValue(false);
        }
    }

    // Update disease
    public void updateDisease(String diseaseId) {
        if (validateFields()) {
            DiseaseItem diseaseItem = new DiseaseItem(
                    diseaseId,
                    diseaseName.getValue(),
                    diseaseSymptoms.getValue(),
                    diseaseRemedies.getValue()
            );

            appRepository.updateDisease(diseaseId, diseaseItem).observeForever(result -> {
                if (result.getStatus() == Result.Status.SUCCESS) {
                    isDiseaseSaved.setValue(true);
                } else {
                    isDiseaseSaved.setValue(false);
                }
            });
        } else {
            isDiseaseSaved.setValue(false);
        }
    }

    // Get disease by ID
    public void getDiseaseById(String diseaseId) {
        appRepository.getAllDiseases().observeForever(result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                for (DiseaseItem disease : result.getData()) {
                    if (disease.getDiseaseId().equals(diseaseId)) {
                        diseaseItemLiveData.setValue(disease);
                        break;
                    }
                }
            }
        });
    }

    // Validate fields
    private boolean validateFields() {
        return !diseaseName.getValue().isEmpty() && !diseaseSymptoms.getValue().isEmpty() && !diseaseRemedies.getValue().isEmpty();
    }

    // Getter methods for LiveData
    public LiveData<String> getDiseaseName() {
        return diseaseName;
    }

    public LiveData<String> getDiseaseSymptoms() {
        return diseaseSymptoms;
    }

    public LiveData<String> getDiseaseRemedies() {
        return diseaseRemedies;
    }

    public LiveData<Boolean> getIsDiseaseSaved() {
        return isDiseaseSaved;
    }

    public LiveData<DiseaseItem> getDiseaseItemLiveData() {
        return diseaseItemLiveData;
    }

    // Setter methods for input fields
    public void setDiseaseName(String name) {
        diseaseName.setValue(name);
    }

    public void setDiseaseSymptoms(String symptoms) {
        diseaseSymptoms.setValue(symptoms);
    }

    public void setDiseaseRemedies(String remedies) {
        diseaseRemedies.setValue(remedies);
    }
}
