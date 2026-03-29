plugins {
    id("com.android.test")
    id("kotlin-android")
}

android {
    namespace = "us.nineworlds.serenity.baselineprofile"
    compileSdk = 35

    defaultConfig {
        minSdk = 25
        targetSdk = 35
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Increase the delay before killing the process to allow profiles to flush
        testInstrumentationRunnerArguments["androidx.benchmark.killProcessDelayMillis"] = "30000"

        // Specifically for the 'Waiting for app processes to flush' error:
        // This gives the 'pm dump-profiles' command more time to return success
        testInstrumentationRunnerArguments["androidx.benchmark.compilation.waitTimeout"] = "60000"

    }

    targetProjectPath = ":serenity-app"
    experimentalProperties["android.experimental.self-instrumenting"] = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    implementation("androidx.benchmark:benchmark-macro-junit4:1.4.1")
    implementation("androidx.test.uiautomator:uiautomator:2.3.0")
    implementation("androidx.test.ext:junit:1.3.0")
    implementation("androidx.test:rules:1.7.0")
    implementation("androidx.test:runner:1.7.0")
    implementation(project(":serenity-app"))
    
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
}
