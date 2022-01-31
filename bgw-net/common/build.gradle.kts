plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.31"
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.0")
}
