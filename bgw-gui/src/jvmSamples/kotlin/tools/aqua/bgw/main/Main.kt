/*
 * Copyright 2025-2026 The BoardGameWork Authors
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

package tools.aqua.bgw.main

import java.io.File
import kotlinx.serialization.json.Json
import tools.aqua.bgw.main.examples.ExampleApplication

/** Serializes the documentation sample scenes into the JSON file given as the first argument. */
internal fun main(args: Array<String>) {
  val output = File(requireNotNull(args.firstOrNull()) { "Missing output file argument." })
  output.parentFile?.mkdirs()
  output.writeText(Json.encodeToString(ExampleApplication.getMap()))
}
