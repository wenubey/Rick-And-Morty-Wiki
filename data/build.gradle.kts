plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlinx-serialization")
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

android {
    namespace = "com.wenubey.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    //Ktor
    implementation(libs.ktor.core)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.contentnegotiation)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.logging)

    implementation(project(":domain"))

    // KSP
    implementation(libs.ksp)

    // Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    // DataStore Preferences
    implementation(libs.dataStore)

    // Paging Compose
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // Room
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.paging)
    implementation(libs.room.ktx)

    // Exoplayer
    implementation(libs.media3.ui)
    implementation(libs.media3.exoplayer)

    // Unit Test
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.ktor.client.mock)
    testImplementation(libs.room.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation (libs.androidx.espresso.core)
    androidTestImplementation (libs.truth)
    androidTestImplementation(libs.core.testing)

    // Timber
    implementation(libs.timber)

    testRuntimeOnly(libs.logback.classic)
}