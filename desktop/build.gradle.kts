import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "dev.ahmedmourad.showcase.desktop"
version = "1.0-SNAPSHOT"

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "dev.ahmedmourad.showcase.desktop.MainKt"
        nativeDistributions {
            targetFormats(
                TargetFormat.Pkg,
                TargetFormat.Exe,
                TargetFormat.Msi,
                TargetFormat.Deb,
                TargetFormat.Rpm
            )
            packageName = "showcase"
            packageVersion = "1.0.0"
            jvmArgs("-Dapple.awt.application.appearance=system")
        }
    }
}
