@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.openjfx.javafxplugin") version "0.0.8"
	id("org.jetbrains.dokka") version "1.4.32"
	id("io.gitlab.arturbosch.detekt") version "1.17.0"
	id("maven-publish")
	kotlin("jvm") version "1.5.0"
	signing
}

group = "tools.aqua"
version = "0.1-SNAPSHOT"

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
			
			pom {
				name.set("BoardGameWork Core Library")
				
				description.set(
					"A framework for board game applications."
				)
				
				url.set("https://github.com/tudo-aqua/bgw-core")
				
				licenses {
					license {
						name.set("The MIT License")
						url.set("https://opensource.org/licenses/MIT")
					}
					license {
						name.set("ISC License")
						url.set("https://opensource.org/licenses/ISC")
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
	isRequired = !hasProperty("skip-signing")
	useGpgCmd()
	sign(publishing.publications["maven"])
}
