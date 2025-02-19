package com.project.homeplantcare.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.project.homeplantcare.data.repo.auth.AuthRepository;
import com.project.homeplantcare.data.utils.Result;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final AuthRepository authRepository;

    @Inject
    public LoginViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Result<String>> loginAdmin(String email, String password) {
        return authRepository.loginAdmin(email, password);
    }

    public LiveData<Result<String>> loginUser(String email, String password) {
        return authRepository.loginUser(email, password);
    }
}
