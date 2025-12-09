plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    id("kotlin-kapt")
}

android {
    namespace = "ru.kulikov.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Firebase
    api(platform(libs.firebase.bom))
    api(libs.firebase.auth)
    api(libs.firebase.database)

    // RKTools
    api(libs.rktools)
    api(libs.rktools.processor)

    // Dagger2
    api(libs.dagger)
    kapt(libs.dagger.compiler)

    // Mockk
    api(libs.mockk)

    // Kotest
    api(libs.kotest.assertions.core)
}