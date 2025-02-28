package com.project.homeplantcare.data.repo.network;

import com.project.homeplantcare.data.repo.network.ResponseModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("/predict")
    Call<ResponseModel> uploadImage(@Part MultipartBody.Part image);
}