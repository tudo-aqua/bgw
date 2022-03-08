/*
 * Copyright 2021-2022 The BoardGameWork Authors
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
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import tools.aqua.defaultFormat

plugins {
  id("tools.aqua.bgw.base-conventions")

  kotlin("jvm")
  id("com.diffplug.spotless")
  id("io.gitlab.arturbosch.detekt")
  id("org.jetbrains.dokka")
}

detekt {
  basePath = rootProject.projectDir.absolutePath
  config = files(rootProject.file("contrib/detekt-rules.yml"))
}

spotless { kotlin { defaultFormat(rootProject) } }

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

// black magic from https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

dependencies {
  dokkaGfmPlugin(libs.dokka.javadoc)

  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.bundles.test)
}

tasks.test {
  useJUnitPlatform()
  testLogging { events(FAILED, PASSED, SKIPPED) }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "11"
    //allWarningsAsErrors = true
    freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
  }
}
