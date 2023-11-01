plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
}

android {
    namespace =  "ru.transaero21.mt"
    compileSdk = 33

    sourceSets {
        named("main") {
            manifest.srcFile("AndroidManifest.xml")
            java.srcDirs("src")
            res.srcDirs("res")
            assets.srcDirs("../assets")
            jniLibs.srcDirs("libs")
        }
    }
    packaging {
        resources {
            excludes += "META-INF/robovm/ios/robovm.xml"
        }
    }
    defaultConfig {
        applicationId  ="ru.transaero21.mt"
        minSdk = 14
        targetSdk = 33
        versionCode = 1
        versionName = "0.0.1"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

val natives: Configuration by configurations.creating

dependencies {
    implementation(project(":core"))
    implementation(libs.kotlin.stdlib)
    api(libs.gdx.backend.android)
    libs.gdx.platform.let { platform ->
        natives(variantOf(platform) { classifier("natives-armeabi-v7a") })
        natives(variantOf(platform) { classifier("natives-arm64-v8a") })
        natives(variantOf(platform) { classifier("natives-x86") })
        natives(variantOf(platform) { classifier("natives-x86_64") })
    }
}

tasks.register("copyAndroidNatives") {
    doFirst {
        natives.files.forEach { jar ->
            val outputDir = file("libs/" + jar.nameWithoutExtension.substringAfterLast("natives-"))
            outputDir.mkdir()
            copy {
                from(zipTree(jar))
                into(outputDir)
                include("*.so")
            }
        }
    }
}

tasks.matching {
    it.name.contains("merge") && it.name.contains("JniLibFolders")
}.configureEach {
    dependsOn("copyAndroidNatives")
}
