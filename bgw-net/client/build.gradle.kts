val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
	kotlin("jvm")
}

group = "tools.aqua"
version = "0.0.1"

repositories {
	mavenCentral()
}

dependencies {
	implementation("io.ktor:ktor-client-websockets:$ktor_version")
	implementation("io.ktor:ktor-client-cio:$ktor_version")
}