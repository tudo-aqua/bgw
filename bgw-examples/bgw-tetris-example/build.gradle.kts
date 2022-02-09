@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	application
}

group = "tools.aqua"
version = rootProject.version

repositories {
	mavenCentral()
}

application {
	mainClass.set("tools.aqua.bgw.examples.tetris.main.MainKt")
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation(project(":bgw-ui"))
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "11"
}

tasks.withType<JavaExec> {
    doFirst {
        jvmArgs(
			listOf(
				"--module-path", classpath.asPath,
				"--add-modules", "javafx.controls"
			)
		)
    }
}