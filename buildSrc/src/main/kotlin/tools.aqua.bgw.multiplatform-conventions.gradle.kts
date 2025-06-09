/*
 * Copyright 2024-2025 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import gradle.kotlin.dsl.accessors._cea3ad031612b62c062ef0a05a7badc1.dokkaHtml
import gradle.kotlin.dsl.accessors._cea3ad031612b62c062ef0a05a7badc1.dokkaJavadoc
import gradle.kotlin.dsl.accessors._cea3ad031612b62c062ef0a05a7badc1.java
import gradle.kotlin.dsl.accessors._cea3ad031612b62c062ef0a05a7badc1.spotless
import java.lang.ProcessHandle
import java.nio.file.Files
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension
import tools.aqua.defaultFormat

plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  application
  `maven-publish`
  id("io.gitlab.arturbosch.detekt")
  id("org.jetbrains.dokka")
  id("org.jetbrains.kotlinx.kover")
}

val propertyFile = "Config.kt"
val wrappersVersion = "2025.5.6"

fun buildDefaultPropertyFile() {
  rootDir.resolve("bgw-gui/src/jsMain/kotlin/tools/aqua/bgw/${propertyFile}").apply {
    parentFile.mkdirs()
    writeText(generateDefaultProperties())
  }

  rootDir.resolve("bgw-gui/src/jvmMain/kotlin/tools/aqua/bgw/application/${propertyFile}").apply {
    parentFile.mkdirs()
    writeText(generateDefaultProperties("application"))
  }
}

fun generateDefaultProperties(suffix: String = "") =
    """
    package tools.aqua.bgw${if (suffix.isNotEmpty()) ".$suffix" else ""}

    internal object Config {
        val USE_SOCKETS = true
        val GENERATE_SAMPLES = false
        val BGW_VERSION = "${rootProject.version}"
    }
""".trimIndent()

if (!project.extra.has("useSockets")) {
  project.extra.set("useSockets", "true")
}

if (!project.extra.has("generateSamples")) {
  project.extra.set("generateSamples", "false")
}

buildDefaultPropertyFile()

val kdocJar: TaskProvider<Jar> by
    tasks.registering(Jar::class) {
      archiveClassifier.set("kdoc")
      from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    }

val kdoc: Configuration by
    configurations.creating {
      isCanBeConsumed = true
      isCanBeResolved = false
    }

artifacts { add(kdoc.name, kdocJar) }

val javadocJar: TaskProvider<Jar> by
    tasks.registering(Jar::class) {
      archiveClassifier.set("javadoc")
      from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    }

java {
  withSourcesJar()
  withJavadocJar()
}

repositories {
  jcenter()
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
  jvmToolchain(11)
  jvm {
    withJava()
    testRuns["test"].executionTask.configure { useJUnitPlatform() }
  }
  js(IR) {
    binaries.executable()
    browser { commonWebpackConfig { cssSupport { enabled.set(true) } } }
  }
  sourceSets {
    val commonMain by getting {
      dependencies { implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0") }
    }
    val commonTest by getting { dependencies { implementation(kotlin("test")) } }
    val jvmMain by getting {
      dependencies {
        implementation("io.ktor:ktor-server-core:2.3.11")
        implementation("io.ktor:ktor-server-netty:2.3.11")
        implementation("io.ktor:ktor-server-websockets:2.3.11")
        implementation("io.ktor:ktor-server-html-builder-jvm:2.3.11")
        implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
        implementation("me.friwi:jcefmaven:135.0.20")
        implementation("dev.dirs:directories:26")
      }
    }
    val jvmTest by getting
    val jsMain by getting {
      dependencies {
        implementation(
            "org.jetbrains.kotlin-wrappers:kotlin-emotion-react:${wrappersVersion}-11.14.0")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react:${wrappersVersion}-19.1.0")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-core:${wrappersVersion}-19.1.0")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:${wrappersVersion}-19.1.0")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-use:${wrappersVersion}")
        implementation(npm("@dnd-kit/core", "6.2.0"))
        implementation(npm("react-zoom-pan-pinch", "3.6.1"))
      }
    }
    // val jsTest by getting
  }
}

application {
  mainClass.set("tools.aqua.bgw.main.MainKt")
  applicationDefaultJvmArgs =
      listOf(
          "--add-opens",
          "java.desktop/sun.awt=ALL-UNNAMED",
          "--add-opens",
          "java.desktop/java.awt.peer=ALL-UNNAMED")

  if (System.getProperty("os.name").contains("Mac")) {
    applicationDefaultJvmArgs =
        listOf(
            "--add-opens",
            "java.desktop/sun.awt=ALL-UNNAMED",
            "--add-opens",
            "java.desktop/java.awt.peer=ALL-UNNAMED",
            "--add-opens",
            "java.desktop/sun.lwawt=ALL-UNNAMED",
            "--add-opens",
            "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
  }
}

tasks.named<Copy>("jvmProcessResources") {
  val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
  from(jsBrowserDistribution)
}

// region - Cleanup JCEF helper processes
var globalTmpDir = ""

tasks.named<JavaExec>("run") {
  doFirst {
    val tmpDir = Files.createTempDirectory("bgw-")
    tmpDir.toFile().deleteOnExit()
    globalTmpDir = tmpDir.toString()
    jvmArgs = listOf("-DtmpDir=${tmpDir}")
  }
  dependsOn(tasks.named<Jar>("jvmJar"))
  classpath(tasks.named<Jar>("jvmJar"))
}

gradle.buildFinished {
  try {
    val applicationPIDs =
        file("$globalTmpDir/application.pid").readText().split(",").map { it.toLong() }.toSet()
    killJcefHelperProcesses(applicationPIDs)
    if (globalTmpDir.isNotEmpty()) {
      File(globalTmpDir).deleteRecursively()
    }
  } catch (_: Exception) {}
}

fun killJcefHelperProcesses(pids: Set<Long>) {
  pids.forEach { pid -> ProcessHandle.of(pid).ifPresent { it.destroy() } }
}
// endregion

tasks.named("publish") {
  doFirst {
    println("=============================================================================")
    println("Published bgw-gui: ${rootProject.version}")
    println("=============================================================================")
  }
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = "tools.aqua"
      artifactId = "bgw-gui"
      from(components["kotlin"])
    }
  }
}

spotless {
  kotlin {
    target(
        rootProject.fileTree("bgw-gui/src") {
          include("**/*.kt")
          exclude("**/Config.kt")
        })
    defaultFormat(rootProject)
  }
}

// Ignore yarn.lock mismatches
rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin::class.java) {
  rootProject.the<YarnRootExtension>().yarnLockMismatchReport = YarnLockMismatchReport.NONE
  rootProject.the<YarnRootExtension>().reportNewYarnLock = false
  rootProject.the<YarnRootExtension>().yarnLockAutoReplace = true
}
