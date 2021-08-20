plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = Configs.COMPILE_SDK
    buildToolsVersion = Configs.BUILD_TOOLS_VERSION

    defaultConfig {
        applicationId = "dev.sertan.android.paper"
        minSdk = Configs.MIN_SDK
        targetSdk = Configs.TARGET_SDK
        versionCode = Configs.VERSION_CODE
        versionName = Configs.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // AndroidX
    implementation(Dependencies.CORE_KTX)
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.LIVEDATA_KTX)

    // UI
    implementation(Dependencies.MATERIAL)
    implementation(Dependencies.LOTTIE)

    // Hilt
    kapt(Dependencies.HILT_COMPILER)
    implementation(Dependencies.HILT_ANDROID)

    // Coroutine
    implementation(Dependencies.COROUTINES_ANDROID)
    implementation(Dependencies.COROUTINES_PLAY_SERVICES)

    // Firebase
    implementation(platform(Dependencies.FIREBASE_BOM))
    implementation(Dependencies.FIREBASE_AUTH)
    implementation(Dependencies.FIREBASE_FIRESTORE)

    // Navigation
    implementation(Dependencies.NAVIGATION_FRAGMENT)
    implementation(Dependencies.NAVIGATION_UI)

    // LeakCanary
    debugImplementation(Dependencies.LEAKCANARY)

    // Test
    testImplementation(Dependencies.JUNIT)
    testImplementation(Dependencies.TRUTH)
}
