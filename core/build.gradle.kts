java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(project(":map"))
    implementation(project(":models"))
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.serialization.json)
    api(libs.gdx.core)
    implementation(libs.gdx.ktx.actors)
    implementation(libs.gdx.ktx.async)
    implementation(libs.gdx.ktx.json)
    implementation(libs.gdx.ktx.scene2d)
}
