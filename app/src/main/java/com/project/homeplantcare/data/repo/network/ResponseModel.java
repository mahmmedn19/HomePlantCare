package com.project.homeplantcare.data.repo.network;

import com.google.gson.annotations.SerializedName;

public class ResponseModel {
    @SerializedName("predicted_class")
    private String predictedClass;

    public String getPredictedClass() {
        return predictedClass;
    }
}