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
    implementation(libs.kotlin)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.androidx.annotation)

    implementation(libs.toothpick.runtime) {
        exclude(group = "javax.inject")
    }

    implementation(libs.kotlin.coroutines.android)

}
