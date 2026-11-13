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

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJvmCompilation

plugins { id("tools.aqua.bgw.multiplatform-conventions") }

mavenMetadata {
  name.set("BoardGameWork GUI Library")
  description.set("A framework for board game applications.")
}

// The scenes previewed in the documentation. They use internal API to serialize the scenes and are
// therefore compiled against the main compilation, but they are not part of the published library.
val samples: KotlinJvmCompilation =
    kotlin.jvm().compilations.create("samples") {
      associateWith(kotlin.jvm().compilations.getByName("main"))
    }

val samplesOutput = layout.buildDirectory.file("examples/bgwSamples.json")

tasks.register<JavaExec>("generateDocsSamples") {
  group = "documentation"
  description = "Serializes the documentation sample scenes to JSON."
  classpath(samples.output.allOutputs, samples.runtimeDependencyFiles)
  mainClass = "tools.aqua.bgw.main.MainKt"
  args(samplesOutput.get().asFile.absolutePath)
  outputs.file(samplesOutput)
}
