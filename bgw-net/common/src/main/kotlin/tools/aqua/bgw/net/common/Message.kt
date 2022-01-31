package tools.aqua.bgw.net.common

import com.fasterxml.jackson.annotation.JsonTypeInfo

//Message
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
sealed class Message

sealed class GameMessage() : Message()

data class InitializeGameMessage(val payload: String, val prettyPrint: String, val sender: String) : GameMessage()

data class GameActionMessage(val payload: String, val prettyPrint: String, val sender: String) : GameMessage()

data class EndGameMessage(val payload: String, val prettyPrint: String, val sender: String) : GameMessage()

//Request
sealed class Request : Message()

data class CreateGameMessage(val gameID: String, val sessionID : String) : Request()

data class JoinGameMessage(val sessionId: String, val greeting: String) : Request()

data class LeaveGameMessage(val goodbyeMessage: String) : Request()

//Response
sealed class Response : Message()

data class InitializeGameResponse(val status: GameMessageStatus, val errorMessages: List<String>?) : Response() //TODO error message

data class GameActionResponse(val status: GameMessageStatus, val errorMessages: List<String>?) : Response() //TODO error message

data class EndGameResponse(val status: GameMessageStatus, val errorMessages: List<String>?) : Response() //TODO error message

data class CreateGameResponse(val responseStatus: CreateGameResponseStatus) : Response() //TODO error message

data class JoinGameResponse(val responseStatus: JoinGameResponseStatus) : Response() //TODO error message

data class LeaveGameResponse(val responseStatus: LeaveGameResponseStatus) : Response() //TODO error message

//Status
enum class CreateGameResponseStatus {
	/**
	 * Created the game successfully.
	 */
	SUCCESS,

	/**
	 * This connection was already associated with a game on the server. No Game was created.
	 */
	ALREADY_ASSOCIATED_WITH_GAME,

	/**
	 * A Game with the specified ID already exists on the server. No Game was created.
	 */
	GAME_WITH_ID_ALREADY_EXISTS,

	/**
	 * Something on the server went wrong. No Game was created.
	 */
	SERVER_ERROR
}

enum class JoinGameResponseStatus {
	/**
	 * Joined the game successfully.
	 */
	SUCCESS,

	/**
	 * This connection is already associated with a game on the server and can not join another game at this time.
	 * Disconnect from the current game to connect to another game.
	 */
	ALREADY_ASSOCIATED_WITH_GAME,

	/**
	 * No game with the specified id was found on the server.
	 */
	INVALID_ID,

	/**
	 * A player with the same player name is already part of the game.
	 */
	PLAYER_NAME_ALREADY_TAKEN,

	/**
	 * Something on the server went wrong.
	 */
	SERVER_ERROR
}

enum class LeaveGameResponseStatus {
	/**
	 * Disconnected from the game successfully.
	 */
	SUCCESS,

	/**
	 * This connection was not associated with a game.
	 */
	NO_ASSOCIATED_GAME,

	/**
	 * Something on the server went wrong.
	 */
	SERVER_ERROR
}

enum class GameMessageStatus {
	/**
	 * The message was valid and broadcast to all other connected players.
	 */
	SUCCESS,

	/**
	 * This connection was not associated with a game.
	 */
	NO_ASSOCIATED_GAME,

	/**
	 * The specified JSON schema was not found on the server.
	 * Message was rejected.
	 */
	SCHEMA_NOT_FOUND,

	/**
	 * the payload did not match the specified schema.
	 * Message was rejected.
	 */
	INVALID_JSON,

	/**
	 * Something went wrong on the server.
	 */
	SERVER_ERROR
}

//Notification
sealed class Notification : Message()

data class UserJoinedNotification(val greetingMessage: String, val sender: String) : Notification()

data class UserDisconnectedNotification(val goodbyeMessage: String, val sender: String) : Notification()
