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

@file:Suppress("unused")

package tools.aqua.bgw.dialog

import kotlinx.serialization.Serializable

/**
 * Extension filters for [FileDialog]s.
 *
 * Maps a file type description to its extensions.
 *
 * To generate e.g. "(*.png, *.jpg)" set [extensions] = ("png", "jpg").
 *
 * @property description File type description (not displayed in dialog).
 * @property extensions File extensions.
 * @constructor Creates an [ExtensionFilter].
 */
@Serializable
data class ExtensionFilter
internal constructor(val description: String, internal val extensions: List<String>) {

  /**
   * Creates an [ExtensionFilter].
   *
   * Maps a file type description to its extensions.
   *
   * To generate e.g. "(*.png, *.jpg)" set [extensions] = ("png", "jpg").
   *
   * @param description File type description (not displayed in dialog).
   * @param extensions File extensions.
   */
  constructor(
      description: String,
      vararg extensions: String
  ) : this(description, extensions.asList())

  internal fun getExtensionsString(): String? {
    val validExtensions = extensions.filter { it.isNotEmpty() && it.isNotBlank() && it != "*" }

    val returnExtensions =
        validExtensions.joinToString(";*") { if (it.startsWith(".")) it else ".$it" }

    if (returnExtensions.isEmpty()) return null
    return if (returnExtensions.startsWith("*")) returnExtensions.replace(Regex("\\*"), "")
    else returnExtensions
  }
}
