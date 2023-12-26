plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
}

android {
  namespace = "us.nineworlds.serenity.emby"
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
      buildConfigField("String", "CLIENT_VERSION", "\"$Versions.appversion\"")
    }
    
    getByName("release") {
      buildConfigField("String", "CLIENT_VERSION", "\"$Versions.appversion\"")
    }
  }
}

dependencies {
  api(project(":serenity-common"))
  api(project(":serenity-android-common"))

  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")


  releaseApi("com.github.stephanenicolas.toothpick:toothpick-runtime:${Versions.toothPickVersion}") {
    exclude(group = "javax.inject")
  }
  releaseApi("com.github.stephanenicolas.toothpick:smoothie:${Versions.toothPickVersion}") {
    exclude(group = "javax.inject")
  }

  debugImplementation("com.github.stephanenicolas.toothpick:toothpick-runtime:${Versions.toothPickVersion}")
  debugImplementation("com.github.stephanenicolas.toothpick:smoothie:${Versions.toothPickVersion}")

  releaseApi("com.github.stephanenicolas.toothpick:toothpick-javax-annotations:${Versions.toothPickVersion}")
  kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:${Versions.toothPickVersion}")

  api("com.birbit:android-priority-jobqueue:${Versions.androidPriorityJobQueueVersion}")
  api("org.greenrobot:eventbus:${Versions.eventBus}")
  api("com.squareup.moshi:moshi-kotlin:${Versions.moshiKotlinVersion}")
  api("com.squareup.retrofit2:converter-moshi:${Versions.retrofitVersion}")
  api("net.danlew:android.joda:${Versions.jodaTimeVersion}")
  api("com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}")
  api("com.squareup.okhttp3:okhttp:${Versions.okhttpVersion}")
  api("com.squareup.okhttp3:logging-interceptor:${Versions.okhttpVersion}")
  api("me.jessyan:retrofit-url-manager:${Versions.urlManager}")
  api("com.jakewharton.timber:timber:${Versions.timberVersion}")
  api("com.github.nisrulz:easydeviceinfo-base:2.4.1")
  api("com.github.nisrulz:easydeviceinfo-common:2.4.1")

  testImplementation("junit:junit:${Versions.junitVersion}")
  testImplementation("org.assertj:assertj-core:${Versions.assertjVersion}")
  testImplementation("org.mockito:mockito-core:${Versions.mockitoVersion}")
  testImplementation("org.robolectric:robolectric:${Versions.robolectricVersion}")
  testImplementation("org.robolectric:shadows-framework:${Versions.robolectricVersion}")
  testImplementation("org.robolectric:shadowapi:${Versions.robolectricVersion}")
  testImplementation("org.robolectric:shadows-playservices:${Versions.robolectricVersion}")
  testImplementation("org.khronos:opengl-api:${Versions.openglApiVersion}")
  testImplementation("androidx.test:core:1.4.0")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")

  testImplementation("com.github.stephanenicolas.toothpick:toothpick-testing:${Versions.toothPickVersion}")
  kaptTest("com.github.stephanenicolas.toothpick:toothpick-compiler:${Versions.toothPickVersion}")
}
