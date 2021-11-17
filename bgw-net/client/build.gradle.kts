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

	implementation("org.java-websocket:Java-WebSocket:1.5.2")
	implementation(project(":bgw-net:common"))
}