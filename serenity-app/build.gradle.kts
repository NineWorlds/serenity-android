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
    minSdk = Versions.minSdkVersion
    targetSdk = Versions.targetSdkVersion
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

  compileSdk = Versions.targetSdkVersion

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
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

  implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

  implementation(project(":subtitle-converter"))
  implementation(project(":emby-lib"))
  implementation(project(":serenity-android-common"))
  implementation(project(":serenity-common"))

  implementation("androidx.recyclerview:recyclerview:1.2.1") {
    version {
      strictly("1.2.1")
    }
  }

  implementation("com.google.firebase:firebase-analytics")
  implementation("com.google.firebase:firebase-crashlytics")

  implementation(group = "com.github.bumptech.glide", name = "okhttp3-integration", version = "${Versions.glideOkHttpVersion}")
  implementation("com.google.android.exoplayer:exoplayer-core:${Versions.exoplayerVersion}") {
    exclude(module = "support-annotations")
  }
  implementation("com.google.android.exoplayer:exoplayer-ui:${Versions.exoplayerVersion}") {
    exclude(module = "support-annotations")
  }

  implementation("com.google.android.exoplayer:extension-okhttp:${Versions.exoplayerVersion}") {
    exclude(module = "support-annotations")
  }

  implementation("com.squareup.okhttp3:okhttp:${Versions.okhttpVersion}") {
    exclude(group = "com.android.support")
  }

  implementation("androidx.fragment:fragment-ktx:1.4.0")
  implementation("com.google.android.material:material:1.4.0")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
  implementation("com.github.moxy-community:moxy:${Versions.moxyVersion}")
  implementation("com.github.moxy-community:moxy-app-compat:${Versions.moxyVersion}")
  implementation("com.github.moxy-community:moxy-ktx:${Versions.moxyVersion}")
  implementation("com.github.bumptech.glide:glide:${Versions.glideVersion}")
  kapt("com.github.bumptech.glide:compiler:${Versions.glideVersion}")
  implementation("com.birbit:android-priority-jobqueue:${Versions.androidPriorityJobQueueVersion}")
  implementation("androidx.appcompat:appcompat:1.3.1")
  implementation("androidx.leanback:leanback:1.1.0-rc02")
  implementation("androidx.leanback:leanback-preference:1.1.0-rc01")
  implementation("androidx.legacy:legacy-support-v4:1.0.0")
  implementation("androidx.constraintlayout:constraintlayout:2.1.0")
  implementation("androidx.percentlayout:percentlayout:1.0.0")
  implementation("androidx.annotation:annotation:1.2.0")
  implementation("com.googlecode.juniversalchardet:juniversalchardet:${Versions.universalCharDetVersion}")
  implementation("com.squareup.okhttp3:logging-interceptor:${Versions.okhttpVersion}")
  implementation("me.jessyan:retrofit-url-manager:${Versions.urlManager}")
  implementation("com.squareup.okhttp3:okhttp-urlconnection:${Versions.okhttpVersion}")
  implementation("com.jakewharton.timber:timber:${Versions.timberVersion}")
  implementation("androidx.cardview:cardview:1.0.0")
  implementation("androidx.annotation:annotation:1.2.0")

  releaseImplementation("com.github.stephanenicolas.toothpick:toothpick-runtime:${Versions.toothPickVersion}") {
    exclude(group = "javax.inject")
  }
  releaseImplementation("com.github.stephanenicolas.toothpick:smoothie:${Versions.toothPickVersion}") {
    exclude(group = "javax.inject")
  }

  debugImplementation("com.github.stephanenicolas.toothpick:toothpick-runtime:${Versions.toothPickVersion}")

  releaseImplementation("com.github.stephanenicolas.toothpick:toothpick-javax-annotations:${Versions.toothPickVersion}")
  kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:${Versions.toothPickVersion}")

  implementation("com.squareup.moshi:moshi-kotlin:${Versions.moshiKotlinVersion}")
  implementation("com.squareup.retrofit2:converter-moshi:${Versions.retrofitVersion}")
  implementation("net.danlew:android.joda:${Versions.jodaTimeVersion}")
  implementation("com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}")
  implementation("com.google.android:flexbox:1.1.0")
  implementation("com.henryblue.library:tvrecyclerview:1.2.2")
  implementation("jp.wasabeef:recyclerview-animators:3.0.0")

  implementation("com.github.rstanic12:Resourceful:1.0.0") {
    exclude(group = "com.google.guava")
  }

  // https://mvnrepository.com/artifact/org.simpleframework/simple-xml
  implementation("org.simpleframework:simple-xml:${Versions.simpleXmlVersion}") {
    exclude(group = "stax")
    exclude(group = "xpp3")
  }

  testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
  testImplementation("org.apache.commons:commons-lang3:${Versions.commonsLangVersion}")
  testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

  testImplementation("com.squareup.okhttp3:mockwebserver:${Versions.okhttpVersion}")
  testImplementation("commons-io:commons-io:${Versions.commonsioVersion}")
  testImplementation("com.github.stephanenicolas.toothpick:toothpick-testing:${Versions.toothPickVersion}")
  testImplementation("org.robolectric:robolectric:${Versions.robolectricVersion}") {
    exclude(module = "support-v4")
  }
  testImplementation("com.squareup.assertj:assertj-android:${Versions.assertJAndroidVersion}") {
    exclude(module = "support-v4")
    exclude(module = "support-annotations")
  }

  testImplementation("com.willowtreeapps.assertk:assertk-jvm:${Versions.assertkVersion}")
  testImplementation("org.assertj:assertj-core:${Versions.assertjVersion}")
  testImplementation("junit:junit:${Versions.junitVersion}")
  testImplementation("org.robolectric:shadows-framework:${Versions.robolectricVersion}@jar")
  testImplementation("org.robolectric:shadowapi:${Versions.robolectricVersion}")
  testImplementation("org.robolectric:shadows-playservices:${Versions.robolectricVersion}")
  testImplementation("androidx.test:core:1.4.0")
  testImplementation("org.khronos:opengl-api:${Versions.openglApiVersion}")
  testImplementation("org.mockito:mockito-core:${Versions.mockitoVersion}")
  testImplementation("androidx.test.ext:junit:1.1.3")

  kaptTest("com.github.stephanenicolas.toothpick:toothpick-compiler:${Versions.toothPickVersion}")

  kapt("com.github.moxy-community:moxy-compiler:${Versions.moxyVersion}")
}

//com.google.gms.googleservices.GoogleServicesPlugin.config.disableVersionCheck = true



configurations.all {
  resolutionStrategy {
    force("androidx.fragment:fragment:1.4.0")
  }
}