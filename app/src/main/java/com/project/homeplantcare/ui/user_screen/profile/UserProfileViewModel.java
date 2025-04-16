package com.project.homeplantcare.ui.user_screen.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.User;
import com.project.homeplantcare.data.repo.auth.AuthRepository;
import com.project.homeplantcare.data.utils.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserProfileViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<Boolean> isUpdatingProfile = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isUpdatingPassword = new MutableLiveData<>(false);

    @Inject
    public UserProfileViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Result<User>> getUserProfile() {
        return authRepository.getUserProfile();
    }

    public LiveData<Result<String>> updateUserProfile(String newName) {
        isUpdatingProfile.setValue(true);
        MutableLiveData<Result<String>> result = new MutableLiveData<>();

        authRepository.updateUserProfile(newName).observeForever(res -> {
            result.setValue(res);
            isUpdatingProfile.setValue(false);
        });

        return result;
    }

    public LiveData<Result<String>> updateUserPassword(String oldPassword, String newPassword) {
        isUpdatingPassword.setValue(true);
        MutableLiveData<Result<String>> result = new MutableLiveData<>();

        authRepository.updateUserPassword(oldPassword, newPassword).observeForever(res -> {
            result.setValue(res);
            isUpdatingPassword.setValue(false);
        });

        return result;
    }

    public LiveData<Boolean> isUpdatingProfile() {
        return isUpdatingProfile;
    }

    public LiveData<Boolean> isUpdatingPassword() {
        return isUpdatingPassword;
    }
}
