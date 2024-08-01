import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "1.9.0"
}

fun getApiKey(key: String): String = gradleLocalProperties(rootDir, providers).getProperty(key)

android {
    namespace = "ksc.campus.tech.kakao.map"
    compileSdk = 34

    defaultConfig {
        applicationId = "ksc.campus.tech.kakao.map"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "ksc.campus.tech.kakao.map.CustomTestRunner"

        resValue("string", "KAKAO_API_KEY", getApiKey("KAKAO_API_KEY"))
        buildConfigField("String", "KAKAO_REST_API_KEY", getApiKey("KAKAO_REST_API_KEY"))
        buildConfigField("String", "KAKAO_API_URL", getApiKey("KAKAO_API_URL"))

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions{
        animationsDisabled = true
        unitTests.isIncludeAndroidResources = true
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    val fragment_version = "1.6.0"
    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation ("androidx.activity:activity-ktx:1.2.2")
    implementation ("androidx.fragment:fragment-ktx:1.3.3")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.kakao.maps.open:android:2.9.5")
    implementation("androidx.activity:activity:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.core:core-splashscreen:1.0.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.dagger:hilt-android-testing:2.48.1")
    testImplementation("io.mockk:mockk:1.12.4")
    debugImplementation ("androidx.fragment:fragment-testing:$fragment_version")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.fragment:fragment-testing:$fragment_version")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48.1")
    kapt("com.google.dagger:hilt-compiler:2.48.1")
    kapt("androidx.room:room-compiler:2.6.1")
    kaptTest("com.google.dagger:hilt-android-compiler:2.48.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48.1")

    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-config-ktx:22.0.0")
    implementation("com.google.firebase:firebase-messaging-ktx:24.0.0")

}
