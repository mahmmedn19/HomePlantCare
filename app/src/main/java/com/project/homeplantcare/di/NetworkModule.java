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

    private static Retrofit retrofitInstance;

    @Provides
    @Singleton
    public static Retrofit provideRetrofit(@ApplicationContext Context context, GsonConverterFactory gson, OkHttpClient okHttpClient) {
        String baseUrl = SharedPrefUtils.getAiLink(context);
        return getRetrofitInstance(baseUrl, gson, okHttpClient);
    }
    public static synchronized void refreshRetrofitInstance(String newBaseUrl) {
        retrofitInstance = new Retrofit.Builder()
                .baseUrl(newBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build();
    }

    private static synchronized Retrofit getRetrofitInstance(String baseUrl, GsonConverterFactory gson, OkHttpClient okHttpClient) {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(gson)
                    .client(okHttpClient)
                    .build();
        }
        return retrofitInstance;
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
