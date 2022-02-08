@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
}

group = "tools.aqua"
version = rootProject.version

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation(project(":bgw-ui"))
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "11"
}