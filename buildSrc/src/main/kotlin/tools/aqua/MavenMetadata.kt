/*
 * Copyright 2022-2023 The BoardGameWork Authors
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

import javax.inject.Inject
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property

/**
 * This extension stores project-specific Maven metadata (name, description) in a type-safe way. The
 * values can then be retrieved to fill the POM configuration without forcing build files to contain
 * publishing logic.
 *
 * @param objects a Gradle object factory.
 */
open class MavenMetadataExtension @Inject constructor(objects: ObjectFactory) {
  /**
   * The project name for the POM.
   * @see MavenPom.getName
   */
  @Input val name: Property<String> = objects.property()

  /**
   * The project description for the POM.
   * @see MavenPom.getDescription
   */
  @Input val description: Property<String> = objects.property()
}

/**
 * This extension stores global Maven metadata (developers, GitHub, license) in a type-safe way. The
 * values can then be retrieved to fill the POM configuration without forcing build files to contain
 * publishing logic.
 *
 * @param objects a Gradle object factory.
 */
open class GlobalMavenMetadataExtension @Inject constructor(objects: ObjectFactory) {
  /**
   * A developer's identity.
   * @param name the developer's name.
   * @param email the developer's preferred email address.
   */
  data class Developer(val name: String, val email: String)

  /**
   * A GitHub project's coordinates.
   * @param organization the organisation / user the project resides in.
   * @param project the project name.
   * @param mainBranch the main branch (usually "main" or similar).
   */
  data class GithubProject(
      val organization: String,
      val project: String,
      val mainBranch: String = "main"
  )

  /**
   * A OSS license.
   * @param name the licenses full name.
   * @param url a URL with more information about the license.
   */
  data class License(val name: String, val url: String)

  /**
   * The developers for the POM. Uses the root project's developers, if unset.
   * @see MavenPom.developers
   */
  @Input val developers: ListProperty<Developer> = objects.listProperty()

  /**
   * The developers for the POM. Uses the root project's developers, if unset.
   * @see MavenPom.developers
   */
  @Input val githubProject: Property<GithubProject> = objects.property()

  /**
   * The developers for the POM. Uses the root project's developers, if unset.
   * @see MavenPom.developers
   */
  @Input val licenses: ListProperty<License> = objects.listProperty()
}
