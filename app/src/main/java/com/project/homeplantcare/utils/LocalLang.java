package com.project.homeplantcare.utils;

import android.content.Context;
import android.content.res.Configuration;

import java.util.Locale;

public class LocalLang {
    public static void setLocale(String lang, Context context) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}