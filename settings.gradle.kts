pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            val composeVersion = "1.2.1"
            val hiltVersion = "2.44"

            library("androidx-core", "androidx.core:core-ktx:1.9.0")
            library("androidx-lifecycle-runtime", "androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
            library("androidx-activity-compose", "androidx.activity:activity-compose:1.6.0")
            library("androidx-compose-ui", "androidx.compose.ui:ui:$composeVersion")
            library("androidx-compose-material3", "androidx.compose.material3:material3:1.0.0-rc01")
            library("androidx-lifecycle-viewmodel-compose", "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
            library("androidx-lifecycle-runtime-compose", "androidx.lifecycle:lifecycle-runtime-compose:2.6.0-alpha02")
            library("androidx-navigation-compose", "androidx.navigation:navigation-compose:2.5.2")
            library("androidx-constraintlayout-compose", "androidx.constraintlayout:constraintlayout-compose:1.0.1")

            // Firebase
            library("firebase", "com.google.firebase:firebase-bom:31.0.1")

            // Compose permission
            library("accompanist-permissions", "com.google.accompanist:accompanist-permissions:0.23.1")

            // Chrome custom tab
            library("androidx-browser", "androidx.browser:browser:1.4.0")

            library("junit4", "junit:junit:4.13.2")
            library("kotlinx-coroutines-test", "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
            library("truth", "com.google.truth:truth:1.1.2")
            library("androidx-compose-ui-test-junit4", "androidx.compose.ui:ui-test-junit4:$composeVersion")
            library("hilt-android-testing", "com.google.dagger:hilt-android-testing:$hiltVersion")
            library("robolectric", "org.robolectric:robolectric:4.9")
            library("androidx-test-core", "androidx.test:core:1.5.0-beta01")
            library("androidx-test-ext-junit", "androidx.test.ext:junit:1.1.3")
            library("androidx-test-espresso-core", "androidx.test.espresso:espresso-core:3.4.0")
            library("androidx-navigation-testing", "androidx.navigation:navigation-testing:2.5.2")
            library("androidx-compose-ui-tooling", "androidx.compose.ui:ui-tooling:$composeVersion")
            library("androidx-compose-ui-test-manifest", "androidx.compose.ui:ui-test-manifest:$composeVersion")

            // hilt
            library("hilt-android", "com.google.dagger:hilt-android:$hiltVersion")
            library("hilt-navigation-compose", "androidx.hilt:hilt-navigation-compose:1.0.0")
            library("hilt-android-compiler", "com.google.dagger:hilt-android-compiler:$hiltVersion")

            // Map
            library("maps-compose", "com.google.maps.android:maps-compose:2.7.2")
            library("play-services-maps", "com.google.android.gms:play-services-maps:18.1.0")
            library("android-maps-utils", "com.google.maps.android:android-maps-utils:2.3.0")

            // retrofit
            val retrofitVersion = "2.9.0"
            library("retrofit", "com.squareup.retrofit2:retrofit:$retrofitVersion")
            library("retrofit-converter-gson", "com.squareup.retrofit2:converter-gson:$retrofitVersion")
        }
    }
}
rootProject.name = "Ken Apps"
include(":app")
