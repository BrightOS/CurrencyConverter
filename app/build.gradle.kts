plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.navigation.safe.args)
    alias(libs.plugins.google.devtools.ksp)
    kotlin("kapt")
}

android {
    namespace = "ru.bashcony.currencyconverter"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.bashcony.currencyconverter"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xallow-result-return-type")
    }

    viewBinding {
        enable = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(libs.adapter.rxjava2)
    implementation(libs.androidsvg.aar)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.rxjava2)
    implementation(libs.androidx.room.runtime)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.glide)
    implementation(libs.hilt.android)
    implementation(libs.volley)
    kapt(libs.ksp.hilt)
    kapt(libs.ksp.room)
    kapt(libs.compiler)
    implementation(libs.rxjava)
    implementation(libs.rxkotlin)
    implementation(libs.rxandroid)
    implementation(libs.retrofit)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.bashcony.followy.ui) // My own library for some UI elements
    implementation(libs.shimmer)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}