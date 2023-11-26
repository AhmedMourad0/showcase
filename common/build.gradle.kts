import dev.icerock.gradle.MRVisibility

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlin-parcelize")
    id("org.jetbrains.compose")
    id("dev.icerock.mobile.multiplatform-resources")
    id("co.touchlab.skie") version "0.4.20"
}

group = "dev.ahmedmourad.showcase.common"
version = "1.0-SNAPSHOT"

kotlin {
    targetHierarchy.default()
    androidTarget {
        dependencies {
            coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
        }
    }
    jvm("desktop") {
        jvmToolchain(17)
    }
    ios()
    cocoapods {
        summary = "Some description for the Common Module"
        homepage = "Link to the Common Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../ios/Podfile")
        framework {
            baseName = "common"
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                api(compose.materialIconsExtended)
                api("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                api("dev.icerock.moko:resources:0.23.0")
                api("dev.icerock.moko:resources-compose:0.23.0")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("dev.icerock.moko:resources-test:0.23.0")
            }
        }
        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                api("androidx.compose.material3:material3:1.2.0-alpha05")
                api("androidx.compose.material3:material3-window-size-class:1.2.0-alpha05")
                api("androidx.activity:activity-compose:1.7.2")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.10.1")
                api("androidx.startup:startup-runtime:1.1.1")
                api("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
                api("com.google.android.material:material:1.9.0")
                api("com.google.accompanist:accompanist-systemuicontroller:0.31.4-beta")
                api("com.google.accompanist:accompanist-pager-indicators:0.31.5-beta")
                api("androidx.compose.ui:ui-tooling-preview:1.4.3")
                api("androidx.datastore:datastore-preferences:1.0.0")
                api("androidx.core:core-splashscreen:1.0.1")
                api("androidx.compose.runtime:runtime-tracing:1.0.0-alpha03")
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.1")
            }
        }
        val desktopTest by getting
        val iosMain by getting {
            dependsOn(commonMain)
            dependencies {

            }
        }
//        val iosTest by getting {
//            dependsOn(iosMain)
//        }
    }
}

android {
    namespace = "dev.ahmedmourad.showcase.common"
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 23
        targetSdk = 34
        multiDexEnabled = true
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "dev.ahmedmourad.showcase.common"
    multiplatformResourcesClassName = "RR"
    multiplatformResourcesVisibility = MRVisibility.Public
    iosBaseLocalizationRegion = "en"
    multiplatformResourcesSourceSet = "commonMain"
}
