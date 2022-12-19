object Versions {

    const val kotlinVersion = "1.6.20"
    const val androidPluginVersion = "7.1.3"
    const val minSdkVersion = 22
    const val targetSdkVersion = 32

    const val appversion = "2.1.0"
    const val okhttpVersion = "4.9.1"
    const val androidPriorityJobQueueVersion = "2.0.1"
    const val eventBus = "3.1.1"
    const val moshiKotlinVersion = "1.12.0"
    const val retrofitVersion = "2.9.0"
    const val jodaTimeVersion = "2.10.9.1"
    const val timberVersion = "5.0.1"
    const val exoplayerVersion = "2.17.1"
    const val glideOkHttpVersion = "4.12.0"
    const val moxyVersion = "2.2.2"
    const val glideVersion = "4.12.0"
    const val googleAnalyticsVersion = "16.0.4"
    const val universalCharDetVersion = "1.0.3"
    const val butterKnifeVersion = "10.2.1"
    const val simpleXmlVersion = "2.7.1"
    const val urlManager = "1.4.0"

    const val commonsLangVersion = "3.7"
    const val junitVersion = "4.13.1"
    const val assertkVersion = "0.24"
    const val assertjVersion = "3.11.1"
    const val mockitoVersion = "3.12.4"
    const val robolectricVersion = "4.7.3"
    const val openglApiVersion = "gl1.1-android-2.1_r1"
    const val xmlUnitVersion = "1.3"
    const val assertJAndroidVersion = "1.1.1"
    const val commonsioVersion = "2.6"
    const val jacocoVersion = "0.8.7"
    const val toothPickVersion = "3.1.0"
}

object Dependencies {

    object Implementation {
        val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$Versions.kotlinVersion"
        val toothPickRelease = "com.github.stephanenicolas.toothpick:toothpick-runtime:$Versions.toothPickVersion"
    }
}