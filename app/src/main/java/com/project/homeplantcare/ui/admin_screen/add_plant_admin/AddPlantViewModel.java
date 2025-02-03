package com.project.homeplantcare.ui.admin_screen.add_plant_admin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
