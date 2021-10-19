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
	implementation(project(":bgw-core"))
	implementation(project(":bgw-net:client"))
	implementation(project(":bgw-net:common"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "11"
}