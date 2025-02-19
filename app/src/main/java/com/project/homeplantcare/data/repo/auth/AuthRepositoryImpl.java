package com.project.homeplantcare.data.repo.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.homeplantcare.data.utils.Result;
import javax.inject.Inject;

public class AuthRepositoryImpl implements AuthRepository {

    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    @Inject
    public AuthRepositoryImpl(FirebaseAuth auth, FirebaseFirestore db) {
        this.auth = auth;
        this.db = db;
    }

    @Override
    public LiveData<Result<String>> loginAdmin(String email, String password) {
        return login(email, password, "admin");
    }

    @Override
    public LiveData<Result<String>> loginUser(String email, String password) {
        return login(email, password, "user");
    }

    private LiveData<Result<String>> login(String email, String password, String userType) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    result.setValue(Result.success(userType + " login successful."));
                })
                .addOnFailureListener(e -> result.setValue(Result.error(getFirebaseAuthErrorMessage(e))));
        return result;
    }

    private String getFirebaseAuthErrorMessage(Exception e) {
        if (e instanceof FirebaseAuthUserCollisionException) {
            return "This email is already registered.";
        } else if (e instanceof FirebaseAuthWeakPasswordException) {
            return "Password should be at least 6 characters.";
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            return "Invalid email format.";
        } else {
            return "Login failed: " + e.getMessage();
        }
    }
}
