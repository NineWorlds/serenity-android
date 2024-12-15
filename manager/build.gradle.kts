plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
}

android {
  namespace = "me.jessyan.retrofiturlmanager"
  buildFeatures {
    buildConfig = true
  }
  defaultConfig {
    minSdkVersion(libs.versions.minSdkVersion.get())
    targetSdkVersion(libs.versions.targetSdkVersion.get())
  }

  compileSdk = libs.versions.targetSdkVersion.get().toInt()
  testOptions {
    unitTests {
      isIncludeAndroidResources = true
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin", "src/main/java")
    getByName("test").java.srcDirs("src/test/kotlin")
  }

  buildTypes {
    getByName("debug") {
    }
    
    getByName("release") {
    }
  }
}

dependencies {
  implementation(libs.kotlin)
  implementation(libs.kotlin.coroutines.android)
  implementation(libs.kotlin.coroutines.core)

  api(libs.retrofit)
//  compileOnly("com.squareup.okhttp3:okhttp:${Versions.okhttpVersion}")
}
