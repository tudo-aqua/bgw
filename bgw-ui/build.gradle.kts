@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.jetbrains.dokka") version "1.4.32"
	id("io.gitlab.arturbosch.detekt") version "1.18.0"
	id("org.cadixdev.licenser") version "0.6.1"
	id("com.dorongold.task-tree") version "2.1.0" // example usage: ./gradlew publish taskTree
	`maven-publish`
	kotlin("jvm")
	signing
}

group = "tools.aqua"
version = rootProject.version
var javaFxVersion: String = "12.0.1"
extra["isReleaseVersion"] = !version.toString().endsWith("SNAPSHOT")

repositories {
	mavenCentral()
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation("com.jfoenix", "jfoenix", "9.0.1")
	
	testImplementation(kotlin("test"))
	
	dokkaGfmPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.4.32")

	implementation(group="org.openjfx", name="javafx-base", version=javaFxVersion, classifier="win")
	implementation(group="org.openjfx", name="javafx-base", version=javaFxVersion, classifier="mac")
	implementation(group="org.openjfx", name="javafx-base", version=javaFxVersion, classifier="linux")
	implementation(group="org.openjfx", name="javafx-controls", version=javaFxVersion, classifier="win")
	implementation(group="org.openjfx", name="javafx-controls", version=javaFxVersion, classifier="mac")
	implementation(group="org.openjfx", name="javafx-controls", version=javaFxVersion, classifier="linux")
	implementation(group="org.openjfx", name="javafx-fxml", version=javaFxVersion, classifier="win")
	implementation(group="org.openjfx", name="javafx-fxml", version=javaFxVersion, classifier="mac")
	implementation(group="org.openjfx", name="javafx-fxml", version=javaFxVersion, classifier="linux")
	implementation(group="org.openjfx", name="javafx-graphics", version=javaFxVersion, classifier="win")
	implementation(group="org.openjfx", name="javafx-graphics", version=javaFxVersion, classifier="mac")
	implementation(group="org.openjfx", name="javafx-graphics", version=javaFxVersion, classifier="linux")
}

tasks.test {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions.jvmTarget = "11"
}

detekt {
	source = files("src/main/kotlin/tools/aqua/bgw")
	config = files("detekt-rules.yml")
}

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
	archiveClassifier.set("javadoc")
	from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
}

java {
	withSourcesJar()
	withJavadocJar()
}

publishing {
	publications {
		create<MavenPublication>("maven") {

			from(components["java"])

			pom {
				name.set("BoardGameWork Core Library")

				description.set(
					"A framework for board game applications."
				)

				url.set("https://github.com/tudo-aqua/bgw-core")

				licenses {
					license {
						name.set("Apache License, Version 2.0")
						url.set("https://opensource.org/licenses/Apache-2.0")
					}
				}

				developers {
					developer {
						name.set("Stefan Naujokat")
						email.set("stefan.naujokat@tu-dortmund.de")
					}
					developer {
						name.set("Till Schallau")
						email.set("till.schallau@tu-dortmund.de")
					}
					developer {
						name.set("Dominik Mäckel")
						email.set("dominik.maeckel@tu-dortmund.de")
					}
					developer {
						name.set("Fabian Klümpers")
						email.set("fabian.kluempers@tu-dortmund.de")
					}
				}

				scm {
					connection.set("scm:git:git://github.com:tudo-aqua/bgw-core.git")
					developerConnection.set("scm:git:ssh://git@github.com:tudo-aqua/bgw-core.git")
					url.set("https://github.com/tudo-aqua/bgw-core/tree/main")
				}
			}
		}
	}
	repositories {
		maven {
			name = "nexusOSS"
			val releasesUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
			val snapshotsUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
			url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
			credentials {
				username = properties["nexusUsername"] as? String
				password = properties["nexusPassword"] as? String
			}
		}
	}
}

signing {
	useGpgCmd()
	sign(publishing.publications["maven"])
}

tasks.withType<Sign>().configureEach {
	onlyIf { project.extra["isReleaseVersion"] as Boolean }
}

tasks.named("publishMavenPublicationToNexusOSSRepository") {
	dependsOn (tasks.checkLicenses)
}

license {
	header(rootProject.file("LICENSE-HEADER.txt"))
}

