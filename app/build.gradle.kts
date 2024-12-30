plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.teste"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.teste"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.activity)
    implementation(libs.appcompat)
    implementation(libs.cardview)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    implementation(libs.google.maps)
    implementation(libs.play.services.location)
    implementation(libs.okhttp)
    implementation(libs.json)

    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)

    testImplementation(libs.junit)
}