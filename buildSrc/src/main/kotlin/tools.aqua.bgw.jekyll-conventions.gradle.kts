/*
 * Copyright 2021-2023 The BoardGameWork Authors
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
import tools.aqua.JekyllBuildTask
import tools.aqua.KtorServeTask

plugins {
  id("tools.aqua.bgw.base-conventions")

  id("com.diffplug.spotless")
}

val includedKDoc: Configuration by
    configurations.creating {
      isCanBeConsumed = false
      isCanBeResolved = true
      isVisible = false
    }

val libs = the<LibrariesForLibs>()

tasks {
  val jekyllBuild by
      registering(JekyllBuildTask::class) {
        dependsOn(includedKDoc)
        jekyllContainerVersion.set(libs.versions.jekyllDocker)
        includedKDoc.resolvedConfiguration.resolvedArtifacts.forEach {
          includedResources.put("${it.name}-kdoc", it.file)
        }
      }

  @Suppress("UNUSED_VARIABLE")
  val jekyllServe by
      registering(KtorServeTask::class) {
        serverRoot.set(jekyllBuild.flatMap { it.output })
        baseURL.set("/bgw")
      }
}
