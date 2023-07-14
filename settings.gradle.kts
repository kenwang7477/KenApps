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
            val hiltVersion = "2.46.1"

            library("androidx-core", "androidx.core:core-ktx:1.9.0")
            library("androidx-lifecycle-runtime", "androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
            library("androidx-activity-compose", "androidx.activity:activity-compose:1.7.2")
            library("androidx-lifecycle-viewmodel-compose", "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
            library("androidx-lifecycle-runtime-compose", "androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
            library("androidx-navigation-compose", "androidx.navigation:navigation-compose:2.5.2")
            library("androidx-constraintlayout-compose", "androidx.constraintlayout:constraintlayout-compose:1.0.1")
            library("kotlinx-collections-immutable", "org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
            library("androidx-datastore", "androidx.datastore:datastore-preferences:1.0.0")
            library("io-coil-compose", "io.coil-kt:coil-compose:2.4.0")
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

            // Compose permission
            library("accompanist-permissions", "com.google.accompanist:accompanist-permissions:0.23.1")

            // Chrome custom tab
            library("androidx-browser", "androidx.browser:browser:1.4.0")

            // Room
            val roomVersion = "2.5.1"
            library("androidx-room-runtime", "androidx.room:room-runtime:$roomVersion")
            library("androidx-room-compiler", "androidx.room:room-compiler:$roomVersion")
            library("androidx-room-ktx", "androidx.room:room-ktx:$roomVersion")

            library("mockk", "io.mockk:mockk:1.13.5")
            library("kotlinx-coroutines-test", "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
            library("truth", "com.google.truth:truth:1.1.4")
            library("hilt-android-testing", "com.google.dagger:hilt-android-testing:$hiltVersion")
            library("robolectric", "org.robolectric:robolectric:4.9")
            library("androidx-test-core", "androidx.test:core:1.5.0")
            library("androidx-test-ext-junit", "androidx.test.ext:junit:1.1.5")
            library("androidx-test-espresso-core", "androidx.test.espresso:espresso-core:3.4.0")
            library("androidx-navigation-testing", "androidx.navigation:navigation-testing:2.5.2")

            // hilt
            library("hilt-android", "com.google.dagger:hilt-android:$hiltVersion")
            library("hilt-navigation-compose", "androidx.hilt:hilt-navigation-compose:1.0.0")
            library("hilt-android-compiler", "com.google.dagger:hilt-android-compiler:$hiltVersion")

            // Map
            library("maps-compose", "com.google.maps.android:maps-compose:2.11.4")
            library("play-services-maps", "com.google.android.gms:play-services-maps:18.1.0")
            library("android-maps-utils", "com.google.maps.android:android-maps-utils:2.3.0")

            // retrofit
            val retrofitVersion = "2.9.0"
            library("retrofit", "com.squareup.retrofit2:retrofit:$retrofitVersion")
            library("retrofit-converter-gson", "com.squareup.retrofit2:converter-gson:$retrofitVersion")
            library("retrofit2-kotlinx-serialization-converter", "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

            // ktor
            val ktorVersion = "2.3.1"
            library("io-ktor-client-core", "io.ktor:ktor-client-core:$ktorVersion")
            library("io-ktor-client-android", "io.ktor:ktor-client-android:$ktorVersion")
            library("io-ktor-client-serialization", "io.ktor:ktor-client-serialization:$ktorVersion")
            library("io-ktor-client-logging", "io.ktor:ktor-client-logging:$ktorVersion")
            library("io-ktor-client-content-negotiation", "io.ktor:ktor-client-content-negotiation:$ktorVersion")
        }
    }
}
rootProject.name = "Ken Apps"
include(":app")
