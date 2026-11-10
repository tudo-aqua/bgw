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

package tools.aqua

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.PublishToMavenLocal
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository

/**
 * Logs the coordinates of every publication when it is published (remote and local) and refuses to
 * publish versions that could not be determined from git.
 */
fun Project.announcePublications() {
  tasks.withType(PublishToMavenRepository::class.java).configureEach {
    doFirst { announce(publication, "to the ${repository.name} repository (${repository.url})") }
  }
  tasks.withType(PublishToMavenLocal::class.java).configureEach {
    doFirst { announce(publication, "to the local Maven repository") }
  }
}

private fun Project.announce(publication: MavenPublication, target: String) {
  val coordinates = "${publication.groupId}:${publication.artifactId}:${publication.version}"
  check(!isUnresolvedVersion(publication.version)) {
    "Refusing to publish $coordinates: the version could not be determined from git."
  }
  logger.lifecycle("Publishing $coordinates $target")
}
