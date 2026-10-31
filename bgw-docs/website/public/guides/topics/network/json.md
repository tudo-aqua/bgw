[json-schema]: https://json-schema.org/understanding-json-schema/
[tutorialspoint]: https://www.tutorialspoint.com/json/json_schema.htm
[types]: https://json-schema.org/understanding-json-schema/reference/type.html

<!-- GH-Pages Doc -->

[CommunicationDoc]: /guides/network/communication

# JSON

<tldr>Validate your messages with JSON schemas</tldr>

## Introduction

Every message that a client sends through the BGW server is a `GameAction` serialized as JSON, see [Network communication][CommunicationDoc]. The server does not know your classes, but it can reject malformed messages before they reach the other players. For this it uses **JSON schemas**: a description of how a valid message looks.

For every game that is registered on the server, you provide one schema per message class. When a message arrives, the server looks for the schema with the same `@class` as the message. If it finds none, the message is checked against all schemas of the game and accepted if **at least one** matches. A message that matches no schema is rejected and the sender receives a `GameActionResponse` with the status `INVALID_JSON`.

> Schemas are uploaded together with a game ID in the _Games and Schemas_ view of the BGW server's web interface (administrators only). The view checks every uploaded schema against the BGW meta schema, which is described [below](#bgw-meta-schema).
> {style="note"}

## External resources

A documentation for JSON schema can be found [here][json-schema].<br>
A quick start guide can be found [here][tutorialspoint].

---

## JSON data types

The following [data types][types] can be used in a JSON schema:

| Type    | Values allowed     | Values not allowed |
| ------- | ------------------ | ------------------ |
| string  | "foo"              | 1                  |
| number  | 1; 1.1; 1.1e5      | ""                 |
| integer | 1; 1.0             | 1.1                |
| object  | Nested objects     |                    |
| array   | ["1", "2"]; [1, 2] | ""; 1              |
| boolean | true; false        | "true"; 1          |
| null    | null               | ""                 |

---

## A simple message

Consider the following message class:

```kotlin
@GameActionClass
data class GameOverGameAction(
    val winner: String
) : GameAction() {
    override fun formatMessage(): String = "$winner has won the game!"
}
```

Its schema contains all properties of the class. Here there is only the field `winner` of type `string`:

```json
{
  "$schema": "http://json-schema.org/draft-07/schema",
  "type": "object",
  "@class": "com.example.diceduel.GameOverGameAction",
  "title": "Game Over Schema",

  "required": ["winner"],

  "properties": {
    "winner": { "type": "string" }
  },

  "additionalProperties": false
}
```

| Field | Meaning |
| --- | --- |
| `$schema` | The JSON schema version. BGW uses `draft-07`. |
| `type` | A message is always an `object`. |
| `@class` | The **fully qualified name** of the Kotlin class. The server uses it to find the schema of a message. |
| `title` | A readable name of the schema. It is shown in error messages. |
| `required` | All fields that must be present. For a Kotlin class that is every property without a default value. |
| `properties` | The type of every field. |
| `additionalProperties` | `false` forbids all fields that are not listed. |

### BGW meta schema

On top of the standard JSON schema rules, a schema that is uploaded to a BGW server must contain the two fields `title` and `@class`, both of type `string`. A schema without them is rejected when it is uploaded.

---

## Lists and arrays

For lists and arrays the type is `array`. The type of the elements is declared with `items`.

```kotlin
@GameActionClass
data class PlayersGameAction(
    val playerNames: List<String>
) : GameAction() {
    override fun formatMessage(): String? = null
}
```

```json
{
  "$schema": "http://json-schema.org/draft-07/schema",
  "type": "object",
  "@class": "com.example.diceduel.PlayersGameAction",
  "title": "Players Schema",

  "required": ["playerNames"],

  "properties": {
    "playerNames": {
      "type": "array",
      "items": { "type": "string" }
    }
  },

  "additionalProperties": false
}
```

---

## Restricting values, enums and nullability

Numbers can be restricted with `minimum` and `maximum`, strings with a regular expression in `pattern`. An **enum** becomes a string that is limited to the names of its constants:

```kotlin
enum class Direction { LEFT, RIGHT }

@GameActionClass
data class MoveGameAction(
    val direction: Direction,
    val steps: Int
) : GameAction() {
    override fun formatMessage(): String? = null
}
```

```json
{
  "$schema": "http://json-schema.org/draft-07/schema",
  "type": "object",
  "@class": "com.example.diceduel.MoveGameAction",
  "title": "Move Schema",

  "required": ["direction", "steps"],

  "properties": {
    "direction": {
      "type": "string",
      "enum": ["LEFT", "RIGHT"]
    },
    "steps": {
      "type": "integer",
      "minimum": 1,
      "maximum": 6
    }
  },

  "additionalProperties": false
}
```

A field that can be `null` is declared with `oneOf`. Nullable fields should still be listed in `required`, because they are always part of the serialized message:

```json
"direction": {
  "oneOf": [
    { "type": "null" },
    { "type": "string", "enum": ["LEFT", "RIGHT"] }
  ]
}
```

---

## Nesting schemas

Fields with object types need a schema for the object as well. Reusable parts are put into `definitions` and referenced with `$ref`:

```kotlin
enum class CardColor { RED, BLACK }

data class PlayingCard(
    val cardColor: CardColor,
    val cardValue: Int
)

@GameActionClass
data class PlayCardsGameAction(
    val cards: List<PlayingCard>
) : GameAction() {
    override fun formatMessage(): String? = null
}
```

```json
{
  "$schema": "http://json-schema.org/draft-07/schema",
  "type": "object",
  "@class": "com.example.diceduel.PlayCardsGameAction",
  "title": "Play Cards Schema",

  "required": ["cards"],

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

      "required": ["cardColor", "cardValue"],

      "properties": {
        "cardColor": {
          "type": "string",
          "enum": ["RED", "BLACK"]
        },
        "cardValue": {
          "type": "integer",
          "minimum": 0,
          "maximum": 12
        }
      },

      "additionalProperties": false
    }
  }
}
```

> The BGW server repository contains the complete schemas of the MauMau example in `bgw-net-server/src/main/resources/maumauschemas`. They are a good starting point for your own game.
> {style="note"}
