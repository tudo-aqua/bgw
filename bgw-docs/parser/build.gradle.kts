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

val documentedModules =
    mapOf(
        ":bgw-gui" to "package-list",
        ":bgw-net:bgw-net-client" to "package-list-client",
        ":bgw-net:bgw-net-common" to "package-list-common")

// Mirrors the current Dokka output, so pages of removed declarations do not remain in the docs.
tasks.register<Sync>("buildAndCopyDokkaHtml") {
  group = "build"
  into(layout.projectDirectory.dir("example/htmlPartial"))
  // The modules share the files of the page frame (index.html, scripts, styles). The last one wins.
  duplicatesStrategy = DuplicatesStrategy.INCLUDE
  documentedModules.forEach { (path, packageList) ->
    dependsOn("$path:dokkaGenerateModuleHtml")
    from(project(path).layout.buildDirectory.dir("dokka-module/html/module")) {
      rename("package-list", packageList)
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
