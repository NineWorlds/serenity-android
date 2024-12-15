import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("com.android.application")
  id("project-report")
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlin-allopen")
  id("org.sonarqube")
  id("com.google.firebase.crashlytics")
  id("com.google.gms.google-services") version "4.4.0"
}

apply(from = "../jacoco.gradle")

sonarqube {
  properties {
    property("sonar.host.url", "https://sonarcloud.io")
    property("sonar.organization", "kingargyle-github")
    property("sonar.login", "cb7af08f0b0e86306ac5365074326dc27aba503e")
    property("sonar.jacoco.reportPath", "${project.buildDir}/jacoco/testDebugUnitTest.exec")
//    property "sonar.coverage.jacoco.xmlReportPaths", "${project.buildDir}/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"
    property("sonar.coverage.exclusions", "**/R.class, **/R\$*.class, **/BuildConfig.*, **/Manifest*.*, **/*Test*.*,android/**/*.*, **/*MemberInjector.*, **/*\$\$Factory*.*, **/*..State*.*, **/*_ViewBinding*.*")
  }
}

allOpen {
  annotation("us.nineworlds.serenity.common.annotations.OpenForTesting")
}

android {
  namespace = "us.nineworlds.serenity"

  defaultConfig {
    versionCode = 3000000
    versionName = "3.0.0-M1"
    minSdk = libs.versions.minSdkVersion.get().toInt()
    targetSdk = libs.versions.targetSdkVersion.get().toInt()
    multiDexEnabled = true
    multiDexKeepProguard = file("multidex_keep_file.txt")

    buildFeatures {
      viewBinding = true
    }
  }

  sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin", "src/main/java")
    getByName("test").java.srcDirs("src/test/kotlin", "src/test/java")
  }

  compileSdk = libs.versions.targetSdkVersion.get().toInt()

  if (project.hasProperty("keystore")) {
    signingConfigs {
      getByName("release") {
        storeFile = file(project.property("keystore") as String)
        storePassword = project.property("storepass") as String?
        keyAlias = project.property("keyalias") as String?
        keyPassword = project.property("keypass") as String?
      }
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  buildTypes {
    getByName("debug") {
      isMinifyEnabled = false
      isTestCoverageEnabled = false
    }
    getByName("release") {
      if (project.hasProperty("keystore")) {
        signingConfig = signingConfigs.getByName("release")
      }
      isMinifyEnabled = false
    }
  }

  lintOptions {
    isCheckReleaseBuilds = false
    // Or, if you prefer, you can continue to check for errors in release builds,
    // but continue the build even when errors are found:
    isAbortOnError = false
  }

  testOptions {
    unitTests {
      isReturnDefaultValues = true
      isIncludeAndroidResources = true
      all {
        it.apply {
          minHeapSize = "1024m"
          maxHeapSize = "1512m"
          setForkEvery(100)
          maxParallelForks = 2
          jvmArgs("-noverify")
          testLogging {
            setExceptionFormat("full")
          }
        }
      }
    }
  }
}

dependencies {

  implementation(platform(libs.firebase.bom))

  implementation(project(":subtitle-converter"))
  implementation(project(":emby-lib"))
  implementation(project(":serenity-android-common"))
  implementation(project(":serenity-common"))

  implementation(libs.androidx.recycler.view)

  implementation(libs.firebase.analytics)
  implementation(libs.firebase.crashlytics)

  implementation(libs.github.glide.okhttp)
  implementation(libs.exoplayer.core) {
    exclude(module = "support-annotations")
  }
  implementation(libs.exoplayer.ui) {
    exclude(module = "support-annotations")
  }

  implementation(libs.exoplayer.okhttp) {
    exclude(module = "support-annotations")
  }

  implementation(libs.okhttp) {
    exclude(group = "com.android.support")
  }

  implementation(libs.androidx.fragment.ktx)
  implementation(libs.material)
  implementation(libs.kotlin)
  implementation(libs.kotlin.coroutines.android)
  implementation(libs.moxy.community.moxy)
  implementation(libs.moxy.community.moxy.app.compat)
  implementation(libs.moxy.ktx)
  implementation(libs.github.glide)
  kapt(libs.glide.compiler)
  implementation(libs.android.priority.jobqueue)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.leanback)
  implementation(libs.androidx.leanback.preference)
  implementation(libs.androidx.legacy.support.v4)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.percentlayout)
  implementation(libs.androidx.annotation)
  implementation(libs.juniversalchardet)
  implementation(libs.okhttp.logging.interceptor)
  implementation(project(":manager"))
  implementation(libs.okhttp.urlconnection)
  implementation(libs.timber)
  implementation(libs.androidx.cardview)
  implementation(libs.androidx.annotation)

  releaseImplementation(libs.toothpick.runtime) {
    exclude(group = "javax.inject")
  }
  releaseImplementation(libs.toothpick.smoothie) {
    exclude(group = "javax.inject")
  }

  debugImplementation(libs.toothpick.runtime)

  releaseImplementation(libs.toothpick.javax.annotations)
  kapt(libs.toothpick.compiler)

  implementation(libs.moshi)
  implementation(libs.retrofit.moshi)
  implementation(libs.joda.time)
  implementation(libs.retrofit)
  implementation(libs.flexbox)
  //implementation("com.henryblue.library:tvrecyclerview:1.2.2")
  implementation(libs.recyclerview.animators)

  implementation(libs.resourceful) {
    exclude(group = "com.google.guava")
  }

  // https://mvnrepository.com/artifact/org.simpleframework/simple-xml
  implementation(libs.simple.xml) {
    exclude(group = "stax")
    exclude(group = "xpp3")
  }

  testImplementation(libs.mockito.core)
  testImplementation(libs.mockito.kotlin)
  testImplementation(libs.commons.lang3)
  testImplementation(libs.kotlin.coroutines.test)

  testImplementation(libs.okhttp.mockwebserver)
  testImplementation(libs.commons.io)
  testImplementation(libs.toothpick.testing)
  testImplementation(libs.robolectric) {
    exclude(module = "support-v4")
  }
  testImplementation(libs.assertj.android) {
    exclude(module = "support-v4")
    exclude(module = "support-annotations")
  }

  testImplementation(libs.assertk.jvm)
  testImplementation(libs.assertj.core)
  testImplementation(libs.junit)
  testImplementation(libs.robolectric.shadows.framework)
  testImplementation(libs.robolectric.shadows.api)
  testImplementation(libs.robolectric.shadows.playservices)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.opengl.api)
  testImplementation(libs.androidx.junit)

  kaptTest(libs.toothpick.compiler)

  kapt(libs.moxy.compiler)
}

//com.google.gms.googleservices.GoogleServicesPlugin.config.disableVersionCheck = true


configurations.all {
  resolutionStrategy {
    force(libs.androidx.fragment)
  }
}