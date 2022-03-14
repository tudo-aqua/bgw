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

  override fun validate(message: GameActionMessage, gameID: String): Optional<List<String>> {
    val gameSchemas =
        schemaMap[gameID]
            ?: schemasByGameRepository
                .findAll()
                .filter { it.gameID == gameID }
                .map {
                  JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(it.schema)
                }
                .also { schemaMap[gameID] = it }

    val errors = gameSchemas.map { validate((mapper.readTree(message.payload))) }

    return if (errors.any { it.isEmpty() }) Optional.empty() else Optional.of(errors.flatten())
  }

  override fun validate(schemaNode: JsonNode): List<String> =
      metaSchema.validate(schemaNode).map { it.message }

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
