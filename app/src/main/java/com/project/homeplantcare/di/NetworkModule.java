package com.project.homeplantcare.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.homeplantcare.data.repo.network.ApiService;

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
    //Base URL
    private static final String BASE_URL = "https://8605-34-145-17-51.ngrok-free.app";

    @Provides
    public static Retrofit provideRetrofit(GsonConverterFactory gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gson)
                .client(okHttpClient)
                .build();
    }

    @Provides
    public static   GsonConverterFactory provideGson() {
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
    // provide your API service
    @Provides
    public static ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}