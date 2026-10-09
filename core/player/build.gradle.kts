plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    )

    sourceSets {
        commonMain.dependencies {
            api(project(":core:model"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.koin.android)
            api(libs.media3.exoplayer)
            implementation(libs.media3.exoplayer.hls)
            implementation(libs.media3.exoplayer.dash)
            api(libs.media3.ui)
            implementation(libs.media3.common)
            implementation(libs.media3.session)
        }
    }
}

android {
    namespace = "com.cbhard.videoplayer.core.player"
    compileSdk = 37
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
