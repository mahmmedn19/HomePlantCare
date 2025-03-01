package com.project.homeplantcare.di;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.project.homeplantcare.data.repo.network.ApiService;
import com.project.homeplantcare.utils.SharedPrefUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public static Retrofit provideRetrofit(@ApplicationContext Context context, GsonConverterFactory gson, OkHttpClient okHttpClient) {
        String baseUrl = SharedPrefUtils.getAiLink(context); // ✅ Get AI Link from SharedPreferences

        return new Retrofit.Builder()
                .baseUrl(baseUrl) // ✅ Use AI Link as Base URL
                .addConverterFactory(gson)
                .client(okHttpClient)
                .build();
    }

    @Provides
    public static GsonConverterFactory provideGson() {
        return GsonConverterFactory.create(new GsonBuilder().create());
    }

    @Provides
    public static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    public static OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public static ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
