/*
 * Copyright 2024-2026 The BoardGameWork Authors
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

import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension
import tools.aqua.defaultFormat

plugins {
  id("tools.aqua.bgw.publish-conventions")

  kotlin("multiplatform")
  kotlin("plugin.serialization")

  id("dev.detekt")
  id("org.jetbrains.dokka")
  id("org.jetbrains.kotlinx.kover")
  id("com.diffplug.spotless")
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

/** The project version escaped for use inside a Kotlin string literal. */
fun escapedVersion(): String =
    rootProject.version.toString().replace("\\", "\\\\").replace("\"", "\\\"").replace("$", "\\$")

fun generateDefaultProperties(suffix: String = "") =
    """
    package tools.aqua.bgw${if (suffix.isNotEmpty()) ".$suffix" else ""}

    internal object Config {
        val USE_SOCKETS = true
        val BGW_VERSION = "${escapedVersion()}"
    }
"""
        .trimIndent()

if (!project.extra.has("useSockets")) {
  project.extra.set("useSockets", "true")
}

buildDefaultPropertyFile()

kotlin {
  jvmToolchain(11)
  jvm { testRuns["test"].executionTask.configure { useJUnitPlatform() } }
  js(IR) {
    binaries.executable()
    browser {
      commonWebpackConfig {
        cssSupport { enabled.set(true) }
        mode =
            if (providers.gradleProperty("bgwDevBundle").orNull.toBoolean())
                KotlinWebpackConfig.Mode.DEVELOPMENT
            else KotlinWebpackConfig.Mode.PRODUCTION
      }
    }
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
        implementation("me.friwi:jcefmaven:146.0.10")
        implementation("dev.dirs:directories:26")
      }
    }
    val jvmTest by getting {
      dependencies {
        implementation("com.mohamedrejeb.ksoup:ksoup-html:0.4.1")
        implementation("org.seleniumhq.selenium:selenium-api:4.34.0")
        implementation("org.seleniumhq.selenium:selenium-java:4.34.0")
        implementation("org.seleniumhq.selenium:selenium-chrome-driver:4.34.0")
        implementation("io.github.bonigarcia:webdrivermanager:6.1.0")
      }
    }
    val jsMain by getting {
      dependencies {
        implementation(
            "org.jetbrains.kotlin-wrappers:kotlin-emotion-react:${wrappersVersion}-11.14.0")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react:${wrappersVersion}-19.1.0")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-core:${wrappersVersion}-19.1.0")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:${wrappersVersion}-19.1.0")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-use:${wrappersVersion}")
        implementation(npm("@dnd-kit/core", "6.3.1"))
        implementation(npm("react-zoom-pan-pinch", "3.6.1"))
        implementation(npm("animejs", "4.2.2"))
      }
    }
  }
}

tasks.named<Copy>("jvmProcessResources") {
  val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
  from(jsBrowserDistribution)
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
