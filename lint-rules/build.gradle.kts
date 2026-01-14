import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    compileOnly("com.android.tools.lint:lint-api:31.3.1")
    compileOnly("com.android.tools.lint:lint-checks:31.3.1")

    testImplementation("com.android.tools.lint:lint:31.3.1")
    testImplementation("com.android.tools.lint:lint-tests:31.3.1")
    testImplementation("junit:junit:4.13.2")
}

tasks.jar {
    manifest {
        attributes("Lint-Registry-v2" to "us.nineworlds.serenity.lint.PreferenceMigrationIssueRegistry")
    }
}
