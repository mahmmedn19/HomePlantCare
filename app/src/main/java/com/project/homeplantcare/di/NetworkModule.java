package com.project.homeplantcare.di;

import com.google.gson.GsonBuilder;
import com.project.homeplantcare.data.repo.network.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    private static final String DEFAULT_BASE_URL = "https://edcc-34-105-42-79.ngrok-free.app/"; // ✅ Default Base URL

    @Provides
    @Singleton
    public static Retrofit provideRetrofit(GsonConverterFactory gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(DEFAULT_BASE_URL) // ✅ Keep a default base URL
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