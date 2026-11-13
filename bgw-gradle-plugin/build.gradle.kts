/*
 * Copyright 2026 The BoardGameWork Authors
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

import tools.aqua.defaultFormat

plugins {
  id("tools.aqua.bgw.publish-conventions")
  `java-gradle-plugin`
}

mavenMetadata {
  name.set("BoardGameWork Gradle Plugin")
  description.set("Sets up Gradle projects that use BoardGameWork.")
}

java { toolchain { languageVersion = JavaLanguageVersion.of(11) } }

gradlePlugin {
  website = "https://tudo-aqua.github.io/bgw"
  vcsUrl = "https://github.com/tudo-aqua/bgw"
  plugins {
    create("bgw") {
      id = "tools.aqua.bgw"
      implementationClass = "tools.aqua.bgw.gradle.BgwPlugin"
      displayName = "BoardGameWork"
      description =
          "Adds the BoardGameWork dependencies and the JVM arguments required to run BoardGameWork applications."
    }
  }
}

dependencies {
  testImplementation(platform(libs.junit.bom))
  testImplementation(libs.bundles.test)
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.processResources {
  val version = project.version.toString()
  inputs.property("version", version)
  filesMatching("**/bgw-gradle-plugin.properties") { expand("version" to version) }
}

tasks.test { useJUnitPlatform() }

spotless { java { defaultFormat(rootProject) } }
