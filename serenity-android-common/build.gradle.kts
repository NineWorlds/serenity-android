plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
}

android {
  compileSdkVersion(Versions.targetSdkVersion)

  defaultConfig {
    minSdkVersion(Versions.minSdkVersion)
    targetSdkVersion(Versions.targetSdkVersion)
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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
  api("org.greenrobot:eventbus:${Versions.eventBus}")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}")
  api("com.birbit:android-priority-jobqueue:${Versions.androidPriorityJobQueueVersion}")

  releaseApi("com.github.stephanenicolas.toothpick:toothpick-runtime:${Versions.toothPickVersion}") {
    exclude(group = "javax.inject")
  }
  releaseApi("com.github.stephanenicolas.toothpick:smoothie:${Versions.toothPickVersion}") {
    exclude(group = "javax.inject")
  }

  debugApi("com.github.stephanenicolas.toothpick:toothpick-runtime:${Versions.toothPickVersion}")
  debugApi("com.github.stephanenicolas.toothpick:smoothie:${Versions.toothPickVersion}")

  releaseApi("com.github.stephanenicolas.toothpick:toothpick-javax-annotations:${Versions.toothPickVersion}")
  kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:${Versions.toothPickVersion}")

  testImplementation("junit:junit:${Versions.junitVersion}")
  testImplementation("org.assertj:assertj-core:${Versions.assertjVersion}")
  testImplementation("org.mockito:mockito-core:${Versions.mockitoVersion}")
  testImplementation("org.robolectric:robolectric:${Versions.robolectricVersion}") {
    exclude(module = "support-v4")
  }
  testImplementation("org.robolectric:shadows-framework:${Versions.robolectricVersion}")
  testImplementation("org.robolectric:shadowapi:${Versions.robolectricVersion}")
  testImplementation("org.robolectric:shadows-playservices:${Versions.robolectricVersion}")
  testImplementation("org.khronos:opengl-api:${Versions.openglApiVersion}")

  testImplementation("com.github.stephanenicolas.toothpick:toothpick-testing:${Versions.toothPickVersion}")
  kaptTest("com.github.stephanenicolas.toothpick:toothpick-compiler:${Versions.toothPickVersion}")
}
