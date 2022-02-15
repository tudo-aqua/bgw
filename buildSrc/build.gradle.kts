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
  implementation("com.diffplug.spotless", "spotless-plugin-gradle", libs.versions.spotless.get())
  implementation("com.dorongold.plugins", "task-tree", libs.versions.tasktree.get())
  implementation("com.github.ben-manes", "gradle-versions-plugin", libs.versions.versions.get())
  implementation("com.vaadin", "vaadin-gradle-plugin", libs.versions.vaadinPlugin.get())
  implementation("de.gesellix", "docker-client", libs.versions.dockerClient.get())
  implementation("io.github.gradle-nexus", "publish-plugin", libs.versions.nexusPublish.get())
  implementation("io.gitlab.arturbosch.detekt", "detekt-gradle-plugin", libs.versions.detekt.get())
  implementation("io.ktor", "ktor-server-core", libs.versions.ktor.get())
  implementation("io.ktor", "ktor-server-netty", libs.versions.ktor.get())
  implementation(
      "io.spring.gradle", "dependency-management-plugin", libs.versions.dependencyManagement.get())
  implementation("me.qoomon", "gradle-git-versioning-plugin", libs.versions.gitVersioning.get())
  implementation("org.apache.commons", "commons-compress", libs.versions.commonsCompress.get())
  implementation("org.jetbrains.dokka", "dokka-gradle-plugin", libs.versions.kotlin.get())
  implementation("org.jetbrains.kotlin", "kotlin-allopen", libs.versions.kotlin.get())
  implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", libs.versions.kotlin.get())
  implementation("org.jetbrains.kotlin", "kotlin-noarg", libs.versions.kotlin.get())
  implementation("org.jetbrains.kotlinx", "kover", libs.versions.kover.get())
  implementation(
      "org.springframework.boot", "spring-boot-gradle-plugin", libs.versions.spring.get())

  // black magic from https://github.com/gradle/gradle/issues/15383
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
