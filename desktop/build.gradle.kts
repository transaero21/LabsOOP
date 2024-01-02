java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

sourceSets {
    main {
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

application {
    mainClass.set(mainClassName)
    applicationDefaultJvmArgs = listOf("-XstartOnFirstThread")
}

tasks.jar {
    manifest.attributes["Main-Class"] = mainClassName
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
