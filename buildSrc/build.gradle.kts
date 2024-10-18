/*
 * Copyright 2021-2024 The BoardGameWork Authors
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

plugins { `kotlin-dsl` }

repositories {
  gradlePluginPortal()
  mavenCentral()
  maven {
    url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    name = "ktor-eap"
  }
}

dependencies {
  implementation(libs.bundles.gradle.kotlin.full)
  implementation(libs.bundles.ktor.netty)

  implementation(libs.commons.compress)
  implementation(libs.dockerClient)

  implementation(libs.gradle.bmVersions)
  implementation(libs.gradle.dependencyManagement)
  implementation(libs.gradle.detekt)
  implementation(libs.gradle.gitVersioning)
  implementation(libs.gradle.kover)
  implementation(libs.gradle.nexusPublish)
  implementation(libs.gradle.spotless)
  implementation(libs.gradle.spring.boot)
  implementation(libs.gradle.taskTree)
  implementation(libs.gradle.vaadin)

  implementation(
      "org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin:2.0.0")
  implementation(
      "org.jetbrains.kotlin.plugin.serialization:org.jetbrains.kotlin.plugin.serialization.gradle.plugin:2.0.0")

  // black magic from https://github.com/gradle/gradle/issues/15383
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
