@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.openjfx.javafxplugin") version "0.0.8"
	id("org.jetbrains.dokka") version "1.4.32"
	id("io.gitlab.arturbosch.detekt") version "1.17.0"
	id("maven-publish")
	kotlin("jvm") version "1.5.0"
}

group = "tools.aqua"
version = "1.0"

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation("com.jfoenix", "jfoenix", "9.0.1")
	
	testImplementation(kotlin("test"))
	
	dokkaGfmPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.4.32")
}

tasks.test {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "11"
}

sourceSets {
	getByName("main") {
		java.srcDirs("src/main")
		resources.srcDirs("src/main/resources")
	}
	getByName("test") {
		java.srcDirs("src/test")
	}
}

javafx {
	modules("javafx.controls", "javafx.fxml")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
	jvmTarget = "11"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
	jvmTarget = "11"
}

detekt {
	// Version of Detekt that will be used. When unspecified the latest detekt
	// version found will be used. Override to stay on the same version.
	toolVersion = "1.17.0"
	
	// The directories where detekt looks for source files.
	// Defaults to `files("src/main/java", "src/main/kotlin")`.
	input = files("src/tools/aqua/bgw")
	
	config = files("detekt-rules.yml")
	
}

val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)
val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
	dependsOn(dokkaHtml)
	archiveClassifier.set("javadoc")
	from(dokkaHtml.outputDirectory)
}

java {
	withSourcesJar()
	withJavadocJar()
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = "tools.aqua"
			artifactId = "bgw-core"
			version = "0.1-SNAPSHOT"
			from(components["java"])
		}
	}
}