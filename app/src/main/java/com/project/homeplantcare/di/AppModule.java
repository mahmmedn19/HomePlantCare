package com.project.homeplantcare.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.homeplantcare.data.repo.app_repo.AppRepository;
import com.project.homeplantcare.data.repo.app_repo.AppRepositoryImpl;
import com.project.homeplantcare.data.repo.auth.AuthRepository;
import com.project.homeplantcare.data.repo.auth.AuthRepositoryImpl;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    public FirebaseFirestore provideFirestore() {
        return FirebaseFirestore.getInstance();
    }

    @Provides
    @Singleton
    public AuthRepository provideAuthRepository(FirebaseAuth auth, FirebaseFirestore firestore) {
        return new AuthRepositoryImpl(auth, firestore);
    }

    @Provides
    @Singleton
    public AppRepository provideAppRepository(FirebaseFirestore firestore) {
        return new AppRepositoryImpl(firestore);
    }

}
