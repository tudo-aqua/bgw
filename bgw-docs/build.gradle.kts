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
  nodeProjectDir.set(file("parser"))
}

tasks.register("generateRuntime") {
  this.group = "documentation"
  doFirst {
    println("Generated runtime with sockets disabled.")
    project.extra["useSockets"] = "false"
  }
  doLast { buildPropertyFile() }
  finalizedBy(
      ":bgw-gui:compileProductionExecutableKotlinJs",
      ":bgw-gui:compileTestDevelopmentExecutableKotlinJs",
      ":bgw-gui:build")
}

tasks.register("generateSamplesConfig") {
  this.group = "documentation"
  doFirst { project.extra["generateSamples"] = "true" }
  doLast { buildPropertyFile() }
  finalizedBy(":bgw-gui:build")
}

tasks.register("generateSamples") {
  this.group = "documentation"
  dependsOn("generateSamplesConfig")
  mustRunAfter(":bgw-gui:build")
  finalizedBy(":bgw-gui:run")
}

tasks.register("buildAndCopySamples") {
  mustRunAfter("buildAndCopyRuntime")
  this.group = "documentation"
  dependsOn("generateSamples")
  mustRunAfter(":bgw-gui:run")
  doLast {
    val sourceDir = project(":bgw-gui").buildDir.resolve("examples/bgwSamples.json")
    val destinationDir = projectDir.resolve("website/public/bgw")
    println("Copying files from $sourceDir to $destinationDir")
    copy {
      from(sourceDir)
      into(destinationDir)
    }
  }
}

tasks.register("buildAndCopyRuntime") {
  dependsOn("generateRuntime")
  this.group = "documentation"
  mustRunAfter(":bgw-gui:build")
  doLast {
    val sourceDir = project(":bgw-gui").buildDir.resolve("dist/js/productionExecutable/bgw-gui.js")
    val destinationDir = projectDir.resolve("website/public/bgw")
    println("Copying files from $sourceDir to $destinationDir")
    copy {
      from(sourceDir)
      into(destinationDir)
    }
  }
}

tasks.register("buildAndCopyDokkaHtml") {
  dependsOn(":bgw-gui:dokkaHtmlPartial")
  this.group = "documentation"
  doLast {
    val sourceDir = project(":bgw-gui").buildDir.resolve("dokka/htmlPartial")
    val destinationDir = projectDir.resolve("parser/example/htmlPartial")
    println("Copying files from $sourceDir to $destinationDir")
    copy {
      from(sourceDir)
      into(destinationDir)
    }
  }
}

tasks.register("dokkaJson", YarnTask::class) {
  dependsOn("npmInstall", "buildAndCopyDokkaHtml")
  this.group = "documentation"
  doFirst { projectDir.resolve("parser/output").mkdir() }
  args.set(listOf("run", "build"))
}

tasks.register("buildAndCopyDokkaJson") {
  mustRunAfter("buildAndCopySamples")
  dependsOn("dokkaJson")
  this.group = "documentation"
  doLast {
    val sourceDir = projectDir.resolve("parser/output/cleanedStructure.json")
    val destinationDir = projectDir.resolve("website/public/bgw")
    println("Copying files from $sourceDir to $destinationDir")
    copy {
      from(sourceDir)
      into(destinationDir)
    }
  }
}

tasks.register("docsBuild") {
  this.group = "documentation"
}

tasks.register("docsBuildRuntime") {
  dependsOn("buildAndCopyRuntime")
  this.group = "documentation"
}

tasks.register("docsBuildSamples") {
  dependsOn("buildAndCopySamples")
  this.group = "documentation"
}

tasks.register("docsBuildDokkaJson") {
  dependsOn("buildAndCopyDokkaJson")
  this.group = "documentation"
}

tasks.register("docsServe", YarnTask::class) {
  dependsOn("docsBuild")
  this.group = "documentation"
  args.set(listOf("run", "dev"))
}

val wrappersVersion = "-pre.831"
val USE_SOCKET_DEFAULT = true
val GENERATE_SAMPLES_DEFAULT = false

fun buildPropertyFile() {
  val propertyFile = "Config.kt"
  rootDir.resolve("bgw-gui/src/jsMain/kotlin/tools/aqua/bgw/${propertyFile}").apply {
    println("Generate properties into $absolutePath")
    parentFile.mkdirs()
    writeText(generateProperties())
  }

  rootDir.resolve("bgw-gui/src/jvmMain/kotlin/tools/aqua/bgw/application/${propertyFile}").apply {
    println("Generate properties into $absolutePath")
    parentFile.mkdirs()
    writeText(generateProperties("application"))
  }

  val useSockets = project.findProperty("useSockets")?.toString()?.toBoolean() ?: USE_SOCKET_DEFAULT
  val generateSamples =
      project.findProperty("generateSamples")?.toString()?.toBoolean() ?: GENERATE_SAMPLES_DEFAULT

  println("useSockets: $useSockets")
  println("generateSamples: $generateSamples")
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
