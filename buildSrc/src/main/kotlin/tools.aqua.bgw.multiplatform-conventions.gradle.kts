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

import gradle.kotlin.dsl.accessors._1d4b2bd2040b92c2213b59b79754c7b4.dokkaHtml
import gradle.kotlin.dsl.accessors._1d4b2bd2040b92c2213b59b79754c7b4.dokkaJavadoc
import gradle.kotlin.dsl.accessors._1d4b2bd2040b92c2213b59b79754c7b4.java
import java.lang.ProcessHandle
import java.lang.management.ManagementFactory
import java.lang.management.OperatingSystemMXBean
import java.util.stream.Collectors
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

fun buildPropertyFile() {
  rootDir.resolve("bgw-gui/src/jsMain/kotlin/tools/aqua/bgw/${propertyFile}").apply {
    println("Generate properties into $absolutePath")
    parentFile.mkdirs()
    writeText(generateProperties())
  }
}

fun generateProperties(prefix: String = "") =
    """
    package tools.aqua.bgw

    object Config {
        val USE_SOCKETS = ${useSockets ?: "true"}
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
      dependencies { implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0") }
    }
    val commonTest by getting { dependencies { implementation(kotlin("test")) } }
    val jvmMain by getting {
      dependencies {
        implementation("io.ktor:ktor-server-core:2.3.11")
        implementation("io.ktor:ktor-server-netty:2.3.11")
        implementation("io.ktor:ktor-server-websockets:2.3.11")
        implementation("io.ktor:ktor-server-html-builder-jvm:2.3.11")
        implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
        implementation("me.friwi:jcefmaven:122.1.10")
      }
    }
    val jvmTest by getting
    val jsMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.3.1-pre.828")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-core:18.3.1-pre.828")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.3.1-pre.828")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.13.3-pre.828")
        implementation(npm("@dnd-kit/core", "6.1.0"))
      }
    }
    val jsTest by getting
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

var initialHelperPIDs = setOf<Long>()

// Cleanup task that depends on tracking `jcef_helper.exe` processes
val cleanupJcefHelper by
    tasks.registering {
      group = "application"
      description = "Cleans up orphaned jcef_helper processes after the run task"

      doLast {
        // After 'run' completes, find new `jcef_helper.exe` processes and terminate them
        println("Initial jcef_helper PIDs: $initialHelperPIDs")
        val currentHelperPIDs = getCurrentJcefHelperPIDs()
        println("Current jcef_helper PIDs: $currentHelperPIDs")
        val newHelperPIDs = currentHelperPIDs - initialHelperPIDs
        println("New jcef_helper PIDs: $newHelperPIDs")

        // Kill only new helper processes started during the run
        killJcefHelperProcesses(newHelperPIDs)
      }
    }

tasks.named<JavaExec>("run") {
  doFirst {
    initialHelperPIDs = getCurrentJcefHelperPIDs()
    println("Initial jcef_helper PIDs: $initialHelperPIDs")
  }

  dependsOn(tasks.named<Jar>("jvmJar"))
  classpath(tasks.named<Jar>("jvmJar"))
}

gradle.buildFinished {
  println("Build finished, cleaning up jcef_helper processes")
  tasks.named("cleanupJcefHelper").get().actions.forEach {
    it.execute(tasks.named("cleanupJcefHelper").get())
  }
}

// Helper function to get current jcef_helper process IDs
fun getCurrentJcefHelperPIDs(): Set<Long> {
  return ProcessHandle.allProcesses()
      .filter { it.info().command().orElse("").contains("jcef_helper") }
      .map { it.pid() }
      .collect(Collectors.toSet())
}

// Function to get the parent process ID of a given process
fun getParentProcessId(pid: Long): Long? {
  val osBean = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean
  val processHandle = ProcessHandle.of(pid)
  return processHandle.orElse(null)?.parent()?.orElse(null)?.pid()
}

// Function to kill JCEF helper processes
fun killJcefHelperProcesses(pids: Set<Long>) {
  val currentProcessId = ProcessHandle.current().pid()
  pids.forEach { pid ->
    val parentPid = getParentProcessId(pid)
    println("Parent process ID of $pid is $parentPid")
    if (parentPid == currentProcessId) {
      println("Killing process $pid which is a child of the current process $currentProcessId")
      Runtime.getRuntime().exec("kill -9 $pid")
    } else {
      println("Skipping process $pid as it is not a child of the current process $currentProcessId")
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
