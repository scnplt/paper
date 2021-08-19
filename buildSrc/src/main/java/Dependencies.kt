object Dependencies {
    // Classpath
    const val GRADLE = "com.android.tools.build:gradle:${Versions.GRADLE}"
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val HILT_ANDROID_GRADLE_PLUGIN =
        "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT}"
    const val GOOGLE_SERVICES = "com.google.gms:google-services:${Versions.GOOGLE_SERVICES}"
    const val NAVIGATION_SAFE_ARGS =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}"

    // Implementation
    const val CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIVEDATA_KTX}"
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    const val LOTTIE = "com.airbnb.android:lottie:${Versions.LOTTIE}"
    const val HILT_ANDROID = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
    const val COROUTINES_PLAY_SERVICES =
        "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.COROUTINES}"
    const val FIREBASE_AUTH = "com.google.firebase:firebase-auth-ktx:${Versions.FIREBASE_AUTH}"
    const val FIREBASE_FIRESTORE =
        "com.google.firebase:firebase-firestore-ktx:${Versions.FIREBASE_FIRESTORE}"
    const val NAVIGATION_FRAGMENT =
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"

    // Kapt
    const val HILT_COMPILER = "com.google.dagger:hilt-compiler:${Versions.HILT}"

    // Platform
    const val FIREBASE_BOM = "com.google.firebase:firebase-bom:${Versions.FIREBASE_BOM}"

    // Debug Implementation
    const val LEAKCANARY = "com.squareup.leakcanary:leakcanary-android:${Versions.LEAKCANARY}"

    // Test Implementation
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
    const val TRUTH = "com.google.truth:truth:${Versions.TRUTH}"

    // Android Test Implementation
    const val EXT_JUNIT = "androidx.test.ext:junit:${Versions.EXT_JUNIT}"
    const val ESPRESSO = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
}
