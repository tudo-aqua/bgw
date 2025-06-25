/*
 * Copyright 2021-2025 The BoardGameWork Authors
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

import com.diffplug.gradle.spotless.KotlinExtension
import com.diffplug.gradle.spotless.KotlinGradleExtension
import org.gradle.api.Project

/**
 * Apply the project default formatting and license header.
 *
 * @param rootProject the root project; used to look up the license header.
 */
fun KotlinExtension.defaultFormat(rootProject: Project) {
  licenseHeaderFile(rootProject.file("contrib/license-header.template.kt")).also {
    it.updateYearWithLatest(true)
  }
  ktfmt()
}

/**
 * Apply the project default formatting and license header.
 *
 * @param rootProject the root project; used to look up the license header.
 */
fun KotlinGradleExtension.defaultFormat(rootProject: Project) {
  licenseHeaderFile(
          rootProject.file("contrib/license-header.template.kt"),
          "(import |@file|plugins |dependencyResolutionManagement|rootProject.name)")
      .also { it.updateYearWithLatest(true) }
  ktfmt()
}
