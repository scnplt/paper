buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Dependencies.GRADLE)
        classpath(Dependencies.KOTLIN_GRADLE_PLUGIN)

        // Hilt
        classpath(Dependencies.HILT_ANDROID_GRADLE_PLUGIN)

        // Firebase
        classpath(Dependencies.GOOGLE_SERVICES)

        // Navigation
        classpath(Dependencies.NAVIGATION_SAFE_ARGS)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}
