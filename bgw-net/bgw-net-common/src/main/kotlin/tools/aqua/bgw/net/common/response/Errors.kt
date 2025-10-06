/*
 * Copyright 2025 The BoardGameWork Authors
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

package tools.aqua.bgw.net.common.response

/**
 * Represents a collection of errors. Each error is associated with a title. The title belongs to
 * the schema where the errors occurred. The errors are represented as a list of strings.
 *
 * @param items A map of titles to lists of errors to initialize this [Errors] object with.
 *
 * The [Errors] class extends [LinkedHashMap] with the title as key and the list of errors as value.
 */
class Errors(items: Map<String, List<String>>) : LinkedHashMap<String, List<String>>() {

  init {
    this.putAll(items)
  }

  /**
   * Returns a string representation of this [Errors] object. The string contains all errors grouped
   * by their title. Each error is enclosed in double quotes. Errors are separated by commas.
   *
   * @return a string representation of this [Errors] object
   */
  override fun toString(): String =
      this.entries.joinToString("\n\n") { (title, errors) ->
        """
            [$title]
            ${errors.joinToString(",\n") { "\"$it\"" }}
            """
            .trimIndent()
      }
}
