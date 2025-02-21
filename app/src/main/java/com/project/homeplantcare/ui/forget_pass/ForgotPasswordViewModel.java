package com.project.homeplantcare.ui.forget_pass;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.data.repo.auth.AuthRepository;
import com.project.homeplantcare.data.utils.Result;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ForgotPasswordViewModel extends ViewModel {

    private final AuthRepository authRepository;
    private final MutableLiveData<Result<String>> resetPasswordResult = new MutableLiveData<>();

    @Inject
    public ForgotPasswordViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Result<String>> getResetPasswordResult() {
        return resetPasswordResult;
    }

    public void resetPassword(String email) {
        resetPasswordResult.setValue(Result.loading());
        authRepository.sendPasswordResetEmail(email).observeForever(resetPasswordResult::setValue);
    }
}