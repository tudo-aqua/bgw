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

import com.diffplug.gradle.spotless.KotlinExtension
import com.diffplug.gradle.spotless.KotlinGradleExtension
import java.util.regex.Pattern
import tools.aqua.defaultFormat

plugins {
  id("tools.aqua.bgw.base-conventions")

  id("com.diffplug.spotless")

  // plugins that must be applied to the root project
  id("io.github.gradle-nexus.publish-plugin")
  id("me.qoomon.git-versioning")
  id("org.jetbrains.kotlinx.kover")
}

group = "tools.aqua"

version = "0.0.0-SNAPSHOT"

gitVersioning.apply {
  describeTagPattern = Pattern.compile("v(?<version>.*)")
  refs {
    considerTagsOnBranches = true
    branch("((?!main).*|main.+|)") { // everything but main
      version = "\${describe.tag.version}-\${ref.slug}-\${describe.distance}-\${commit.short}-SNAPSHOT"
    }
    branch("main") {
      version = "\${describe.tag.version}-\${describe.distance}-\${commit.short}-SNAPSHOT"
    }
    tag("v(?<version>.*)") { version = "\${ref.version}" }
  }
}

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

nexusPublishing { repositories { sonatype() } }
