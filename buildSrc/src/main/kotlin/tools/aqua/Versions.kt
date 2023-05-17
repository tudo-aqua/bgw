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

package tools.aqua

import com.github.benmanes.gradle.versions.updates.resolutionstrategy.ComponentFilter

/** Keywords that indicate a stable version, independent of other version elements. */
private val stableKeyword = listOf("RELEASE", "FINAL", "GA")

/** A regular expression recognizing "usual" stable versions. */
private val regex = "^[0-9,.v-]+(-r)?$".toRegex()

/** Checks if this string appears to represent a stable version. */
val String.isStable: Boolean
  get() = stableKeyword.any { toUpperCase().contains(it) } || regex.matches(this)

/** Checks if this string does not appear to represent a stable version. */
val String.isUnstable: Boolean
  get() = isStable.not()

/**
 * Tests if the proposed version update would replace a stable with an unstable verison.
 * Stable-to-stable, unstable-to-stable, and unstable-to-unstable upgrades are accepted.
 */
val destabilizesVersion = ComponentFilter {
  candidate.version.isUnstable && currentVersion.isStable
}

/**
 * Split a dotted version into more generic fragments. E.g., `1.2.3` is split into `[1, 1.2,
 * 1.2.3]`.
 */
val String.genericVersions: List<String>
  get() {
    val versionFragments = split('.')
    return (1..versionFragments.size).map { versionFragments.take(it).joinToString(".") }
  }
