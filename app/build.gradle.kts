plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt") // Explicitly apply the kapt plugin
    id("kotlin-parcelize")
}

android {
    namespace = "com.moviez"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.moviez"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }



    // Enable Kapt debug logging
    kapt {
        useBuildCache = true // Enable build cache for Kapt
        correctErrorTypes = true // Resolve type errors during annotation processing
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas".toString()) // Room schema location
            arg("room.incremental", "true") // Enable incremental processing for Room
        }
    }
}

dependencies {
    // Core dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Networking dependencies
    implementation(libs.androidx.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.androidx.interceptor)

    // LiveData dependencies
    implementation(libs.androidx.liveData)

    // Glide for image loading
    implementation(libs.androidx.glide)

    // Fragment KTX
    implementation(libs.androidx.ktx)

    // Room dependencies
    implementation(libs.androidx.room.runtime) // Room runtime
    implementation(libs.androidx.room.ktx)     // Room Kotlin extensions
    kapt(libs.androidx.room.compiler)          // Room annotation processor
    implementation(libs.kotlinx.metadata.jvm)

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}