package com.project.homeplantcare.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class InputValidator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    // Chars only and 4-20 characters long
    private static final String USERNAME_REGEX = "^[a-zA-Z]+$"; // Only letters allowed, no numbers or special characters
    private static final Map<EditText, TextWatcher> textWatcherMap = new HashMap<>();

    public static boolean validateData(TextInputLayout textInputLayout, String text) {
        if (isEmpty(text)) {
            setError(textInputLayout, "This field cannot be empty");
            return false;
        } else if (text.length() < 4) {
            setError(textInputLayout, "At least 4 char");
            return false;
        }
        clearError(textInputLayout);
        return true;
    }

    public static boolean validateField(TextInputLayout textInputLayout, String text, String errorMessage) {
        if (isEmpty(text)) {
            setError(textInputLayout, errorMessage);
            return false;
        }

        if (text.length() < 4) {
            setError(textInputLayout, "At least 4 characters required");
            return false;
        }

        // Remove hidden characters and trim spaces
        text = text.replaceAll("[^\\p{Print}]", "").trim();

        // Must contain at least one English letter
        if (!text.matches(".*[a-zA-Z].*")) {
            setError(textInputLayout, "Must contain at least one English letter");
            return false;
        }

        // Must NOT contain any Arabic letters
        if (text.matches(".*[\\u0600-\\u06FF].*")) {
            setError(textInputLayout, "Arabic letters are not allowed");
            return false;
        }

        // Must NOT contain any digits
        if (text.matches(".*\\d.*")) {
            setError(textInputLayout, "Digits are not allowed");
            return false;
        }

        clearError(textInputLayout);
        return true;
    }

    public static boolean validateFieldWithDigitsAndSpecialChars_EnglishOnly(TextInputLayout textInputLayout, String text, String errorMessage) {
        if (isEmpty(text)) {
            setError(textInputLayout, errorMessage);
            return false;
        }

        if (text.length() < 4) {
            setError(textInputLayout, "At least 4 characters required");
            return false;
        }

        // Remove hidden characters and trim spaces
        text = text.replaceAll("[^\\p{Print}]", "").trim();

        // Must contain at least one English letter
        if (!text.matches(".*[a-zA-Z].*")) {
            setError(textInputLayout, "Must contain at least one English letter");
            return false;
        }

        // Must NOT contain any Arabic letters
        if (text.matches(".*[\\u0600-\\u06FF].*")) {
            setError(textInputLayout, "Arabic letters are not allowed");
            return false;
        }

        clearError(textInputLayout);
        return true;
    }




    public static boolean validateEmail(TextInputLayout emailTextInputLayout, String email) {
        if (isEmpty(email)) {
            setError(emailTextInputLayout, "Email field cannot be empty");
            return false;
        } else if (!isValidEmailFormat(email)) {
            setError(emailTextInputLayout, "Invalid email address");
            return false;
        }
        clearError(emailTextInputLayout);
        return true;
    }

    public static boolean validatePassword(TextInputLayout passwordTextInputLayout, String password) {
        if (isEmpty(password)) {
            setError(passwordTextInputLayout, "Password field cannot be empty");
            return false;
        } else if (password.length() < 8) {
            setError(passwordTextInputLayout, "Password must be at least 8 characters long");
            return false;
        } else if (!isValidPasswordFormat(password)) {
            setError(passwordTextInputLayout, "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character");
            return false;
        }
        clearError(passwordTextInputLayout);
        return true;
    }

    public static boolean validateConfirmPassword(TextInputLayout confirmPasswordTextInputLayout, String password, String confirmPassword) {
        if (isEmpty(confirmPassword)) {
            setError(confirmPasswordTextInputLayout, "Confirm password field cannot be empty");
            return false;
        } else if (!password.equals(confirmPassword)) {
            setError(confirmPasswordTextInputLayout, "Passwords do not match");
            return false;
        }
        clearError(confirmPasswordTextInputLayout);
        return true;
    }

    public static boolean validateUsername(TextInputLayout nameTextInputLayout, String name) {
        if (isEmpty(name)) {
            setError(nameTextInputLayout, "Name field cannot be empty");
            return false;
        } else if (name.length() < 4 || name.length() > 20) {
            setError(nameTextInputLayout, "Name must be between 4 and 20 letters");
            return false;
        } else if (!isValidUsernameFormat(name)) {
            setError(nameTextInputLayout, "Name can only contain letters");
            return false;
        }
        clearError(nameTextInputLayout);
        return true;
    }


    private static boolean isEmpty(String input) {
        return TextUtils.isEmpty(input);
    }

    private static boolean isValidEmailFormat(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    private static boolean isValidPasswordFormat(String password) {
        return Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
    }

    private static boolean isValidUsernameFormat(String name) {
        return Pattern.compile(USERNAME_REGEX).matcher(name).matches();
    }

    private static void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(errorMessage);
    }

    private static void clearError(TextInputLayout textInputLayout) {
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
    }

    public static void clearErrorOnTextChange(TextInputLayout textInputLayout) {
        if (textInputLayout == null || textInputLayout.getEditText() == null) {
            return; // Prevent NullPointerException
        }

        EditText editText = textInputLayout.getEditText();

        // Remove any existing TextWatcher to prevent duplicate listeners
        removeExistingTextWatcher(editText);

        // Create a new TextWatcher
        TextWatcher textWatcher = new TextWatcher() {
            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousText = s.toString(); // Store previous text value before modification
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearError(textInputLayout); // Clear error dynamically when typing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Ensure the field is not left empty
                if (editable.toString().trim().isEmpty()) {
                    setError(textInputLayout, "This field cannot be empty");
                }
            }
        };

        // Add the TextWatcher to the EditText
        editText.addTextChangedListener(textWatcher);

        // Store the TextWatcher reference
        textWatcherMap.put(editText, textWatcher);
    }


    private static void removeExistingTextWatcher(EditText editText) {
        // Check if a TextWatcher is already registered for this EditText
        if (textWatcherMap.containsKey(editText)) {
            editText.removeTextChangedListener(textWatcherMap.get(editText));
            textWatcherMap.remove(editText); // Remove reference after unbinding
        }
    }
}