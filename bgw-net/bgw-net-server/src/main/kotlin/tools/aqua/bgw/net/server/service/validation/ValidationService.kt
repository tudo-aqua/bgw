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

package tools.aqua.bgw.net.server.service.validation

import com.fasterxml.jackson.databind.JsonNode
import java.util.*
import tools.aqua.bgw.net.common.message.GameActionMessage
import tools.aqua.bgw.net.server.entity.tables.SchemasByGame

interface ValidationService {
  /**
   * Validates the payload of [GameActionMessage] with the matching Schema (Init, Game, End) of the
   * [SchemasByGame] Entity, identified by [gameID]. Returns a list of validation errors or null if
   * validation was successful.
   *
   * @param message The [GameActionMessage] with the payload, that gets validated.
   * @param gameID The identifier for the [SchemasByGame] Entity in the Database.
   *
   * @return a [List] of [String] representations of the validation errors that occurred during
   * validation or [Optional.EMPTY] if there were no errors.
   *
   * @throws JsonSchemaNotFoundException whenever [gameID] did not resolve to a [SchemasByGame]
   * entity.
   */
  @Throws(JsonSchemaNotFoundException::class)
  fun validate(message: GameActionMessage, gameID: String): Optional<List<String>>

  fun validate(schemaNode: JsonNode): List<String>

  /**
   * Instructs the [ValidationService] implementation to clear it schema cache. Should be called
   * whenever a [SchemasByGame] entity is removed or updated in the database.
   */
  fun flushSchemaCache()
}
