package com.project.homeplantcare.data.repo.auth;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.project.homeplantcare.data.models.AdminProfile;
import com.project.homeplantcare.data.models.User;
import com.project.homeplantcare.data.utils.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
                        // Check if the user is an admin or a regular user
                        checkUserType(email, userType, result);
                    } else {
                        result.setValue(Result.error("User not found."));
                    }
                })
                .addOnFailureListener(e -> result.setValue(Result.error(getFirebaseAuthErrorMessage(e))));
        return result;
    }

    // Check if the user is an admin or a user from respective collections
    private void checkUserType(String email, String userType, MutableLiveData<Result<String>> result) {

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
            // Check if the user is in the 'users' collection
            db.collection("user").whereEqualTo("email", email).get()
                    .addOnSuccessListener(userSnapshot -> {
                        if (!userSnapshot.isEmpty()) {
                            // Extract user document
                            DocumentSnapshot userDoc = userSnapshot.getDocuments().get(0);

                            // Check if the user is blocked
                            Boolean isBlocked = userDoc.getBoolean("isBlocked");
                            if (isBlocked != null && isBlocked) {
                                result.setValue(Result.error("User is blocked. Please contact support."));
                            } else {
                                result.setValue(Result.success("User login successful."));
                            }
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
        User user = new User(username, email, userId,false);  // Include UID in the User object
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
        if (currentUser == null) {
            liveData.setValue(Result.error("User not authenticated."));
            return liveData;
        }

        db.collection("admin").document(currentUser.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        AdminProfile adminProfile = documentSnapshot.toObject(AdminProfile.class);
                        liveData.setValue(Result.success(adminProfile));
                    } else {
                        liveData.setValue(Result.error("Admin profile not found."));
                    }
                })
                .addOnFailureListener(e -> liveData.setValue(Result.error("Error fetching admin profile: " + e.getMessage())));

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

    @Override
    public LiveData<Result<User>> getUserProfile() {
        MutableLiveData<Result<User>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            result.setValue(Result.error("User not authenticated."));
            return result;
        }

        String uid = currentUser.getUid();
        Log.d("Firestore", "Fetching profile for UID: " + uid);

        db.collection("user").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User userProfile = documentSnapshot.toObject(User.class);
                        Log.d("Firestore", "User profile retrieved: " + userProfile);
                        result.setValue(Result.success(userProfile));
                    } else {
                        Log.e("Firestore", "User profile not found.");
                        result.setValue(Result.error("User profile not found."));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error retrieving profile: " + e.getMessage());
                    result.setValue(Result.error("Failed to retrieve profile: " + e.getMessage()));
                });

        return result;
    }


    @Override
    public LiveData<Result<String>> updateUserProfile(String newName) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            result.setValue(Result.error("User not authenticated."));
            return result;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("username", newName);

        db.collection("user").document(user.getUid()).update(updates)
                .addOnSuccessListener(aVoid -> result.setValue(Result.success("Profile updated successfully.")))
                .addOnFailureListener(e -> result.setValue(Result.error("Failed to update profile: " + e.getMessage())));

        return result;
    }

    @Override
    public LiveData<Result<String>> updateUserPassword(String oldPassword, String newPassword) {
        MutableLiveData<Result<String>> result = new MutableLiveData<>();
        result.setValue(Result.loading());

        FirebaseUser user = auth.getCurrentUser();
        if (user == null || user.getEmail() == null) {
            result.setValue(Result.error("User not authenticated."));
            return result;
        }

        user.reauthenticate(EmailAuthProvider.getCredential(user.getEmail(), oldPassword))
                .addOnSuccessListener(authResult -> {
                    user.updatePassword(newPassword)
                            .addOnSuccessListener(aVoid -> result.setValue(Result.success("Password updated successfully.")))
                            .addOnFailureListener(e -> result.setValue(Result.error("Failed to update password: " + e.getMessage())));
                })
                .addOnFailureListener(e -> result.setValue(Result.error("Old password is incorrect.")));

        return result;
    }
    @Override
    public LiveData<Result<List<User>>> getAllUsers() {
        MutableLiveData<Result<List<User>>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection("user")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> userList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        User user = document.toObject(User.class);
                        userList.add(user);
                    }
                    resultLiveData.setValue(Result.success(userList));
                })
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to fetch users: " + e.getMessage())));

        return resultLiveData;
    }

    @Override
    public LiveData<Result<String>> blockUser(String userId) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());

        db.collection("user").document(userId)
                .update("isBlocked", true)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("User blocked successfully.")))
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to block user: " + e.getMessage())));

        return resultLiveData;
    }

    @Override
    public LiveData<Result<String>> unblockUser(String userId) {
        MutableLiveData<Result<String>> resultLiveData = new MutableLiveData<>();
        resultLiveData.setValue(Result.loading());
        db.collection("user").document(userId)
                .update("isBlocked", false)
                .addOnSuccessListener(aVoid -> resultLiveData.setValue(Result.success("User unblocked successfully.")))
                .addOnFailureListener(e -> resultLiveData.setValue(Result.error("Failed to unblock user: " + e.getMessage())));

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
            return "Authentication error: " + e.getMessage();
        }
    }
}
