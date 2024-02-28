import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

object Deps {

    // Android
    const val kotlinCore = "androidx.core:core-ktx:${Versions.kotlinCoreVersion}"
    const val lifecycleRuntime =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntimeVersion}"
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activityComposeVersion}"


    // Compose
    const val composeBom = "androidx.compose:compose-bom:${Versions.composeBomVersion}"
    const val composeUi = "androidx.compose.ui:ui"
    const val composeUiGraphics = "androidx.compose.ui:ui-graphics"
    const val composeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const  val composeMaterial3 = "androidx.compose.material3:material3"
    const val composeRuntime = "androidx.compose.runtime:runtime"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling"


    // Test Dependencies
    const val jUnit = "junit:junit:${Versions.jUnitVersion}"
    const val jUnitTestExt = "androidx.test.ext:junit:${Versions.jUnitExtVersion}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoVersion}"
    const val composeUiTestJUnit4 = "androidx.compose.ui:ui-test-junit4-android:${Versions.composeJUnit4}"
    const val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest:${Versions.composeJUnit4}"

    // Hilt
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hiltAgp = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"

    // Room
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"

    // OkHttp
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"

    // Retrofit
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
}

fun DependencyHandler.kotlinRuntimeCore() {
    implementation(Deps.kotlinCore)
    implementation(Deps.activityCompose)
    implementation(Deps.lifecycleRuntime)
    implementation(platform(Deps.composeBom))
}

fun DependencyHandler.room() {
    implementation(Deps.roomRuntime)
    implementation(Deps.roomKtx)
    ksp(Deps.roomCompiler)
}

fun DependencyHandler.jUnit() {
    testImplementation(Deps.jUnit)
    androidTestImplementation(Deps.jUnitTestExt)
    androidTestImplementation(Deps.espressoCore)
    androidTestImplementation(Deps.composeUiTestJUnit4)
    debugImplementation(Deps.composeUiTooling)
    debugImplementation(Deps.composeUiTestManifest)
}

fun DependencyHandler.retrofit() {
    implementation(Deps.retrofit)
    implementation(Deps.moshiConverter)
    implementation(Deps.okHttp)
    implementation(Deps.okHttpLoggingInterceptor)
}

fun DependencyHandler.compose() {
    implementation(Deps.composeUi)
    implementation(Deps.composeUiGraphics)
    implementation(Deps.composeRuntime)
    implementation(Deps.composeMaterial3)
    implementation(Deps.composeUiToolingPreview)
}

fun DependencyHandler.hilt() {
    implementation(Deps.hiltAndroid)
    ksp(Deps.hiltCompiler)
}