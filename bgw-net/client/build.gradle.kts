val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
	kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
	mavenCentral()
}

dependencies {
	implementation("io.ktor:ktor-client-websockets:$ktor_version")
	implementation("io.ktor:ktor-client-cio:$ktor_version")
}