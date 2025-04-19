package com.project.homeplantcare.ui.admin_screen.profile_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.repo.auth.AuthRepository;
import com.project.homeplantcare.data.models.AdminProfile;
import com.project.homeplantcare.data.utils.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AdminProfileViewModel extends ViewModel {

    private final AuthRepository authRepository;

    @Inject
    public AdminProfileViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    // Fetch Admin Profile
    public LiveData<Result<AdminProfile>> getAdminProfile() {
        return authRepository.getAdminProfile();
    }

    // Admin name is not editable, so no update logic needed
}