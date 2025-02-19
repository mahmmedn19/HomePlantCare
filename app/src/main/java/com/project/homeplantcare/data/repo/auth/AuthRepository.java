package com.project.homeplantcare.data.repo.auth;

import androidx.lifecycle.LiveData;
import com.project.homeplantcare.data.utils.Result;

public interface AuthRepository {
    // LOGIN ADMIN
    LiveData<Result<String>> loginAdmin(String email, String password);
    LiveData<Result<String>> loginUser(String email, String password);

}
