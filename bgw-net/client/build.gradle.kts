val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
	application
	kotlin("jvm")
}

group = "tools.aqua"
version = "0.0.1"
application {
	mainClass.set("tools.aqua.bgw.net.client.ApplicationKt")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("io.ktor:ktor-client-websockets:$ktor_version")
	implementation("io.ktor:ktor-client-cio:$ktor_version")
}