/*
 * Copyright 2025 The BoardGameWork Authors
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
import io.ktor.http.*

/*
 * Copyright 2025 The BoardGameWork Authors
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

plugins {
  id("tools.aqua.bgw.base-conventions")
  id("com.github.node-gradle.node") version "7.1.0"
}

node {
  version.set("20.17.0")
  download.set(true)
  nodeProjectDir.set(projectDir.resolve("website"))
}

tasks.register<GradleBuild>("docsBuild") {
  group = "documentation"
  tasks =
      listOf(
          ":bgw-docs:website:buildAndCopyRuntimeTask",
          ":bgw-docs:parser:buildAndCopySamplesTask",
          ":bgw-docs:parser:buildAndCopyDokkaJson",
          "yarnPreDeploy")
}

tasks.register("yarnInstall", YarnTask::class) {
  group = "yarn"
  args.set(listOf("install"))
}

tasks.register("yarnPreDeploy", YarnTask::class) {
  dependsOn("yarnInstall")
  group = "yarn"
  args.set(listOf("predeploy"))
}

tasks.register("docsServe", YarnTask::class) {
  dependsOn("docsBuild", "yarnInstall")
  this.group = "documentation"
  args.set(listOf("run", "dev"))
}

val wrappersVersion = "-pre.831"
val USE_SOCKET_DEFAULT = true
val GENERATE_SAMPLES_DEFAULT = false

fun buildPropertyFile() {
  val propertyFile = "Config.kt"
  rootDir.resolve("bgw-gui/src/jsMain/kotlin/tools/aqua/bgw/${propertyFile}").apply {
    parentFile.mkdirs()
    writeText(generateProperties())
  }

  rootDir.resolve("bgw-gui/src/jvmMain/kotlin/tools/aqua/bgw/application/${propertyFile}").apply {
    parentFile.mkdirs()
    writeText(generateProperties("application"))
  }
}

fun generateProperties(suffix: String = ""): String {
  val useSockets = project.findProperty("useSockets")?.toString()?.toBoolean() ?: USE_SOCKET_DEFAULT
  val generateSamples =
      project.findProperty("generateSamples")?.toString()?.toBoolean() ?: GENERATE_SAMPLES_DEFAULT
  return """
    package tools.aqua.bgw${if (suffix.isNotEmpty()) ".$suffix" else ""}

    internal object Config {
        val USE_SOCKETS = $useSockets
        val GENERATE_SAMPLES = $generateSamples
        val BGW_VERSION = "${rootProject.version}"
    }
""".trimIndent()
}
