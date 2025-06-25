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
import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import jakarta.annotation.PostConstruct
import java.io.FileNotFoundException
import java.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tools.aqua.bgw.net.common.message.GameActionMessage
import tools.aqua.bgw.net.server.*
import tools.aqua.bgw.net.server.entity.tables.SchemasByGame
import tools.aqua.bgw.net.server.entity.tables.SchemasByGameRepository

/**
 * Implementation of [ValidationService]. It uses the networknt/json-schema-validator and Jackson to
 * validate.
 *
 * @property schemasByGameRepository Auto-Wired repository for schemas.
 */
@Service
class JsonSchemaValidator(val schemasByGameRepository: SchemasByGameRepository) :
    ValidationService {

  /** The meta schema to validate schemas against. */
  override val metaSchemas: List<JsonSchema> =
      listOf(META_SCHEMA_JSON_URL_STRING, BGW_META_SCHEMA_JSON_URL_STRING).map {
        JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
            .getSchema(javaClass.getResourceAsStream(it) ?: throw FileNotFoundException())
      }

  /** Object mapper for Json (de)serialization. */
  private val mapper = ObjectMapper()

  /** The logger instance. */
  private val logger: Logger = LoggerFactory.getLogger(javaClass)

  /** Schema map that caches schemas loaded from database to increase validation performance. */
  private val schemaCache = mutableMapOf<String, List<JsonSchema>>()

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
  override fun validate(
      message: GameActionMessage,
      gameID: String
  ): Optional<Map<String, List<String>>> {
    val payload = mapper.readTree(message.payload)
    val schemas =
        (schemaCache[gameID]
            ?: schemasByGameRepository
                .findAll()
                .filter { it.gameID == gameID }
                .map {
                  JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(it.schema)
                }
                .also { schemaCache[gameID] = it })

    val matchingSchema = schemas.firstOrNull { it.clazz == message.clazz }

    val errors =
        if (matchingSchema != null) {
          mapOf(matchingSchema.title to validate(matchingSchema, payload))
        } else {
          schemas.associate { schema -> schema.title to validate(schema, payload) }
        }

    /* If there is at least one schema which has no validation errors */
    val foundAtLeastOneMatchingSchema = errors.any { it.value.isEmpty() }

    return if (foundAtLeastOneMatchingSchema) Optional.empty() else Optional.of(errors)
  }

  /**
   * Validates the [schemaNode] against the [reference] schema. Returns a list of validation errors.
   *
   * @param reference The [reference] schema to validate [schemaNode] against.
   * @param schemaNode The schema to be validated against the [reference] schema.
   * @return a [List] of [String] representations of the validation errors that occurred during
   *   validation.
   */
  override fun validate(reference: JsonSchema, schemaNode: JsonNode): List<String> =
      reference.validate(schemaNode).map { it.message }

  /**
   * Instructs the [ValidationService] implementation to clear it schema cache. Should be called
   * whenever a [SchemasByGame] entity is removed or updated in the database.
   */
  override fun flushSchemaCache(): Unit = schemaCache.clear()

  /**
   * This method loads the example schema located at [EXAMPLE_SCHEMA_JSON_URL_STRING] into the
   * database.
   */
  @PostConstruct
  fun initExample() {
    setOf(
            // EXAMPLE_SCHEMA_JSON_URL_STRING,
            "/maumauschemas/game_action_schema.json",
            "/maumauschemas/game_end_schema.json",
            "/maumauschemas/game_init_schema.json",
            "/maumauschemas/shuffle_stack_schema.json")
        .mapNotNull { t ->
          javaClass.getResource(t)?.readText()
              ?: null.also { logger.warn("Failed to load schema from resources: $t") }
        }
        .filter { !schemasByGameRepository.existsBySchemaAndGameID(it, MAUMAU_GAME_ID) }
        .forEach {
          logger.info("Registering schema $it")
          schemasByGameRepository.save(SchemasByGame(MAUMAU_GAME_ID, it))
        }
  }
}
