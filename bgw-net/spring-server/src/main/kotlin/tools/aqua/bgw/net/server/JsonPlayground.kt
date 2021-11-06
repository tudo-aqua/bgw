package tools.aqua.bgw.net.client

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.everit.json.schema.ValidationException
import org.everit.json.schema.loader.SchemaLoader
import org.json.JSONObject


//this is only a playground

//Classes to define Turns
//these could be implemented by students based on the schema (maybe even generated)
@Serializable
data class Turn(val player: Player, val direction: Direction, val steps : Int)

@Serializable
data class Player(val name: String)

@Serializable
enum class Direction() {
	LEFT,
	RIGHT
}

fun main() {
	//encode a Turn object to JSON string
	//this string would arrive at the server
	val jsonString = Json.encodeToString<Turn>(Turn(player = Player("player1"), direction = Direction.LEFT, steps = 7))
	println(jsonString)
	//define the schema
	//this schema would be stored on the server (developed by students)
	val schemaVariable = "\"\$schema\": \"http://json-schema.org/draft-07/schema\"" //WARUM KANN MAN $ IN MULTILINE STRINGS NICHT ESCAPEN???
	val schemaString = """
		{
			$schemaVariable,
			"type": "object",
			"required": ["player","direction","steps"],
			"properties": {
				"player": {
					"type": "object",
					"required": ["name"],
					"properties": {
						"name": {
							"type": "string",
							"maxLength": 10
						}
					}
				},
				"direction": {
					"type": "string",
					"enum": ["LEFT", "RIGHT"]
				},
				"steps": {
					"type": "integer"
				}
			}
		}
	""".trimIndent()
	//load the schema
	//this happens on the server
	val schema = SchemaLoader.load(JSONObject(schemaString))
	//try validating the Turn object JSON string
	try {
		schema.validate(JSONObject(jsonString))
	} catch (e : ValidationException) {
		println("validation failed")
	}
}

