package tools.aqua.bgw.net.client

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tools.aqua.bgw.net.common.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import kotlin.Exception

fun main() {
	val client = BoardGameClient("127.0.0.1", 8080, "chat")
	client.connect()
	Thread.sleep(1000)
	client.send("Hello")
	Thread.sleep(1000)
	client.send("Hello")
}

class BoardGameClient(host: String, port: Int, endpoint: String) {
	private val wsClient: WebSocketClient = MyWebSocketClient(URI.create("ws://$host:$port/$endpoint"))

	inner class MyWebSocketClient(uri: URI) : WebSocketClient(uri) {
		override fun onOpen(handshakedata: ServerHandshake?) {
			println("opened: $handshakedata")
		}

		override fun onMessage(message: String?) {
			println("$message")
//			requireNotNull(message) {
//				//TODO exception handling
//			}
//			val bgwMessage : Message = Json.decodeFromString(message)
//			messageMapping(bgwMessage)
		}

		override fun onClose(code: Int, reason: String?, remote: Boolean) {
			println("closed with code: $code and reason: $reason")
		}

		override fun onError(ex: Exception?) {
			println(ex?.localizedMessage)
		}

		private fun messageMapping(message: Message) {
			when(message) {
				is Request -> throw Exception("Client received a request") //TODO error handling
				is GameActionMessage -> onGameActionReceived?.invoke(message.payload)
				is CreateGameResponse -> onCreateGameResponse?.invoke(message)
				is GameActionResponse -> onGameActionResponse?.invoke(message)
				is JoinGameResponse -> onJoinGameResponse?.invoke(message)
				is LeaveGameResponse -> onLeaveGameResponse?.invoke(message)
				is UserJoinedNotification -> onUserJoined?.invoke(message)
				is UserDisconnectedNotification -> onUserLeft?.invoke(message)
			}
		}
	}

	fun send(string: String) {
		wsClient.send(string)
	}

	fun connect() {
		wsClient.connect()
	}

	fun disconnect() {
		wsClient.closeBlocking()
	}


	fun createGame(gameID: String, sessionID : String, password : String) {
		val message : Message = CreateGameMessage(gameID, sessionID, password)
		wsClient.send(Json.encodeToString(message))
	}

	var onCreateGameResponse : ((CreateGameResponse) -> Unit)? = null


	fun joinGame(sessionID: String, password: String, greeting : String) {
		val message : Message = JoinGameMessage(sessionID, password, greeting)
		wsClient.send(Json.encodeToString(message))
	}

	var onJoinGameResponse : ((JoinGameResponse) -> Unit)? = null


	fun leaveGame() {
		val message : Message = LeaveGameMessage
		wsClient.send(Json.encodeToString(message))
	}

	var onLeaveGameResponse : ((LeaveGameResponse) -> Unit)? = null

	fun sendGameActionMessage(payload: JsonElement) {
		val msg : Message = GameActionMessage(Json.encodeToJsonElement(payload))
		wsClient.send(Json.encodeToString(msg))
	}

	var onGameActionResponse : ((GameActionResponse) -> Unit)? = null

	var onGameActionReceived : ((payload: JsonElement) -> Unit)? = null


	var onUserJoined : ((UserJoinedNotification) -> Unit)? = null

	var onUserLeft : ((UserDisconnectedNotification) -> Unit)? = null
}