plugins {
    id("java")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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
