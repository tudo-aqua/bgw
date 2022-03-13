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

package tools.aqua.bgw.net.server.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.*
import java.io.FileNotFoundException
import javax.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tools.aqua.bgw.net.common.message.GameActionMessage
import tools.aqua.bgw.net.server.*
import tools.aqua.bgw.net.server.entity.tables.SchemasByGame
import tools.aqua.bgw.net.server.entity.tables.SchemasByGameRepository

interface ValidationService {
  /**
   * Validates the payload of [GameMessage] with the matching Schema (Init, Game, End) of the
   * [SchemasByGame] Entity, identified by [gameID]. Returns a list of validation errors or null if
   * validation was successful.
   *
   * @param message The [GameMessage] with the payload, that gets validated.
   * @param gameID The identifier for the [SchemasByGame] Entity in the Database.
   *
   * @return a [List] of [String] representations of the validation errors that occurred during
   * validation or null if there were no errors.
   *
   * @throws JsonSchemaNotFoundException whenever [gameID] did not resolve to a [SchemasByGame]
   * entity.
   */
  @Throws(JsonSchemaNotFoundException::class)
  fun validate(message: GameActionMessage, gameID: String): List<String>?

  fun validate(schemaNode: JsonNode): List<String>

  /**
   * Instructs the [ValidationService] implementation to clear it schema cache. Should be called
   * whenever a [SchemasByGame] entity is removed or updated in the database.
   */
  fun flushSchemaCache()
}

/** Indicates that a key could not be resolved to a [SchemasByGame] entity in the database. */
class JsonSchemaNotFoundException : Exception()

/**
 * Implementation of [ValidationService]. It uses the networknt/json-schema-validator and Jackson to
 * validate.
 */
@Service
class JsonSchemaValidator(val schemasByGameRepository: SchemasByGameRepository) :
    ValidationService {
  private data class ActualSchema(
      val init: JsonSchema,
      val action: JsonSchema,
      val end: JsonSchema
  )

  private val mapper = ObjectMapper()

  private val logger: Logger = LoggerFactory.getLogger(javaClass)

  private val schemaMap = mutableMapOf<String, ActualSchema>()

  override fun validate(message: GameActionMessage, gameID: String): List<String>? {
    val gameSchema =
        schemaMap[gameID]
            ?: with(schemasByGameRepository.findById(gameID)) {
              if (isPresent) {
                val dbEntity = get()
                ActualSchema(
                        init =
                            JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
                                .getSchema(dbEntity.initActionSchema),
                        action =
                            JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
                                .getSchema(dbEntity.gameActionSchema),
                        end =
                            JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
                                .getSchema(dbEntity.endActionSchema))
                    .also { schemaMap[gameID] = it }
              } else throw JsonSchemaNotFoundException()
            }

    return gameSchema
        .action
        .validate(mapper.readTree(message.payload))
        .map(ValidationMessage::getMessage)
        .ifEmpty { null }
  }

  override fun validate(schemaNode: JsonNode): List<String> {
    val factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
    val resource =
        javaClass.getResourceAsStream(META_SCHEMA_JSON_URL_STRING) ?: throw FileNotFoundException()
    val metaSchema = factory.getSchema(resource)
    val results = metaSchema.validate(schemaNode)
    return results.map { it.message }
  }

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
      schemasByGameRepository.save(
          SchemasByGame(EXAMPLE_GAME_ID, exampleSchema, exampleSchema, exampleSchema))
    }

    if (maumauInitSchema == null || maumauGameSchema == null || maumauEndSchema == null) {
      logger.warn("Failed to load maumau schemas from resources")
    } else {
      schemasByGameRepository.save(
          SchemasByGame(MAUMAU_GAME_ID, maumauInitSchema, maumauGameSchema, maumauEndSchema))
    }
  }
}
