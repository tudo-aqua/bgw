/*
 * Copyright 2021-2026 The BoardGameWork Authors
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

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import tools.aqua.defaultFormat

plugins {
  id("tools.aqua.bgw.base-conventions")

  kotlin("jvm")
  id("com.diffplug.spotless")
  id("dev.detekt")
  id("org.jetbrains.dokka")
  id("org.jetbrains.kotlinx.kover")
}

detekt {
  ignoreFailures = true
  basePath = file(rootProject.projectDir.absolutePath)
  config = files(rootProject.file("contrib/detekt-rules.yml"))
}

spotless { kotlin { defaultFormat(rootProject) } }

java {}

dokka {}

// black magic from https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

dependencies {
  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.bundles.test)
}

tasks.test {
  useJUnitPlatform()
  testLogging { events(FAILED, PASSED, SKIPPED) }
}

kotlin {
  jvmToolchain(11)
  compilerOptions {
    jvmTarget = JvmTarget.JVM_11
    // allWarningsAsErrors = true
    freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
  }
}
