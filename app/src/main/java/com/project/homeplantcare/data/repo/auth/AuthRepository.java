package com.project.homeplantcare.data.repo.auth;

import androidx.lifecycle.LiveData;

import com.project.homeplantcare.data.models.AdminProfile;
import com.project.homeplantcare.data.models.User;
import com.project.homeplantcare.data.utils.Result;

import java.util.List;

public interface AuthRepository {
    // LOGIN ADMIN
    LiveData<Result<String>> loginAdmin(String email, String password);

    // LOGIN USER
    LiveData<Result<String>> loginUser(String email, String password);

    // REGISTER USER
    LiveData<Result<String>> registerUser(String email, String password, String username);

    // Get Admin Profile
    LiveData<Result<AdminProfile>> getAdminProfile();


    LiveData<Result<String>> sendPasswordResetEmail(String email);

    // Profile Operations
    LiveData<Result<User>> getUserProfile();
    LiveData<Result<String>> updateUserProfile(String newName);
    LiveData<Result<String>> updateUserPassword(String oldPassword, String newPassword);

}
