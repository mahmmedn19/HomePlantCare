package com.project.homeplantcare.ui.admin_screen.add_disease;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddDiseasesViewModel extends ViewModel {

    private final MutableLiveData<String> diseaseName = new MutableLiveData<>("");
    private final MutableLiveData<String> diseaseSymptoms = new MutableLiveData<>("");
    private final MutableLiveData<String> diseaseRemedies = new MutableLiveData<>("");

    public LiveData<String> getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String name) {
        diseaseName.setValue(name);
    }

    public LiveData<String> getDiseaseSymptoms() {
        return diseaseSymptoms;
    }

    public void setDiseaseSymptoms(String symptoms) {
        diseaseSymptoms.setValue(symptoms);
    }

    public LiveData<String> getDiseaseRemedies() {
        return diseaseRemedies;
    }

    public void setDiseaseRemedies(String remedies) {
        diseaseRemedies.setValue(remedies);
    }
}
