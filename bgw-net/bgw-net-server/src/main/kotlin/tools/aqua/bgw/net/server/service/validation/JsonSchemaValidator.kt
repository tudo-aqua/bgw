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
import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import java.io.FileNotFoundException
import java.util.*
import javax.annotation.PostConstruct
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
 */
@Service
class JsonSchemaValidator(val schemasByGameRepository: SchemasByGameRepository) :
    ValidationService {

  private val mapper = ObjectMapper()

  private val logger: Logger = LoggerFactory.getLogger(javaClass)

  private val metaSchema: JsonSchema =
      JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
          .getSchema(
              javaClass.getResourceAsStream(META_SCHEMA_JSON_URL_STRING)
                  ?: throw FileNotFoundException())

  private val schemaMap = mutableMapOf<String, List<JsonSchema>>()

  /**
   * Validates the payload of [GameActionMessage] against all schemas for this [gameID].
   * Returns [Optional.EMPTY] iff a schema matched the payload or a list of validation errors.
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
  override fun validate(message: GameActionMessage, gameID: String): Optional<List<String>> {
    val payload = mapper.readTree(message.payload)
    val errors =
      (schemaMap[gameID]
            ?: schemasByGameRepository
                .findAll()
                .filter { it.gameID == gameID }
                .map {
                  JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(it.schema)
                }
                .also { schemaMap[gameID] = it })
        .map { validate(it, payload) }

    return if (errors.any { it.isEmpty() }) Optional.empty() else Optional.of(errors.flatten())
  }

  /**
   * Validates the [schemaNode] against the [reference] schema.
   * Returns a list of validation errors.
   *
   * @param reference The [reference] schema to validate [schemaNode] against.
   * @param schemaNode The schema to be validated against the [refrence] schema.
   *
   * @return a [List] of [String] representations of the validation errors that occurred during
   * validation.
   */
  override fun validate(reference : JsonSchema, schemaNode: JsonNode): List<String> =
    reference.validate(schemaNode).map { it.message }

  /**
   * Validates the [schemaNode] against the meta schema.
   * Returns a list of validation errors.
   *
   * @param schemaNode The schema to be validated against the meta schema.
   *
   * @return a [List] of [String] representations of the validation errors that occurred during
   * validation.
   */
  override fun validateMetaSchema(schemaNode: JsonNode): List<String> = validate(metaSchema, schemaNode)

  /**
   * Instructs the [ValidationService] implementation to clear it schema cache. Should be called
   * whenever a [SchemasByGame] entity is removed or updated in the database.
   */
  override fun flushSchemaCache(): Unit = schemaMap.clear()

  /**
   * This method loads the example schema located at [EXAMPLE_SCHEMA_JSON_URL_STRING] into the
   * database.
   */
  @PostConstruct
  fun initExample() {
    val exampleSchema = javaClass.getResource(EXAMPLE_SCHEMA_JSON_URL_STRING)?.readText()

    val maumauInitSchema = javaClass.getResource(MAUMAU_INIT_SCHEMA_JSON_URL_STRING)?.readText()
    val maumauGameSchema = javaClass.getResource(MAUMAU_GAME_SCHEMA_JSON_URL_STRING)?.readText()
    val maumauEndSchema = javaClass.getResource(MAUMAU_END_SCHEMA_JSON_URL_STRING)?.readText()

    if (exampleSchema == null) {
      logger.warn("Failed to load example schema from resources")
    } else {
      schemasByGameRepository.save(SchemasByGame(EXAMPLE_GAME_ID, exampleSchema))
    }

    if (maumauInitSchema == null || maumauGameSchema == null || maumauEndSchema == null) {
      logger.warn("Failed to load maumau schemas from resources")
    } else {
      schemasByGameRepository.save(SchemasByGame(MAUMAU_GAME_ID, maumauInitSchema))
      schemasByGameRepository.save(SchemasByGame(MAUMAU_GAME_ID, maumauGameSchema))
      schemasByGameRepository.save(SchemasByGame(MAUMAU_GAME_ID, maumauEndSchema))
    }
  }
}
