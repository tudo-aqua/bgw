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
  nodeProjectDir.set(projectDir.resolve("website"))
}

// The tasks that write the generated data into website/public, which the website build copies.
val generatedDataTasks =
    listOf(
        ":bgw-docs:website:buildAndCopyRuntimeTask",
        ":bgw-docs:parser:buildAndCopySamplesTask",
        ":bgw-docs:parser:buildAndCopyDokkaJson")

tasks.register<GradleBuild>("docsBuild") {
  group = "documentation"
  tasks = generatedDataTasks + "yarnPreDeploy"
}

tasks.register("yarnInstall", YarnTask::class) {
  group = "yarn"
  args.set(listOf("install"))
}

tasks.register("yarnPreDeploy", YarnTask::class) {
  dependsOn("yarnInstall")
  mustRunAfter(generatedDataTasks)
  group = "yarn"
  args.set(listOf("predeploy"))
}

tasks.register("docsServe", YarnTask::class) {
  dependsOn("docsBuild", "yarnInstall")
  this.group = "documentation"
  args.set(listOf("run", "dev"))
}
