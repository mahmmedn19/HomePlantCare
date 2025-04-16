package com.project.homeplantcare.ui.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.project.homeplantcare.data.repo.auth.AuthRepository;
import com.project.homeplantcare.data.utils.Result;
import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RegisterViewModel extends ViewModel {

    private final AuthRepository authRepository;

    @Inject
    public RegisterViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Result<String>> registerUser(String email, String password, String username) {
        return authRepository.registerUser(email, password, username);
    }
}
