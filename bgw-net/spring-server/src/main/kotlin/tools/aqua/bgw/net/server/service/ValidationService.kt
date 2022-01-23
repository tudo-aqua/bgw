package tools.aqua.bgw.net.server.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import net.pwall.json.schema.JSONSchema
import org.dom4j.datatype.InvalidSchemaException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tools.aqua.bgw.net.common.*
import tools.aqua.bgw.net.server.EXAMPLE_SCHEMA_JSON_URL_STRING
import tools.aqua.bgw.net.server.META_SCHEMA_JSON_URL_STRING
import tools.aqua.bgw.net.server.entity.GameSchema
import tools.aqua.bgw.net.server.entity.GameSchemaRepository
import java.io.File
import javax.annotation.PostConstruct
import kotlin.jvm.Throws

interface ValidationService {
	/**
	 * Validates the payload of [GameMessage] with the matching Schema (Init, Game, End) of the [GameSchema] Entity,
	 * identified by [gameID]. Returns a list of validation errors or null if validation was successful.
	 *
	 * @param message The [GameMessage] with the payload, that gets validated.
	 * @param gameID The identifier for the [GameSchema] Entity in the Database.
	 *
	 * @return a [List] of [String] representations of the validation errors that occurred during validation
	 * or null if there were no errors.
	 *
	 * @throws JsonSchemaNotFoundException whenever [gameID] did not resolve to a [GameSchema] entity.
	 */
	@Throws(JsonSchemaNotFoundException::class)
	fun validate(message: GameMessage, gameID: String): List<String>?

	/**
	 * Instructs the [ValidationService] implementation to clear it schema cache.
	 * Should be called whenever a [GameSchema] entity is removed or updated in the database.
	 */
	fun flushSchemaCache()
}

/**
 * Indicates that a key could not be resolved to a [GameSchema] entity in the database.
 */
class JsonSchemaNotFoundException : Exception()

/**
 * Implementation of [ValidationService]. It uses the networknt/json-schema-validator and Jackson to validate.
 */
@Service
class JsonSchemaValidator(val gameSchemaRepository: GameSchemaRepository) : ValidationService {
	private data class ActualSchema(val init: JsonSchema, val action: JsonSchema, val end: JsonSchema)

	val mapper = ObjectMapper()

	val logger = LoggerFactory.getLogger(javaClass)

	private val schemaMap = mutableMapOf<String, ActualSchema>()

	override fun validate(message: GameMessage, gameID: String): List<String>? {
		val gameSchema = schemaMap[gameID] ?: with(gameSchemaRepository.findById(gameID)) {
			if (isPresent) {
				val dbEntity = get()
				ActualSchema(
					init = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
						.getSchema(dbEntity.initActionSchema),
					action = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
						.getSchema(dbEntity.gameActionSchema),
					end = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(dbEntity.endActionSchema)
				).also { schemaMap[gameID] = it }
			} else
				throw JsonSchemaNotFoundException()
		}
		return when (message) {
			is InitializeGameMessage -> {
				with(gameSchema.init.validate(mapper.readTree(message.payload))) {
					if (isEmpty()) null else map { it.message }
				}
			}
			is GameActionMessage -> {
				with(gameSchema.init.validate(mapper.readTree(message.payload))) {
					if (isEmpty()) null else map { it.message }
				}
			}
			is EndGameMessage -> {
				with(gameSchema.init.validate(mapper.readTree(message.payload))) {
					if (isEmpty()) null else map { it.message }
				}
			}
		}
	}

	override fun flushSchemaCache() = schemaMap.clear()

	/**
	 * This method loads the example schema located at [EXAMPLE_SCHEMA_JSON_URL_STRING] into the database.
	 */
	@PostConstruct
	fun initExample() {
		val schema = javaClass.getResource(EXAMPLE_SCHEMA_JSON_URL_STRING)?.readText()
		if (schema == null) {
			logger.warn("Failed to load example schema from resources")
		} else {
			gameSchemaRepository.save(GameSchema("example", schema, schema, schema))
		}
	}

}