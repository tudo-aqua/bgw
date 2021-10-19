import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonElement

//Message
@Serializable
sealed class Message()

@Serializable
sealed class Request : Message()

@Serializable
data class CreateGameRequest(val password: String, val numPlayers: Int) : Request()

@Serializable
data class JoinGameRequest(val id: Int, val password: String, val name: String, val playerIndex: Int) : Request()

@Serializable
data class GameMessageRequest(val payload: JsonElement) : Request()


//Response
@Serializable
sealed class Response() : Message()

@Serializable
data class CreateGameResponse(val state : State, val id: Int?) : Response()

@Serializable
data class JoinGameResponse(val state: State) : Response()


//State
@Serializable
sealed class State

@Serializable
object Success : State()

@Serializable
class Failure(val reason: String) : State() //TODO replace String type of reason with something useful
