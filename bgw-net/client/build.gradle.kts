val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

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
	implementation("io.ktor:ktor-client-websockets:$ktor_version")
	implementation("io.ktor:ktor-client-cio:$ktor_version")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
	implementation(project(":bgw-net:common"))
}