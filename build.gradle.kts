// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version("7.3.1") apply(false)
    id("com.android.library") version("7.3.1") apply(false)
    id("org.jetbrains.kotlin.android") version("1.7.20") apply(false)

    // https://developers.google.com/maps/documentation/places/android-sdk/secrets-gradle-plugin
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version("2.0.1") apply(false)
}

buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
        classpath("com.google.gms:google-services:4.3.14")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
        classpath("com.google.firebase:perf-plugin:1.4.2")
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.7.20-1.0.8")
    }
}