package com.project.homeplantcare.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {
    private static final String PREF_NAME = "shared_prefs";
    private static final String KEY_AI_LINK = "ai_link";

    // ✅ Save AI link
    public static void saveAiLink(Context context, String aiLink) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_AI_LINK, aiLink);
        editor.apply();
    }

    // ✅ Get AI link (return default if not found)
    public static String getAiLink(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_AI_LINK, "https://17cd-34-19-92-7.ngrok-free.app/"); // ✅ Default fallback
    }
}
