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
import com.networknt.schema.JsonSchema
import java.util.*
import tools.aqua.bgw.net.common.message.GameActionMessage
import tools.aqua.bgw.net.server.entity.tables.SchemasByGame

/** Service for Json validation. */
interface ValidationService {

  /** The meta schema to validate schemas against. */
  val metaSchema: JsonSchema

  /**
   * Validates the payload of [GameActionMessage] against all schemas for this [gameID]. Returns
   * [Optional.EMPTY] iff a schema matched the payload or a list of validation errors.
   *
   * @param message The [GameActionMessage] with the payload, that gets validated.
   * @param gameID The identifier for the [SchemasByGame] entities in the Database.
   *
   * @return a [List] of [String] representations of the validation errors that occurred during
   * validation or [Optional.EMPTY] if there were no errors.
   *
   * @throws JsonSchemaNotFoundException whenever [gameID] did not resolve to any [SchemasByGame]
   * entity.
   */
  @Throws(JsonSchemaNotFoundException::class)
  fun validate(message: GameActionMessage, gameID: String): Optional<List<String>>

  /**
   * Validates the [schemaNode] against the [reference] schema. Returns a list of validation errors.
   *
   * @param reference The [reference] schema to validate [schemaNode] against.
   * @param schemaNode The schema to be validated against the [reference] schema.
   *
   * @return a [List] of [String] representations of the validation errors that occurred during
   * validation.
   */
  fun validate(reference: JsonSchema, schemaNode: JsonNode): List<String>

  /**
   * Validates the [schemaNode] against the meta schema. Returns a list of validation errors.
   *
   * @param schemaNode The schema to be validated against the meta schema.
   *
   * @return a [List] of [String] representations of the validation errors that occurred during
   * validation.
   */
  fun validateMetaSchema(schemaNode: JsonNode): List<String> = validate(metaSchema, schemaNode)

  /**
   * Instructs the [ValidationService] implementation to clear it schema cache. Should be called
   * whenever a [SchemasByGame] entity is removed or updated in the database.
   */
  fun flushSchemaCache()
}
