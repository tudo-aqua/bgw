/*
 * Copyright 2022 The BoardGameWork Authors
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
import org.gradle.api.provider.Property
import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.property

/**
 * This extension stores project-specific Maven metadata (name, description) in a type-safe way. The
 * can then be retrieved to fill the POM configuration without forcing build files to contain
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
