/*
 * Copyright 2022-2025 The BoardGameWork Authors
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
import com.networknt.schema.JsonSchema
import java.util.*
import tools.aqua.bgw.net.common.message.GameActionMessage
import tools.aqua.bgw.net.server.entity.tables.SchemasByGame

/** Service for Json validation. */
interface ValidationService {

  /** The meta schema to validate schemas against. */
  val metaSchemas: List<JsonSchema>

  /**
   * Validates the [GameActionMessage] with the payload against the schema matching the class and
   * the gameID. Returns a list of validation errors. If there is not matching schema with that
   * class it checks against all other schemas for that game. If there is no schema for that game it
   * throws a [JsonSchemaNotFoundException].
   *
   * @param message The [GameActionMessage] with the payload, that gets validated.
   * @param gameID The identifier for the [SchemasByGame] entities in the Database.
   * @return a [Optional] containing a [Map] of [String] to [List] of [String] representations of
   *   the validation errors that occurred during validation. The key of the map is the schema class
   *   name. The value is a list of validation errors that occurred during validation.
   * @throws JsonSchemaNotFoundException whenever [gameID] did not resolve to any [SchemasByGame]
   *   entity.
   */
  @Throws(JsonSchemaNotFoundException::class)
  fun validate(message: GameActionMessage, gameID: String): Optional<Map<String, List<String>>>

  /**
   * Validates the [schemaNode] against the [reference] schema. Returns a list of validation errors.
   *
   * @param reference The [reference] schema to validate [schemaNode] against.
   * @param schemaNode The schema to be validated against the [reference] schema.
   * @return a [List] of [String] representations of the validation errors that occurred during
   *   validation.
   */
  fun validate(reference: JsonSchema, schemaNode: JsonNode): List<String>

  /**
   * Validates the [schemaNode] against the meta schema. Returns a list of validation errors.
   *
   * @param schemaNode The schema to be validated against the meta schema.
   * @return a [List] of [String] representations of the validation errors that occurred during
   *   validation.
   */
  fun validateMetaSchema(schemaNode: JsonNode): List<String> =
      this.metaSchemas.map { validate(it, schemaNode) }.flatten()

  /**
   * Instructs the [ValidationService] implementation to clear it schema cache. Should be called
   * whenever a [SchemasByGame] entity is removed or updated in the database.
   */
  fun flushSchemaCache()
}
