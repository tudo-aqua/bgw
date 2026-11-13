/*
 * Copyright 2025-2026 The BoardGameWork Authors
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

import com.github.gradle.node.yarn.task.YarnTask

plugins {
  id("tools.aqua.bgw.base-conventions")
  id("com.github.node-gradle.node") version "7.1.0"
}

node {
  version.set("20.17.0")
  download.set(true)
  nodeProjectDir.set(projectDir)
}

tasks.register<Copy>("buildAndCopySamples") {
  group = "build"
  from(project(":bgw-gui").tasks.named("generateDocsSamples"))
  into(projectDir.resolve("../website/public/bgw"))
}

tasks.register("buildAndCopyDokkaHtml") {
  dependsOn(
      rootProject.project(":bgw-gui").tasks.named("dokkaGenerateModuleHtml"),
      rootProject.project(":bgw-net:bgw-net-client").tasks.named("dokkaGenerateModuleHtml"),
      rootProject.project(":bgw-net:bgw-net-common").tasks.named("dokkaGenerateModuleHtml"))
  this.group = "build"
  doLast {
    val sourceDir = project(":bgw-gui").buildDir.resolve("dokka-module/html/module")
    val destinationDir = projectDir.resolve("example/htmlPartial")
    println("Copying files from $sourceDir to $destinationDir")
    copy {
      from(sourceDir)
      into(destinationDir)
    }

    val sourceDir2 =
        rootProject.project(":bgw-net:bgw-net-client").buildDir.resolve("dokka-module/html/module")
    val destinationDir2 = projectDir.resolve("example/htmlPartial")
    println("Copying files from $sourceDir2 to $destinationDir2")
    copy {
      from(sourceDir2)
      into(destinationDir2)
      rename("package-list", "package-list-client")
    }

    val sourceDir3 =
        rootProject.project(":bgw-net:bgw-net-common").buildDir.resolve("dokka-module/html/module")
    val destinationDir3 = projectDir.resolve("example/htmlPartial")
    println("Copying files from $sourceDir3 to $destinationDir3")
    copy {
      from(sourceDir3)
      into(destinationDir3)
      rename("package-list", "package-list-common")
    }
  }
}

tasks.register("dokkaJson", YarnTask::class) {
  dependsOn("npmInstall", "buildAndCopyDokkaHtml")
  this.group = "build"
  doFirst { projectDir.resolve("output").mkdir() }
  args.set(listOf("run", "build"))
}

tasks.register("buildAndCopyDokkaJson") {
  dependsOn("dokkaJson")
  this.group = "build"
  doLast {
    val sourceDir = projectDir.resolve("output/cleanedStructure.json")
    val destinationDir = projectDir.resolve("../website/public/bgw")
    println("Copying files from $sourceDir to $destinationDir")
    copy {
      from(sourceDir)
      into(destinationDir)
    }
  }
}

tasks.register<GradleBuild>("buildAndCopySamplesTask") {
  this.group = "build"
  tasks = listOf("buildAndCopySamples")
}
