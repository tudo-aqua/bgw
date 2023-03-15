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

import org.gradle.api.publish.plugins.PublishingPlugin.PUBLISH_TASK_GROUP
import tools.aqua.GlobalMavenMetadataExtension
import tools.aqua.MavenMetadataExtension
import tools.aqua.developer
import tools.aqua.github
import tools.aqua.license

plugins {
  id("tools.aqua.bgw.base-conventions")

  `maven-publish`
  signing
}

val mavenMetadata = extensions.create<MavenMetadataExtension>("mavenMetadata")

publishing {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])

      pom {
        name.set(mavenMetadata.name)
        description.set(mavenMetadata.description)

        val globalMetadata = rootProject.extensions.getByType<GlobalMavenMetadataExtension>()

        developers { globalMetadata.developers.get().forEach { developer(it.name, it.email) } }

        globalMetadata.githubProject.get().let {
          github(it.organization, it.project, it.mainBranch)
        }

        licenses { globalMetadata.licenses.get().forEach { license(it.name, it.url) } }
      }
    }
  }
}

signing {
  setRequired { gradle.taskGraph.allTasks.any { it.group == PUBLISH_TASK_GROUP } }
  useGpgCmd()
  sign(publishing.publications["maven"])
}
