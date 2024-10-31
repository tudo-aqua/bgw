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
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.3.1-pre.758")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.3.1-pre.758")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.11.4-pre.758")
      }
    }
    val jsTest by getting
  }
}

application { mainClass.set("tools.aqua.bgw.main.MainKt") }

tasks.named<Copy>("jvmProcessResources") {
  val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
  from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
  dependsOn(tasks.named<Jar>("jvmJar"))
  classpath(tasks.named<Jar>("jvmJar"))
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
