/*
 * Copyright 2024 The BoardGameWork Authors
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

import com.diffplug.gradle.spotless.KotlinExtension
import gradle.kotlin.dsl.accessors._1d4b2bd2040b92c2213b59b79754c7b4.dokkaHtml
import gradle.kotlin.dsl.accessors._1d4b2bd2040b92c2213b59b79754c7b4.dokkaJavadoc
import gradle.kotlin.dsl.accessors._1d4b2bd2040b92c2213b59b79754c7b4.java
import gradle.kotlin.dsl.accessors._1d4b2bd2040b92c2213b59b79754c7b4.spotless
import java.lang.ProcessHandle
import java.lang.management.ManagementFactory
import java.lang.management.OperatingSystemMXBean
import java.util.stream.Collectors
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tools.aqua.defaultFormat

plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  application
  `maven-publish`
  id("io.gitlab.arturbosch.detekt")
  id("org.jetbrains.dokka")
}

val useSockets: String? = project.findProperty("useSockets")?.toString()
val propertyFile = "Config.kt"
val wrappersVersion = "-pre.831"

fun buildPropertyFile() {
  rootDir.resolve("bgw-gui/src/jsMain/kotlin/tools/aqua/bgw/${propertyFile}").apply {
    println("Generate properties into $absolutePath")
    parentFile.mkdirs()
    writeText(generateProperties())
  }

    rootDir.resolve("bgw-gui/src/jvmMain/kotlin/tools/aqua/bgw/application/${propertyFile}").apply {
        println("Generate properties into $absolutePath")
        parentFile.mkdirs()
        writeText(generateProperties("application"))
    }
}

fun generateProperties(suffix: String = "") =
    """
    package tools.aqua.bgw${if (suffix.isNotEmpty()) ".$suffix" else ""}

    internal object Config {
        val USE_SOCKETS = ${useSockets ?: "true"}
        val BGW_VERSION = "${rootProject.version}"
    }
""".trimIndent()

tasks.withType<KotlinCompile> { buildPropertyFile() }

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
    browser {
      commonWebpackConfig {
        cssSupport { enabled.set(true) }
        mode = org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.Mode.DEVELOPMENT
      }
    }
  }
  sourceSets {
    val commonMain by getting {
      dependencies { implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3") }
    }
    val commonTest by getting { dependencies { implementation(kotlin("test")) } }
    val jvmMain by getting {
      dependencies {
        implementation("io.ktor:ktor-server-core:2.3.11")
        implementation("io.ktor:ktor-server-netty:2.3.11")
        implementation("io.ktor:ktor-server-websockets:2.3.11")
        implementation("io.ktor:ktor-server-html-builder-jvm:2.3.11")
        implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
        implementation("me.friwi:jcefmaven:127.3.1")
        implementation("dev.dirs:directories:26")
      }
    }
    val jvmTest by getting
    val jsMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.3.1${wrappersVersion}")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-core:18.3.1${wrappersVersion}")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.3.1${wrappersVersion}")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.13.3${wrappersVersion}")
        implementation(npm("@dnd-kit/core", "6.2.0"))
        implementation(npm("react-zoom-pan-pinch", "3.6.1"))
      }
    }
    //val jsTest by getting
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

tasks.named<JavaExec>("run") {
  dependsOn(tasks.named<Jar>("jvmJar"))
  classpath(tasks.named<Jar>("jvmJar"))
}

gradle.buildFinished {
    try {
        val applicationPIDs = file("build/application.pid").readText().split(",").map { it.toLong() }.toSet()
        killJcefHelperProcesses(applicationPIDs)
    } catch (e: Exception) {}
}

tasks.named("publish") {
    doFirst {
        println("=============================================================================")
        println("Published bgw-gui: ${rootProject.version}")
        println("=============================================================================")
    }
}

// Function to kill JCEF helper processes
fun killJcefHelperProcesses(pids: Set<Long>) {
  pids.forEach { pid ->
      ProcessHandle.of(pid).ifPresent {
          it.destroy()
      }
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

//spotless {
//    kotlin {
//        target(rootProject.fileTree("bgw-gui/src") {
//            include("**/*.kt")
//            exclude("**/Config.kt")
//        })
//        defaultFormat(rootProject)
//    }
//}

// Ignore yarn.lock mismatches
rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin::class.java) {
    rootProject.the<YarnRootExtension>().yarnLockMismatchReport =
        YarnLockMismatchReport.NONE
    rootProject.the<YarnRootExtension>().reportNewYarnLock = false
    rootProject.the<YarnRootExtension>().yarnLockAutoReplace = true
}