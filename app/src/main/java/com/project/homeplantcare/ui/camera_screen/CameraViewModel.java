package com.project.homeplantcare.ui.camera_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CameraViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isProcessing = new MutableLiveData<>(false);
    private final MutableLiveData<String> analysisResult = new MutableLiveData<>();

    public LiveData<Boolean> getIsProcessing() {
        return isProcessing;
    }

    public LiveData<String> getAnalysisResult() {
        return analysisResult;
    }

    public void analyzeImage() {
        isProcessing.setValue(true);
        new Thread(() -> {
            try {
                Thread.sleep(3000); // Simulating AI processing time
                analysisResult.postValue("Detected: Powdery Mildew");
            } catch (InterruptedException e) {
                analysisResult.postValue("Error processing image.");
            }
            isProcessing.postValue(false);
        }).start();
    }
}
