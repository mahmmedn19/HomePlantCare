package com.project.homeplantcare.ui.admin_screen.add_plant_admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.models.DiseaseItem;

import java.util.ArrayList;
import java.util.List;

public class AddPlantViewModel extends ViewModel {

    public MutableLiveData<String> plantName = new MutableLiveData<>("");
    public MutableLiveData<String> plantDescription = new MutableLiveData<>("");
    public MutableLiveData<String> lightRequirement = new MutableLiveData<>("");
    public MutableLiveData<String> waterRequirement = new MutableLiveData<>("");
    public MutableLiveData<String> soilRequirement = new MutableLiveData<>("");
    public MutableLiveData<String> weatherRequirement = new MutableLiveData<>("");
    public MutableLiveData<String> growthDate = new MutableLiveData<>("");
    public MutableLiveData<Integer> imageResId = new MutableLiveData<>(null);

    private final MutableLiveData<Boolean> isPlantSaved = new MutableLiveData<>(false);
    private final MutableLiveData<List<DiseaseItem>> diseaseList = new MutableLiveData<>();
    private final MutableLiveData<List<DiseaseItem>> selectedDiseases = new MutableLiveData<>(new ArrayList<>());

    public AddPlantViewModel() {
        loadMockDiseases(); // Load diseases on ViewModel creation
    }

    public LiveData<List<DiseaseItem>> getDiseaseList() {
        return diseaseList;
    }

    public LiveData<List<DiseaseItem>> getSelectedDiseases() {
        return selectedDiseases;
    }

    public void toggleDiseaseSelection(DiseaseItem disease) {
        List<DiseaseItem> currentSelection = selectedDiseases.getValue();
        if (currentSelection.contains(disease)) {
            currentSelection.remove(disease);
        } else {
            currentSelection.add(disease);
        }
        selectedDiseases.setValue(currentSelection);
    }

    public void loadMockDiseases() {
        List<DiseaseItem> mockDiseases = new ArrayList<>();
        mockDiseases.add(new DiseaseItem(1, "Leaf Spot", "Dark spots on leaves", "Remove affected leaves and apply fungicide"));
        mockDiseases.add(new DiseaseItem(2, "Powdery Mildew", "White powder on leaves", "Use baking soda spray or fungicide"));
        mockDiseases.add(new DiseaseItem(3, "Root Rot", "Soft and dark roots", "Reduce watering and improve drainage"));

        diseaseList.setValue(mockDiseases);
    }

    public LiveData<Boolean> getIsPlantSaved() {
        return isPlantSaved;
    }

    public void savePlant() {
        if (validateInputs()) {
            isPlantSaved.setValue(true);
        } else {
            isPlantSaved.setValue(false);
        }
    }

    private boolean validateInputs() {
        return !plantName.getValue().isEmpty() &&
                !plantDescription.getValue().isEmpty() &&
                !lightRequirement.getValue().isEmpty() &&
                !waterRequirement.getValue().isEmpty() &&
                !soilRequirement.getValue().isEmpty() &&
                !weatherRequirement.getValue().isEmpty() &&
                !growthDate.getValue().isEmpty();
    }
}
