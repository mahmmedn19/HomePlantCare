package com.project.homeplantcare.ui.admin_screen.manage_user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.models.User;
import com.project.homeplantcare.data.repo.auth.AuthRepository;
import com.project.homeplantcare.data.utils.Result;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserViewModel extends ViewModel {
    private final AuthRepository authRepository;

    @Inject
    public UserViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Result<List<User>>> getAllUsers() {
        return authRepository.getAllUsers();
    }

    public LiveData<Result<String>> blockUser(String userId) {
        return authRepository.blockUser(userId);
    }

    public LiveData<Result<String>> unblockUser(String userId) {
        return authRepository.unblockUser(userId);
    }
}