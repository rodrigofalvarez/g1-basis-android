plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "io.texne.g1.subtitles"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.texne.g1.subtitles"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    kapt {
        correctErrorTypes = true
    }
    hilt {
        enableAggregatingTask = false
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
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
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.material)
    implementation(libs.androidx.material3)
    implementation(project(":client"))
    implementation(project(":service"))

    kapt(libs.hilt.android.compiler)
}