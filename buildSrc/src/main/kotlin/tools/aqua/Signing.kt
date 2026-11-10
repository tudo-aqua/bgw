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

/** Gradle property to force signing on (`true`) or off (`false`) regardless of the tasks run. */
const val SIGNING_PROPERTY = "bgwSign"

/**
 * Whether publications have to be signed in this build.
 *
 * Signing (and therefore a GPG installation) is only required when publishing to a remote
 * repository, e.g. Maven Central or the staging repository. Local publications via
 * `publishToMavenLocal` stay unsigned. Use `-PbgwSign=true` or `-PbgwSign=false` to override.
 */
fun Project.isSigningRequired(): Boolean {
  providers.gradleProperty(SIGNING_PROPERTY).orNull?.let {
    return it.toBoolean()
  }
  return gradle.startParameter.taskNames.any { requested ->
    val task = requested.substringAfterLast(':')
    task.startsWith("publish") && !task.endsWith("MavenLocal")
  }
}
