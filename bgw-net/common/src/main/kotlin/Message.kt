import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonElement

//Message
@Serializable
sealed class Message()

@Serializable
data class CreateGame(val password: String, val numPlayers: Int) : Message()

@Serializable
data class JoinGame(val id: Int, val password: String, val name: String, val playerIndex: Int) : Message()

@Serializable
data class GameMessage(val payload: JsonElement)


//Response
@Serializable
sealed class Response()

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
