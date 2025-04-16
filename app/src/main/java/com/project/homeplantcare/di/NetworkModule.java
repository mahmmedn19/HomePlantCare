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
    public static String BASE_URL = "https://default-url.com"; // ✅ Default base URL (Change this as needed)

    @Provides
    @Singleton
    public static Retrofit provideRetrofit(GsonConverterFactory gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL) // ✅ Uses dynamically updated BASE_URL
                .addConverterFactory(gson)
                .client(okHttpClient)
                .build();
    }
    public static synchronized void refreshRetrofitInstance(String newBaseUrl) {
        if (newBaseUrl != null && !newBaseUrl.isEmpty() && !newBaseUrl.equals(BASE_URL)) {
            BASE_URL = newBaseUrl; // ✅ Update static base URL
        }
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
                .addInterceptor(chain -> {
                    okhttp3.Request originalRequest = chain.request();

                    // ✅ Parse base URL correctly
                    okhttp3.HttpUrl baseHttpUrl = okhttp3.HttpUrl.parse(BASE_URL);
                    if (baseHttpUrl == null) {
                        throw new IllegalArgumentException("Invalid Base URL: " + BASE_URL);
                    }

                    // ✅ Build a new request with the correct host
                    okhttp3.HttpUrl newUrl = originalRequest.url().newBuilder()
                            .scheme(baseHttpUrl.scheme()) // ✅ Keep the same scheme (http/https)
                            .host(baseHttpUrl.host()) // ✅ Set correct host dynamically
                            .build();

                    okhttp3.Request newRequest = originalRequest.newBuilder()
                            .url(newUrl)
                            .build();

                    return chain.proceed(newRequest);
                })
                .build();
    }

    @Provides
    @Singleton
    public static ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
