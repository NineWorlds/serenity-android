import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "us.nineworlds.serenity.emby"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        minSdkVersion(libs.versions.minSdkVersion.get())
        targetSdkVersion(libs.versions.targetSdkVersion.get())
    }

    compileSdk =
        libs.versions.targetSdkVersion
            .get()
            .toInt()
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin", "src/main/java")
        getByName("test").java.srcDirs("src/test/kotlin")
        getByName("test").resources.srcDirs("src/test/resources")
    }

    val testMode = "MOCK"
    val serverUrl = "http://localhost:8096/"
    val testUser = "test"
    val testPassword = "unknown"

    buildTypes {
        getByName("debug") {
            buildConfigField("String", "CLIENT_VERSION", "\"${libs.versions.appVersion.get()}\"")
            buildConfigField("String", "TEST_MODE", "\"$testMode\"")
            buildConfigField("String", "SERVER_URL", "\"$serverUrl\"")
            buildConfigField("String", "TEST_USER", "\"$testUser\"")
            buildConfigField("String", "TEST_PASSWORD", "\"$testPassword\"")
        }

        getByName("release") {
            buildConfigField("String", "CLIENT_VERSION", "\"${libs.versions.appVersion.get()}\"")
            buildConfigField("String", "TEST_MODE", "\"MOCK\"")
            buildConfigField("String", "SERVER_URL", "\"http://localhost:8096\"")
            buildConfigField("String", "TEST_USER", "\"test\"")
            buildConfigField("String", "TEST_PASSWORD", "\"unknown\"")
        }
    }

    lint {
        abortOnError = false
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    lintChecks(project(":lint-rules"))

    api(project(":serenity-common"))
    api(project(":serenity-android-common"))
    api(project(":manager"))

    implementation(libs.kotlin)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.kotlin.coroutines.core)

    releaseApi(libs.toothpick.runtime) {
        exclude(group = "javax.inject")
    }
    releaseApi(libs.toothpick.smoothie) {
        exclude(group = "javax.inject")
    }

    debugImplementation(libs.toothpick.runtime)
    debugImplementation(libs.toothpick.smoothie)

    releaseApi(libs.toothpick.javax.annotations)
    ksp(libs.toothpick.ksp.compiler)

    implementation(libs.eventbus)
    implementation(libs.moshi)
    implementation(libs.retrofit.moshi)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.timber)
    implementation(libs.androidx.annotation)

    testImplementation(libs.junit)
    testImplementation(libs.assertj.core)
    testImplementation(libs.assertk.jvm)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockk)
    testImplementation(libs.robolectric)
    testImplementation(libs.robolectric.shadows.framework)
    testImplementation(libs.opengl.api)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.kotlin.coroutines.test)

    testImplementation(libs.toothpick.testing)
    kspTest(libs.toothpick.ksp.compiler)
    testImplementation(libs.turbine)
    testImplementation(libs.okhttp.mockwebserver)
}
