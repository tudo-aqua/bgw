val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
	application
	kotlin("jvm")
}

group = "tools.aqua"
version = rootProject.version
application {
	mainClass.set("tools.aqua.ApplicationKt")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("io.ktor:ktor-server-core:$ktor_version")
	implementation("io.ktor:ktor-server-netty:$ktor_version")
	implementation("ch.qos.logback:logback-classic:$logback_version")
	testImplementation("io.ktor:ktor-server-tests:$ktor_version")
	testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
}