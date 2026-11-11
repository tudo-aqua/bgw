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

/** Whether [version] still contains placeholders, i.e. git metadata could not be read. */
fun isUnresolvedVersion(version: String): Boolean =
    version.contains("\${") || version.contains("-0000000-")

/**
 * Computes the project version with the git command line, in the same format as the git-versioning
 * plugin configuration in the root conventions:
 * - `<tag>` if HEAD is tagged with `v<tag>`,
 * - `<tag>-<distance>-<commit>-SNAPSHOT` on `main` or a detached HEAD,
 * - `<tag>-<branch>-<distance>-<commit>-SNAPSHOT` on other branches.
 *
 * Returns `null` if git is not available or no `v*` tag is reachable.
 */
fun Project.gitCliVersion(): String? {
  fun git(vararg args: String): String? =
      try {
        providers
            .exec {
              commandLine("git", *args)
              workingDir = rootDir
              isIgnoreExitValue = true
            }
            .let { result ->
              if (result.result.get().exitValue != 0) null
              else result.standardOutput.asText.get().trim().ifEmpty { null }
            }
      } catch (_: Exception) {
        null
      }

  git("tag", "--points-at", "HEAD", "--list", "v*")?.lines()?.firstOrNull()?.let {
    return it.removePrefix("v")
  }

  val describe = git("describe", "--tags", "--long", "--match", "v*") ?: return null
  val match = Regex("""^v(.*)-(\d+)-g[0-9a-f]+$""").find(describe) ?: return null
  val (tagVersion, distance) = match.destructured
  val commit = git("rev-parse", "--short=7", "HEAD") ?: return null
  val branch = git("rev-parse", "--abbrev-ref", "HEAD") ?: "HEAD"

  return if (branch == "main" || branch == "HEAD") "$tagVersion-$distance-$commit-SNAPSHOT"
  else "$tagVersion-${branch.replace('/', '-')}-$distance-$commit-SNAPSHOT"
}
