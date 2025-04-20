package com.project.homeplantcare.di;


import com.google.gson.GsonBuilder;
import com.project.homeplantcare.data.repo.network.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    private static final String baseUrl = "https://plantcareapi.loca.lt"; // Default base URL


    @Provides
    public static GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create(new GsonBuilder().create());
    }

    @Provides
    public static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    public static OkHttpClient provideOkHttpClient(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    HttpUrl dynamicBaseUrl = HttpUrl.parse(baseUrl);

                    if (dynamicBaseUrl == null) {
                        throw new IllegalArgumentException("Invalid base URL: " + baseUrl);
                    }

                    HttpUrl updatedUrl = originalRequest.url().newBuilder()
                            .scheme(dynamicBaseUrl.scheme())
                            .host(dynamicBaseUrl.host())
                            .port(dynamicBaseUrl.port())
                            .build();

                    Request newRequest = originalRequest.newBuilder().url(updatedUrl).build();
                    return chain.proceed(newRequest);
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    @Singleton
    public static Retrofit provideRetrofit(GsonConverterFactory gsonFactory, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(gsonFactory)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    public static ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}