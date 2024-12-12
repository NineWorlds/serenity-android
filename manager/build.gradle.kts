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
    minSdkVersion(Versions.minSdkVersion)
    targetSdkVersion(Versions.targetSdkVersion)
  }

  compileSdkVersion(Versions.targetSdkVersion)
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
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

  api("com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}")
//  compileOnly("com.squareup.okhttp3:okhttp:${Versions.okhttpVersion}")
}
