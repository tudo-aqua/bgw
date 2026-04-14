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

import tools.aqua.GlobalMavenMetadataExtension
import tools.aqua.MavenMetadataExtension

plugins {
  id("tools.aqua.bgw.base-conventions")
  id("com.vanniktech.maven.publish")
}

val mavenMetadata = extensions.create<MavenMetadataExtension>("mavenMetadata")

mavenPublishing {
  publishToMavenCentral()
  signAllPublications()

  pom {
    name.set(mavenMetadata.name)
    description.set(mavenMetadata.description)

    val globalMetadata = rootProject.extensions.getByType<GlobalMavenMetadataExtension>()

    url.set(
        "https://github.com/${globalMetadata.githubProject.get().organization}/${globalMetadata.githubProject.get().project}")

    licenses {
      globalMetadata.licenses.get().forEach { licenseData ->
        license {
          name.set(licenseData.name)
          url.set(licenseData.url)
        }
      }
    }

    developers {
      globalMetadata.developers.get().forEach { dev ->
        developer {
          name.set(dev.name)
          email.set(dev.email)
        }
      }
    }

    scm {
      val github = globalMetadata.githubProject.get()
      connection.set("scm:git:git://github.com/${github.organization}/${github.project}.git")
      developerConnection.set(
          "scm:git:ssh://git@github.com/${github.organization}/${github.project}.git")
      url.set(
          "https://github.com/${github.organization}/${github.project}/tree/${github.mainBranch}")
    }
  }
}

tasks.named("publish") {
  doFirst {
    println("=============================================================================")
    println("Published ${project.name}: ${rootProject.version}")
    println("=============================================================================")
  }
}
