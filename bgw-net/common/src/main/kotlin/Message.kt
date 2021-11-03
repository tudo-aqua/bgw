import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

//Message
@Serializable
sealed class Message

@Serializable
data class GameMessage(val payload: JsonElement) : Message()

//Request
@Serializable
sealed class Request : Message()

@Serializable
data class CreateGameRequest(val password: String) : Request()

@Serializable
data class JoinGameRequest(val id: String, val password: String, val name: String) : Request()

@Serializable
object DisconnectFromGameRequest : Request()

//Response
@Serializable
sealed class Response : Message()

@Serializable
data class GameMessageResponse(val state: GameMessageState) : Response()

@Serializable
data class CreateGameResponse(val state: CreateGameState, val id: String?) : Response()

@Serializable
data class JoinGameResponse(val state: JoinGameState) : Response()

@Serializable
data class DisconnectFromGameResponse(val state: DisconnectFromGameState) : Response()

//State
@Serializable
enum class CreateGameState {
	SUCCESS,
	ALREADY_ASSOCIATED_WITH_GAME,
	SERVER_ERROR
}

@Serializable
enum class JoinGameState {
	SUCCESS,
	ALREADY_ASSOCIATED_WITH_GAME,
	INVALID_ID,
	INVALID_PASSWORD,
	SERVER_ERROR
}

@Serializable
enum class DisconnectFromGameState {
	SUCCESS,
	NO_ASSOCIATED_GAME,
	SERVER_ERROR
}

@Serializable
enum class GameMessageState {
	SUCCESS,
	SCHEMA_NOT_FOUND,
	INVALID_JSON,
	SERVER_ERROR
}

//Notification
@Serializable
sealed class Notification : Message()

@Serializable
data class UserJoinedNotification(val name: String) : Notification()

@Serializable
data class UserDisconnectedNotification(val name: String) : Notification()

@Serializable
object GameEndedNotification : Notification()
