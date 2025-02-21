package com.project.homeplantcare.data.repo.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.homeplantcare.data.models.AdminProfile;
import com.project.homeplantcare.data.models.User;
import com.project.homeplantcare.data.utils.Result;

import java.util.Objects;

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
                    // Fetch the user info after successful login
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        String userId = user.getUid();
                        // Check if the user is an admin or a regular user
                        checkUserType(userId, email, password, userType, result);
                    } else {
                        result.setValue(Result.error("User not found."));
                    }
                })
                .addOnFailureListener(e -> result.setValue(Result.error(getFirebaseAuthErrorMessage(e))));
        return result;
    }

    // Check if the user is an admin or a user from respective collections
    private void checkUserType(String userId, String email, String password, String userType, MutableLiveData<Result<String>> result) {

        // Check if the user is trying to login as admin and handle accordingly
        if (userType.equals("admin")) {
            // First check if the user is in the 'admins' collection
            db.collection("admin").whereEqualTo("adminEmail", email).get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            // User is an admin
                            result.setValue(Result.success("Admin login successful."));
                        } else {
                            // Admin not found
                            result.setValue(Result.error("Admin not found."));
                        }
                    })
                    .addOnFailureListener(e -> result.setValue(Result.error("Failed to check admin: " + e.getMessage())));
        } else {
            // If user is not an admin, check in the 'users' collection
            db.collection("user").whereEqualTo("email", email).get()
                    .addOnSuccessListener(userSnapshot -> {
                        if (!userSnapshot.isEmpty()) {
                            // User is a regular user
                            result.setValue(Result.success("User login successful."));
                        } else {
                            result.setValue(Result.error("User not found."));
                        }
                    })
                    .addOnFailureListener(e -> result.setValue(Result.error("Failed to check user: " + e.getMessage())));
        }
    }



    @Override
    public LiveData<Result<String>> registerUser(String email, String password, String username) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // Successfully registered, now save additional user info to Firestore
                    String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid(); // Get the UID
                    saveUserToFirestore(userId, username, email, result);
                })
                .addOnFailureListener(e -> result.setValue(Result.error(getFirebaseAuthErrorMessage(e))));
        return result;
    }

    private void saveUserToFirestore(String userId, String username, String email, MutableLiveData<Result<String>> result) {
        // Save user data to Firestore
        User user = new User(username, email, userId);  // Include UID in the User object
        db.collection("user").document(userId)  // Use userId as document ID
                .set(user)
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("User registered and data saved successfully!")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to save user data: " + e.getMessage())));
    }

    @Override
    public LiveData<Result<AdminProfile>> getAdminProfile() {
        MutableLiveData<Result<AdminProfile>> liveData = new MutableLiveData<>();
        liveData.setValue(Result.loading());
        FirebaseUser currentUser = auth.getCurrentUser();
        assert currentUser != null;
        String uid = currentUser.getUid();

        db.collection("admin")
                .document(uid)  // Assuming a single admin document
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        AdminProfile adminProfile = documentSnapshot.toObject(AdminProfile.class);
                        liveData.setValue(Result.success(adminProfile));
                    } else {
                        liveData.setValue(Result.error("Admin profile not found"));
                    }
                })
                .addOnFailureListener(e -> liveData.setValue(Result.error(e.getMessage())));

        return liveData;
    }

    @Override
    public LiveData<Result<String>> updateAdminProfile(String newName) {
        MutableLiveData<Result<String>> liveData = new MutableLiveData<>();
        liveData.setValue(Result.loading());
        FirebaseUser currentUser = auth.getCurrentUser();
        assert currentUser != null;
        String uid = currentUser.getUid();
        db.collection("admin")
                .document(uid)  // Assuming adminId uniquely identifies the admin document
                .update("adminName", newName)
                .addOnSuccessListener(aVoid -> liveData.setValue(Result.success("Profile updated successfully")))
                .addOnFailureListener(e -> liveData.setValue(Result.error("Failed to update profile: " + e.getMessage())));

        return liveData;
    }

    @Override
    public LiveData<Result<String>> sendPasswordResetEmail(String email) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("Reset link sent to your email.")))
                .addOnFailureListener(e -> {
                    String errorMessage = getFirebaseAuthErrorMessage(Objects.requireNonNull(e));
                    resultLiveData.setValue(Result.error(errorMessage));
                });
        return resultLiveData;
    }

    private String getFirebaseAuthErrorMessage(Exception e) {
        if (e instanceof FirebaseAuthUserCollisionException) {
            return "This email is already registered.";
        } else if (e instanceof FirebaseAuthWeakPasswordException) {
            return "Password should be at least 6 characters.";
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            return "Invalid email or password.";
        } else {
            return "Login failed: " + e.getMessage();
        }
    }
}
