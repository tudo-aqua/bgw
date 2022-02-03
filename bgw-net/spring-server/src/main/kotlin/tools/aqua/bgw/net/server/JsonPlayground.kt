package tools.aqua.bgw.net.server

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Serializable
data class Person(val name: String, val age: Int)

//this is only a playground

//Classes to define Turns
//these could be implemented by students based on the schema (maybe even generated)
@Serializable
data class Turn(val direction: Direction, val steps: Int, val meeple: Meeple)

@Serializable
enum class Direction() {
	LEFT,
	RIGHT
}

@Serializable
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
//@JsonSubTypes(
//	JsonSubTypes.Type(value = Tower::class, name = "Tower"),

//	JsonSubTypes.Type(value = King::class, name = "King")
//)
sealed class Meeple()

data class Tower(val height: Int, val error: Boolean) : Meeple()

data class King(val name: String) : Meeple()

data class Car(val name: String, val numWheels: Int)

class Client<T>(private val clazz: Class<T>) {
	private val om = ObjectMapper().registerModule(kotlinModule())

	fun serializeIt(obj: T) : String {
		return om.writeValueAsString(obj)
	}

	fun deserializeIt(string: String) {
		onDeserialization?.invoke(om.readValue(string, clazz))
	}

	var onDeserialization : ((T) -> Unit)? = null
}

}

fun not_main() {
	//encode a Turn object to JSON string
	//this string would arrive at the server
	val jsonString = Json.encodeToString<Turn>(
		Turn(
			direction = Direction.LEFT,
			steps = 7,
			King("Ludwig")
		)
	)
	println(jsonString)
	//define the schema
	//this schema would be stored on the server (developed by students)
	val schemaVariable =
		"\"\$schema\": \"http://json-schema.org/draft-07/schema\"" //WARUM KANN MAN $ IN MULTILINE STRINGS NICHT ESCAPEN???
	val schemaString = """
		{
			$schemaVariable,
			"type": "object",
			"required": ["player","direction","steps", "meeple"], 
			"properties": {
				"player": {
					"type": "object",
					"required": ["name"],
					"properties": {
						"name": {
							"type": "string",
							"maxLength": 10
						}
					},
					"additionalProperties": false
				},
				"direction": {
					"type": "string",
					"enum": ["LEFT", "RIGHT"]
				},
				"steps": {
					"type": "integer"
				},
				"meeple": {
					"type": "object",
					"oneOf": [
						{
							"type": "object",
							"required": ["type", "name"],
							"properties": {
					            "type": { "const": "tools.aqua.bgw.net.server.King" },
								"name": { "type": "string" }
							},
							"additionalProperties": false
						},
						{ 
							"type": "object",
							"required": ["type", "height"],
							"properties": {
					            "type": { "const": "tools.aqua.bgw.net.server.Tower" },
								"height": { "type": "integer" }
							},
							"additionalProperties": false
						}
					]
				}
			},
			"additionalProperties": false
		}
	""".trimIndent()
	//load the schema
	//this happens on the server
	//val schema = SchemaLoader.load(JSONObject(schemaString))
	//try validating the Turn object JSON string
	//try {
	//schema.validate(JSONObject(jsonString))
	//} catch (e: ValidationException) {
	//println("validation failed with error(s): ${e.allMessages.joinToString(prefix = "\n", separator = "\n", postfix = "\n") {
	//it
	//}}")
	//}
}

