/*
 * Copyright 2021-2026 The BoardGameWork Authors
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

import com.diffplug.gradle.spotless.KotlinExtension
import com.diffplug.gradle.spotless.KotlinGradleExtension
import org.gradle.kotlin.dsl.*
import tools.aqua.GlobalMavenMetadataExtension
import tools.aqua.defaultFormat
import tools.aqua.gitCliVersion
import tools.aqua.isUnresolvedVersion

plugins {
  id("tools.aqua.bgw.base-conventions")

  id("com.diffplug.spotless")

  // plugins that must be applied to the root project
  id("me.qoomon.git-versioning")
  id("org.jetbrains.kotlinx.kover")

  signing
}

version = "0.0.0-SNAPSHOT"

val mavenMetadata = extensions.create<GlobalMavenMetadataExtension>("mavenMetadata")

gitVersioning.apply {
  describeTagPattern = "v(?<version>.*)"
  refs {
    considerTagsOnBranches = true
    tag("v(?<version>.*)") { version = "\${ref.version}" }
    branch("((?!main).*|main.+|)") { // everything but main
      version =
          "\${describe.tag.version}-\${ref.slug}-\${describe.distance}-\${commit.short}-SNAPSHOT"
    }
    branch("main") {
      version = "\${describe.tag.version}-\${describe.distance}-\${commit.short}-SNAPSHOT"
    }
  }
}

if (isUnresolvedVersion(version.toString())) {
  val fallback = gitCliVersion()
  if (fallback != null) {
    logger.info("git-versioning could not read the repository, using git CLI version $fallback")
    allprojects { version = fallback }
  } else {
    logger.warn("The project version could not be determined from git: $version")
  }
}

val printVersion by tasks.registering { doFirst { logger.error(version.toString()) } }

spotless {
  format("kotlinBuildSrc", KotlinExtension::class.java) {
    target("buildSrc/src/*/kotlin/**/*.kt")
    defaultFormat(rootProject)
  }
  format("kotlinGradleBuildSrc", KotlinGradleExtension::class.java) {
    target("buildSrc/*.gradle.kts", "buildSrc/src/*/kotlin/**/*.gradle.kts")
    defaultFormat(rootProject)
  }
}

kover { merge {} }

signing { useGpgCmd() }
