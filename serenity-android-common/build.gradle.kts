plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
  id("com.google.devtools.ksp")
}

android {
  namespace = "us.nineworlds.serenity.common.android"

  compileSdk = libs.versions.targetSdkVersion.get().toInt()

  defaultConfig {
    minSdkVersion(libs.versions.minSdkVersion.get())
    targetSdkVersion(libs.versions.targetSdkVersion.get())
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
    getByName("test").java.srcDirs("src/test/kotlin")
  }

  buildTypes {

    getByName("debug") {
      isMinifyEnabled = false
    }

    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
    }

  }
}

dependencies {
  api(project(":serenity-common"))
  api(libs.eventbus)
  implementation(libs.kotlin)

  releaseApi(libs.toothpick.runtime) {
    exclude(group = "javax.inject")
  }
  releaseApi(libs.toothpick.smoothie) {
    exclude(group = "javax.inject")
  }

  debugApi(libs.toothpick.runtime)
  debugApi(libs.toothpick.smoothie)

  releaseApi(libs.toothpick.javax.annotations)
  ksp(libs.toothpick.ksp.compiler)

  testImplementation(libs.junit)
  testImplementation(libs.assertj.core)
  testImplementation(libs.mockito.core)
  testImplementation(libs.opengl.api)

  testImplementation(libs.toothpick.testing)
  kspTest(libs.toothpick.ksp.compiler)
}
