import java.util.Properties

plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization") version "1.8.0"
}

group = "dev.ahmedmourad.showcase.android"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
    implementation(project(":common"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("androidx.compose.runtime:runtime-tracing:1.0.0-alpha03")
    testImplementation("junit:junit:4.13.2")
    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2022.10.00"))
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}

val secrets = Properties().apply {
    load(rootProject.file("secrets.properties").reader())
}
val KeyAlias: String by secrets
val KeyPassword: String by secrets
val StoreFile: String by secrets
val StorePassword: String by secrets

android {
    namespace = "dev.ahmedmourad.showcase.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "dev.ahmedmourad.showcase.android"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    signingConfigs {
        create("signRelease") {
            keyAlias = KeyAlias
            keyPassword = KeyPassword
            storeFile = file(StoreFile)
            storePassword = StorePassword
        }
    }
    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("signRelease")
        }
    }
    buildFeatures {
        compose = true
    }
}
