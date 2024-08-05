import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.wenubey.rickandmortywiki"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wenubey.rickandmortywiki"
        minSdk = 26
        targetSdk = 34
        versionCode = 8
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isShrinkResources = true
            isMinifyEnabled = true
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
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    subprojects {
        tasks.withType<Test> {
            testLogging {
                events(
                    TestLogEvent.FAILED,
                    TestLogEvent.PASSED,
                    TestLogEvent.SKIPPED,
                    TestLogEvent.STANDARD_OUT,
                )
                exceptionFormat = TestExceptionFormat.FULL
                showExceptions = true
                showCauses = true
                showStackTraces = true
                debug {
                    events(
                        TestLogEvent.STARTED,
                        TestLogEvent.FAILED,
                        TestLogEvent.PASSED,
                        TestLogEvent.SKIPPED,
                        TestLogEvent.STANDARD_ERROR,
                        TestLogEvent.STANDARD_OUT
                    )
                    exceptionFormat = TestExceptionFormat.FULL
                }
                info.events = debug.events
                info.exceptionFormat = debug.exceptionFormat
            }
            afterSuite(KotlinClosure2<TestDescriptor, TestResult, Unit>({ desc, result ->
                if (desc.parent == null) {
                    val output = "Results: ${result.resultType} (${result.testCount} tests, " +
                            "${result.successfulTestCount} passed, ${result.failedTestCount} failed, " +
                            "${result.skippedTestCount} skipped)"
                    val startItem = "|  "
                    val endItem = "  |"
                    val repeatLength = startItem.length + output.length + endItem.length
                    println(
                        "\n" + "-".repeat(repeatLength) + "\n$startItem$output$endItem\n" + "-".repeat(
                            repeatLength
                        )
                    )
                }
            }))
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(project(":domain"))
    implementation(project(":data"))

    // KSP
    implementation(libs.ksp)

    // Hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigationCompose)

    // Coil
    implementation(libs.coil)

    //Ktor
    implementation(libs.ktor.core)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.contentnegotiation)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.logging)

    // DataStore Preferences
    implementation(libs.dataStore)

    // Navigation
    implementation(libs.navigation.compose)

    // Icons Extended
    implementation(libs.androidx.material.icons)

    // ViewModel
    implementation(libs.androidx.viewmodel.compose)

    // Paging Compose
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // Room
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.paging)
    implementation(libs.room.ktx)

    // Custom Tabs
    implementation(libs.browser)

    // Exoplayer
    implementation(libs.media3.ui)
    implementation(libs.media3.exoplayer)

    // Glance Widget
    implementation(libs.glance.material3)
    implementation(libs.glance.appwidget)

    // Timber
    implementation(libs.timber)

}