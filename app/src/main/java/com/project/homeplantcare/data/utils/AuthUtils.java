package com.project.homeplantcare.data.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthUtils {

    /**
     * Get the current logged-in user ID.
     *
     * @return The user ID if logged in, otherwise null.
     */
    public static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    /**
     * Check if a user is logged in.
     *
     * @return True if user is logged in, otherwise false.
     */
    public static boolean isUserLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}