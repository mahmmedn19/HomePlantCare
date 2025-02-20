package com.project.homeplantcare.ui.admin_screen.profile_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.repo.auth.AuthRepository;
import com.project.homeplantcare.data.repo.auth.AuthRepositoryImpl;
import com.project.homeplantcare.data.models.AdminProfile;
import com.project.homeplantcare.data.utils.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AdminProfileViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<Result<AdminProfile>> adminProfileLiveData = new MutableLiveData<>();
    private final MutableLiveData<Result<String>> updateProfileLiveData = new MutableLiveData<>();

    @Inject
    public AdminProfileViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    // Fetch Admin Profile
    public LiveData<Result<AdminProfile>> getAdminProfile() {
        return authRepository.getAdminProfile();
    }

    // Update Admin Profile
    public LiveData<Result<String>> updateAdminProfile(String newName) {
        return authRepository.updateAdminProfile(newName);
    }

    public LiveData<Result<String>> getUpdateProfileLiveData() {
        return updateProfileLiveData;
    }
}
