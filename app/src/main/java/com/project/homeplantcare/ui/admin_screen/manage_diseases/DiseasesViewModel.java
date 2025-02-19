package com.project.homeplantcare.ui.admin_screen.manage_diseases;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.DiseaseItem;

import java.util.Arrays;
import java.util.List;

public class DiseasesViewModel extends ViewModel {
    private final MutableLiveData<List<DiseaseItem>> diseasesLiveData = new MutableLiveData<>();

    public DiseasesViewModel() {
        loadMockData();
    }

    public LiveData<List<DiseaseItem>> getDiseasesLiveData() {
        return diseasesLiveData;
    }

    public List<DiseaseItem> getMockDiseases() {
        return diseasesLiveData.getValue();
    }

    private void loadMockData() {
        List<DiseaseItem> mockDiseases = Arrays.asList(
                new DiseaseItem(1, "Powdery Mildew", "White powdery spots on leaves", "Apply fungicide, remove infected leaves"),
                new DiseaseItem(2, "Leaf Spot", "Brown spots with yellow halo", "Improve air circulation, use copper-based fungicide"),
                new DiseaseItem(3, "Root Rot", "Wilting, yellowing leaves, mushy roots", "Reduce watering, apply fungicide")
        );

        diseasesLiveData.setValue(mockDiseases);
    }
}
