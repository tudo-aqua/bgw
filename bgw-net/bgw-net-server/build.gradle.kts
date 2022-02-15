import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.vaadin") version "0.14.6.0"
	kotlin("jvm")
	kotlin("plugin.spring") version "1.5.31"
	kotlin("plugin.jpa") version "1.5.0"
}

group = rootProject.group
version = rootProject.version
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

extra["vaadinVersion"] = "14.7.3"

dependencies {
	implementation(project(":bgw-net:bgw-net-common"))

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
	//implementation("com.github.everit-org.json-schema:org.everit.json.schema:1.14.0")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	//json kotlin schema
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
	implementation("com.networknt:json-schema-validator:1.0.65")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.vladmihalcea:hibernate-types-52:2.14.0")
	runtimeOnly("org.postgresql", "postgresql")

	implementation("com.vaadin:vaadin-spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
	imports {
		mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
