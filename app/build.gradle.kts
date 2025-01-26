plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.firebaseCrashlytics)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.googleServices)
}
android {
    namespace = "com.project.homeplantcare"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.project.homeplantcare"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    implementation(libs.firebaseBom)
    implementation(libs.firebaseAuth)
    implementation(libs.firebaseFirestore)
    implementation(libs.firebaseCrashlytics)
    implementation(libs.firebaseStorage)

    implementation(libs.navigationFragment)
    implementation(libs.navigationUi)

    implementation(libs.lifecycleRuntime)
    implementation(libs.lifecycleViewmodel)
    implementation(libs.lifecycleLivedata)

    implementation(libs.glide)
    implementation(libs.lottie)
    implementation(libs.imagepicker)
    implementation(libs.imageslideshow)

    implementation(libs.hiltAndroid)
    implementation(libs.androidxActivity)
    kapt(libs.hiltCompiler)

    implementation(libs.retrofit)
    implementation(libs.retrofitGson)
    implementation(libs.retrofitRxjava3)
    implementation(libs.okhttpLogging)

    implementation(libs.kotlinStdlib)
    implementation(libs.rxandroid)

    implementation(libs.datastorePreferences)
    implementation(libs.datastorePreferencesCore)

    testImplementation(libs.junit)
    androidTestImplementation(libs.testextjunit)
    androidTestImplementation(libs.espresso)

    implementation(libs.fragment)
    implementation(libs.activity)

    implementation (libs.onboarder)

}
hilt {
    enableAggregatingTask = true
}