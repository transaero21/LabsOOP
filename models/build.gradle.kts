java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":map"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines)
    testImplementation(libs.kotlin.test)
}

tasks.test {
    useJUnitPlatform()
}
