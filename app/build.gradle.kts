plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}



android {
    namespace = "com.saveetha.smarthealthcareapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.saveetha.smarthealthcareapp"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
//
//dependencies {
//
//    implementation(libs.appcompat)
//    implementation(libs.material)
//    implementation ("com.google.android.material:material:1.11.0")
//    implementation(libs.activity)
//    implementation(libs.constraintlayout)
//    implementation(libs.core.ktx)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.ext.junit)
//    androidTestImplementation(libs.espresso.core)
//    implementation("com.squareup.okhttp3:okhttp:4.11.0")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
//    implementation ("com.android.volley:volley:1.2.1")
//    implementation ("com.google.android.material:material:1.6.0")
//    implementation ("com.android.volley:volley:1.2.1")
//    implementation("com.squareup.okhttp3:okhttp:4.9.3")
//    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
//    implementation ("androidx.cardview:cardview:1.0.0")
//    implementation ("androidx.core:core-ktx:1.7.0")
//dependencies {
//
//    // Core AndroidX libraries
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("androidx.core:core-ktx:1.12.0") // Updated from 1.7.0
//    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation("androidx.cardview:cardview:1.0.0")
//    implementation ("com.google.android.gms:play-services-location:21.0.1")
//    implementation ("com.google.android.gms:play-services-location:21.0.1")
//
//    implementation ("com.google.android.gms:play-services-maps:18.2.0")
//    implementation ("com.google.maps.android:android-maps-utils:2.3.0")
//
//
//
//
//    // Material Design (only ONE version, use latest stable)
//    implementation("com.google.android.material:material:1.11.0") // ✅ This supports cardStrokeColor
//
//    // Networking libraries
//    implementation("com.android.volley:volley:1.2.1") // Only once
//    implementation("com.squareup.okhttp3:okhttp:4.11.0")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//    implementation ("com.airbnb.android:lottie:6.1.0")
//
//    // Kotlin + Activity
//    implementation("androidx.activity:activity-ktx:1.8.2")
//    implementation(libs.activity)
//    implementation(libs.contentpager)
//    implementation(libs.lifecycle.livedata.ktx)
//    implementation(libs.lifecycle.viewmodel.ktx)
//    implementation(libs.fragment.ktx)
//    implementation(libs.firebase.messaging.ktx)
//
//    // Testing
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//
//
//    // Glide for image loading
//    implementation("com.github.bumptech.glide:glide:4.16.0")
//    kapt("com.github.bumptech.glide:compiler:4.16.0")
//
//    implementation ("com.google.android.gms:play-services-maps:18.2.0")
//    implementation ("com.google.android.libraries.places:places:3.4.0")
//    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
//    // Retrofit
//    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
//    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
//    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
//
//    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
//    implementation("com.google.firebase:firebase-messaging-ktx")
//    implementation("com.google.firebase:firebase-analytics-ktx")
//
//
//
//}









dependencies {

    // Core AndroidX libraries
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")

    // Google Play Services & Maps
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:android-maps-utils:2.3.0")
    implementation("com.google.android.libraries.places:places:3.4.0")

    // Material Design
    implementation("com.google.android.material:material:1.11.0") // ✅ latest

    // Networking libraries
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Lottie Animations
    implementation("com.airbnb.android:lottie:6.1.0")

    // Kotlin + Jetpack
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation(libs.activity)
    implementation(libs.contentpager)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.fragment.ktx)

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Glide (Image Loading)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.firebase.messaging)
    implementation(libs.google.material)
    implementation(libs.generativeai)
    kapt("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

}


