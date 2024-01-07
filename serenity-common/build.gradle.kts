import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
    id("java")
    id("kotlin")
    kotlin("kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}



dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlinVersion}")

    implementation("com.squareup.okhttp3:okhttp:${Versions.okhttpVersion}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.okhttpVersion}")
    implementation("androidx.annotation:annotation:1.2.0")

    implementation("com.github.stephanenicolas.toothpick:toothpick-runtime:${Versions.toothPickVersion}") {
        exclude(group = "javax.inject")
    }

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1")

}
