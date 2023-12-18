plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.gms)
    alias(libs.plugins.android.secrets.plugin)
    id("kotlin-parcelize")
}

android {
    namespace = "com.kenwang.kenapps"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kenwang.kenapps"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.kenwang.kenapps.runner.CustomTestRunner"
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        resourceConfigurations.addAll(listOf("en", "zh-rTW"))

        /**
         * To fix
         * Schema export directory is not provided to the annotation processor so we cannot export the schema. You can either provide `room.schemaLocation` annotation processor argument OR set exportSchema to false.
         * issue
         * reference: https://blog.csdn.net/qq_41886129/article/details/124111081
         */
        ksp {
            arg("room.schemaKenApps", "$projectDir/schemas")
        }
    }

    externalNativeBuild {
        ndkVersion = "25.2.9519653"
        cmake {
            path(file("src/main/cpp/CMakeLists.txt"))
            version = "3.22.1"
        }
    }
    buildTypes {
        release {
            buildFeatures.buildConfig = true
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
    packaging {
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }

        // https://developer.android.com/studio/test/gradle-managed-devices?hl=zh-tw
        managedDevices {
            devices {
                val pixel5api33 by creating(com.android.build.api.dsl.ManagedVirtualDevice::class) {
                    // Use device profiles you typically see in Android Studio.
                    device = "Pixel 5"
                    // Use only API levels 33 and higher.
                    apiLevel = 33
                    // To include Google services, use "google".
                    systemImageSource = "google"
                }
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.datastore)
    implementation(libs.io.coil.compose)
    implementation(libs.kotlinx.serialization.json)

    // Compose
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    androidTestImplementation(composeBom)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.compose.ui.test.junit4)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.perf)
    implementation(libs.firebase.crashlytics)

    // Compose permission
    implementation(libs.accompanist.permissions)

    // Chrome custom tab
    implementation(libs.androidx.browser)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.navigation.testing)

    // hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    testImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.hilt.android.testing)
    ksp(libs.hilt.android.compiler)
    kspTest(libs.hilt.android.compiler)
    kspAndroidTest(libs.hilt.android.compiler)

    // Map
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose.utils)

    // ktor
    implementation(libs.io.ktor.client.core)
    implementation(libs.io.ktor.client.android)
    implementation(libs.io.ktor.client.serialization)
    implementation(libs.io.ktor.client.okhttp)
    implementation(libs.io.ktor.client.logging)
    implementation(libs.io.ktor.client.auth)
    implementation(libs.io.ktor.client.content.negotiation)
    implementation(libs.io.ktor.serialization.ktolin.json)
}
