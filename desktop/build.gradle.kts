import org.gradle.internal.os.OperatingSystem

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

sourceSets {
    main {
        java {
            srcDir("src")
        }
        resources {
            srcDir("../assets")
        }
    }
}

plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

dependencies {
    implementation(project(":core"))
    implementation(libs.kotlin.stdlib)
    api(libs.gdx.backend.lwjgl3)
    libs.gdx.platform.let { platform ->
        api(variantOf(platform) { classifier("natives-desktop") })
    }
}

val mainClassName = "ru.transaero21.mt.DesktopLauncherKt"
val assetsDir = file("../assets")

application {
    mainClass.set(mainClassName)
    applicationDefaultJvmArgs = listOf("-XstartOnFirstThread")
}

//tasks.register<Jar>("fatJar") {
//    destinationDirectory.set(assetsDir)
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//    manifest {
//        attributes["Main-Class"] = mainClassName
//    }
//    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
//    with(tasks.jar.get() as CopySpec)
//}

tasks.jar {
    manifest.attributes["Main-Class"] = mainClassName
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
