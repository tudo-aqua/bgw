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
	@Throws(JsonSchemaNotFoundException::class)
	fun validate(message: GameMessage, gameID: String): Boolean

	fun flushSchemas()
}

/**
 * uses pwall567/json-kotlin-schema to validate
 */
//@Service
class JsonKotlinSchemaValidator(private val gameSchemaRepository: GameSchemaRepository) : ValidationService {
	private data class ActualSchema(val init: JSONSchema, val action: JSONSchema, val end: JSONSchema)

	//TODO load
	private lateinit var metaSchema: JSONSchema

	val logger = LoggerFactory.getLogger(javaClass)

	private val schemaMap = mutableMapOf<String, ActualSchema>()

	override fun validate(message: GameMessage, gameID: String): Boolean {
		println("im validatin !!!!!!!!!!!!!!!!!!!!!")
		val gameSchema = schemaMap[gameID] ?: with(gameSchemaRepository.findById(gameID)) {
			if (isPresent) {
				val dbEntity = get()
				ActualSchema(
					init = JSONSchema.parse(dbEntity.initActionSchema),
					action = JSONSchema.parse(dbEntity.gameActionSchema),
					end = JSONSchema.parse(dbEntity.endActionSchema)
				).also { schemaMap[gameID] = it }
			} else
				throw JsonSchemaNotFoundException()
		}

		return when (message) {
			is InitializeGameMessage -> {
				validate(message.payload, gameSchema.init)
			}
			is GameActionMessage -> {
				validate(message.payload, gameSchema.action)
			}
			is EndGameMessage -> {
				validate(message.payload, gameSchema.end)
			}
		}
	}

	override fun flushSchemas() = schemaMap.clear()

	private fun validate(msg: String, schema: JSONSchema) = schema.validate(msg)

	/**
	 * Loads default schemas into the database.
	 */
	@PostConstruct
	fun init() {
		javaClass.getResource(EXAMPLE_SCHEMA_JSON_URL_STRING)?.let {
			val schemaString = it.readText()
			gameSchemaRepository.save(
				GameSchema(
					gameID = "example",
					initActionSchema = schemaString,
					gameActionSchema = schemaString,
					endActionSchema = schemaString,
				)
			)
		} ?: logger.warn("example_schema could not be loaded")
	}
}


class JsonSchemaNotFoundException() : Exception()

@Service
class JsonSchemaValidator(val gameSchemaRepository: GameSchemaRepository) : ValidationService {
	private data class ActualSchema(val init: JsonSchema, val action: JsonSchema, val end: JsonSchema)

	val logger = LoggerFactory.getLogger(javaClass)

	private val schemaMap = mutableMapOf<String, ActualSchema>()

	override fun validate(message: GameMessage, gameID: String): Boolean {
		val gameSchema = schemaMap[gameID] ?: with(gameSchemaRepository.findById(gameID)) {
			if (isPresent) {
				val dbEntity = get()
				ActualSchema(
					init = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(dbEntity.initActionSchema),
					action = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(dbEntity.gameActionSchema),
					end = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(dbEntity.endActionSchema)
				).also { schemaMap[gameID] = it }
			} else
				throw JsonSchemaNotFoundException()
		}
		return when (message) {
			is InitializeGameMessage -> {
				gameSchema.init.validate(ObjectMapper().readTree(message.payload)).also { println(it) }.size == 0
			}
			is GameActionMessage -> {
				gameSchema.action.validate(ObjectMapper().readTree(message.payload)).also { println(it) }.size == 0
			}
			is EndGameMessage -> {
				gameSchema.end.validate(ObjectMapper().readTree(message.payload)).also { println(it) }.size == 0
			}
		}
	}

	override fun flushSchemas() = schemaMap.clear()

	@PostConstruct
	fun initExample() {
		val schema = javaClass.getResource(EXAMPLE_SCHEMA_JSON_URL_STRING).readText()
		gameSchemaRepository.save(GameSchema("example", schema, schema, schema))
	}

}