plugins {
	kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
	implementation("org.java-websocket:Java-WebSocket:1.5.2")
	implementation(project(":bgw-net:common"))
}