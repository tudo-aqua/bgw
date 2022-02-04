package tools.aqua.bgw.net.server.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import com.networknt.schema.ValidationMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import tools.aqua.bgw.net.common.*
import tools.aqua.bgw.net.server.EXAMPLE_SCHEMA_JSON_URL_STRING
import tools.aqua.bgw.net.server.entity.SchemasByGame
import tools.aqua.bgw.net.server.entity.SchemasByGameRepository
import javax.annotation.PostConstruct
import kotlin.jvm.Throws

interface ValidationService {
	/**
	 * Validates the payload of [GameMessage] with the matching Schema (Init, Game, End) of the [SchemasByGame] Entity,
	 * identified by [gameID]. Returns a list of validation errors or null if validation was successful.
	 *
	 * @param message The [GameMessage] with the payload, that gets validated.
	 * @param gameID The identifier for the [SchemasByGame] Entity in the Database.
	 *
	 * @return a [List] of [String] representations of the validation errors that occurred during validation
	 * or null if there were no errors.
	 *
	 * @throws JsonSchemaNotFoundException whenever [gameID] did not resolve to a [SchemasByGame] entity.
	 */
	@Throws(JsonSchemaNotFoundException::class)
	fun validate(message: GameMessage, gameID: String): List<String>?

	/**
	 * Instructs the [ValidationService] implementation to clear it schema cache.
	 * Should be called whenever a [SchemasByGame] entity is removed or updated in the database.
	 */
	fun flushSchemaCache()
}

/**
 * Indicates that a key could not be resolved to a [SchemasByGame] entity in the database.
 */
class JsonSchemaNotFoundException : Exception()

/**
 * Implementation of [ValidationService]. It uses the networknt/json-schema-validator and Jackson to validate.
 */
@Service
class JsonSchemaValidator(val schemasByGameRepository: SchemasByGameRepository) : ValidationService {
	private data class ActualSchema(val init: JsonSchema, val action: JsonSchema, val end: JsonSchema)

	private val mapper = ObjectMapper()

	private val logger: Logger = LoggerFactory.getLogger(javaClass)

	private val schemaMap = mutableMapOf<String, ActualSchema>()

	override fun validate(message: GameMessage, gameID: String): List<String>? {
		val gameSchema = schemaMap[gameID] ?: with(schemasByGameRepository.findById(gameID)) {
			if (isPresent) {
				val dbEntity = get()
				ActualSchema(
					init = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
						.getSchema(dbEntity.initActionSchema),
					action = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7)
						.getSchema(dbEntity.gameActionSchema),
					end = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(dbEntity.endActionSchema)
				).also { schemaMap[gameID] = it }
			} else throw JsonSchemaNotFoundException()
		}
		fun Iterable<ValidationMessage>.stringsOrNull() = map(ValidationMessage::getMessage).ifEmpty { null }
		return when (message) {
			is InitializeGameMessage -> gameSchema.init.validate(mapper.readTree(message.payload)).stringsOrNull()
			is GameActionMessage -> gameSchema.action.validate(mapper.readTree(message.payload)).stringsOrNull()
			is EndGameMessage -> gameSchema.end.validate(mapper.readTree(message.payload)).stringsOrNull()
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
			schemasByGameRepository.save(SchemasByGame("example", schema, schema, schema))
		}
	}

}