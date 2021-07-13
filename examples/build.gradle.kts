@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.openjfx.javafxplugin") version "0.0.8"
	kotlin("jvm")
}

//val versionNumber = "0.1"
val versionNumber = "0.1-SNAPSHOT"

group = "tools.aqua"
version = versionNumber

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation("com.jfoenix", "jfoenix", "9.0.1")
	testImplementation(kotlin("test"))
	implementation(project(":framework"))
}

tasks.test {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "11"
}

javafx {
	modules("javafx.controls", "javafx.fxml")
}



