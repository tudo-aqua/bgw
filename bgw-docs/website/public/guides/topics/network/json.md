[json-schema]: https://json-schema.org/understanding-json-schema/
[tutorialspoint]: https://www.tutorialspoint.com/json/json_schema.htm
[types]: https://json-schema.org/understanding-json-schema/reference/type.html

# JSON

This section deals with the integration of json schema to validate GameActionClasses on the server side and how to 
generate a json schema from a given KClass.

## External resources
A documentation for json schema can be found [here][json-schema].<br>
A quick-start-guide can be found [here][tutorialspoint].

## JSON data types

The following [data types][types] may be used in json schema:

| Type     | Values allowed     | Values not allowed |
|----------|--------------------|--------------------|
| string   | "foo"              | 1                  |
| number   | 1; 1.1; 1.1e5      | ""                 |              
| integer  | 1; 1.0             | 1.1                |
| object   | Nested objects     |                    | 
| array    | ["1", "2"]; [1, 2] | ""; 1              |
| boolean  | true; false        | "true"; 1          |
| null     | null               | ""                 |

## Converting a simple DataClass into a json schema

Let's consider the following data class:
````kotlin
@GameActionClass
data class MauMauEndGameAction(
  val winner: String
) : GameAction() {
	
  override fun toString(): String = 
    "$winner has won the game!"
}
````

The json schema contains all attributes declared in the class. In this instance there is only one field ``winner`` of
type ``string``. The resulting json schema looks as follows.

````json
{
  "$schema": "http://json-schema.org/draft-07/schema",
  "type": "object",
  "title": "MauMau End Game Schema",
	
  "required": [
    "winner"
  ],
	
  "properties": {
    "winner": { "type": "string" }
  },
	
  "additionalProperties": false
}
````

The first block defines meta information: The json meta schema ``draft-07``, the type ``object`` and the title of this 
schema.

The ``required`` field contains a list of all attributes of the KClass. In this case the field ``winner``.

For each field you have to define ``properties``. This includes the data type for the field as one of those listed 
[above](#JSON data types). Here the property ``winner`` gets annotated with type ``string``.

Additionally ``"additionalProperties": false`` forbids the usage of additional attributes.

## Lists and arrays

For lists and arrays the data type is set to ``array``. Additionally, the data type for the elements in that array has 
to be specified. The attribute

````kotlin
data class Example(
  val playerNames: list<String>
)
````

gets translated to

````json
{
  "$schema": "http://json-schema.org/draft-07/schema",
  "type": "object",
  "title": "Example Schema",
	
  "required": [
    "playerNames"
  ],
	
  "properties": {
    "playerNames": { 
      "type": "array",
      "items":{ "type": "string" }
    }
  },
	
  "additionalProperties": false
}
````

## Restricting values, Enums and Nullability

Values of a field can be restricted by ``minimum``/``maximum`` for integers or by regular expression ``patterns``.

Enums can be used by restricting the values of a string to the enum constants

````kotlin
enum class Direction {
  LEFT,
  RIGHT
}

data class Example(
  val direction: Direction
)
````

gets translated to

````json
{
  "$schema": "http://json-schema.org/draft-07/schema",
  "type": "object",
  "title": "Example Schema",
	
  "required": [
    "direction"
  ],
	
  "properties": {
    "playerNames": { 
      "type": "string",
      "enum": ["LEFT", "RIGHT"]
    }
  },
	
  "additionalProperties": false
}
````

Nullability can be expressed by using ``oneOf``.

````json
{
  "$schema": "http://json-schema.org/draft-07/schema",
  "type": "object",
  "title": "Example Schema",
	
  "required": [
    "direction"
  ],
	
  "properties": {
    "playerNames": {
      "oneOf": [
        { "type": "null" },
        { "type": "string",
          "enum": ["LEFT", "RIGHT"] }
      ]
    }
  },
	
  "additionalProperties": false
}
````

## Schema nesting

For attributes of object types, these objects need to be defined by a schema as well. Consider the following example:

````kotlin
enum class CardColor {
  RED,
  BLACK
}

data class PlayingCard(
  val cardColor: CardColor,
  val cardValue: Int
) 

data class CardStack(
  val cards: List<PlayingCard>
)
````

The class ``CardStack`` uses the class ``PlayingCard``. Therefor the schema for ``PlayingCard`` has to be nested into 
the schema for ``CardStack``.

The sub-schema for ``PlayingCard`` looks as follows:

````json
{
  "type": "object",
	
  "required": [
    "cardColor",
    "cardValue"
  ],
	
  "properties": {
    "cardColor": {
      "type": "string",
      "enum": [
        "RED",
        "BLACK"
      ]
    },
    "cardValue":{
      "type": "integer",
      "minimum": 0,
      "maximum": 12
    } 
  },
	
  "additionalProperties": false
}
````

The schema for ``CardStack`` referencing the nested schema looks as follows:

````json
{
  "$schema": "http://json-schema.org/draft-07/schema",
  "type": "object",
  "title": "Playing Card",
	
  "required": [
    "cards"
  ],
	
  "properties": {
    "cards": {
      "type": "array",
      "items": { "$ref": "#/definitions/playingCard" }
  }
  },
	
  "additionalProperties": false,

  "definitions": {
    "playingCard": {
      "type": "object",
	    
      "required": [
        "cardColor",
        "cardValue"
      ],

      "properties": {
        "cardColor": {
          "type": "string",
          "enum": [
            "RED",
            "BLACK"
          ]
        }, 
        "cardValue":{
          "type": "integer",
          "minimum": 0,
          "maximum": 12
        }
      }
    }
  }
}
````