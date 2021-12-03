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
	val client = BoardGameClient("Fabian", "127.0.0.1", 8080)
	client.connect()
}

class BoardGameClient(val playerName: String, host: String, port: Int, endpoint: String = "chat") {

	private val wsClient: WebSocketClient = MyWebSocketClient(URI.create("ws://$host:$port/$endpoint"))

	private inner class MyWebSocketClient(uri: URI) : WebSocketClient(uri) {
		override fun onOpen(handshakedata: ServerHandshake?) {
			println("opened: $handshakedata")
		}

		override fun onMessage(message: String?) {
			requireNotNull(message) {
				//TODO exception handling
			}
			val bgwMessage: Message = Json.decodeFromString(message)
			messageMapping(bgwMessage)

		}

		override fun onClose(code: Int, reason: String?, remote: Boolean) {
			println("closed with code: $code and reason: $reason")
		}

		override fun onError(ex: Exception?) {
			println(ex?.localizedMessage)
		}

		private fun messageMapping(message: Message) {
			when (message) {
				is Request -> throw Exception("Client received a request") //TODO error handling
				is InitializeGameMessage -> onInitializeGameReceived?.invoke(message.payload)
				is InitializeGameResponse -> onInitializeGameResponse?.invoke(message)
				is EndGameMessage -> onEndGameReceived?.invoke(message.payload)
				is EndGameResponse -> onEndGameResponse?.invoke(message)
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

	fun connect() {
		wsClient.addHeader("PlayerName", playerName)
		wsClient.addHeader("SoPraSecret", "geheim")
		wsClient.connectBlocking()
//		val initMessage : Message = InitializeConnectionRequest(name = playerName)
//		wsClient.send(Json.encodeToString(initMessage))
	}

	fun disconnect() {
		wsClient.closeBlocking()
	}


	fun createGame(gameID: String, sessionID: String) {
		val message: Message = CreateGameMessage(gameID, sessionID)
		wsClient.send(Json.encodeToString(message))
	}

	var onCreateGameResponse: ((CreateGameResponse) -> Unit)? = null


	fun joinGame(sessionID: String, password: String, greetingMessage: String) {
		val message: Message = JoinGameMessage(sessionID, password, greetingMessage)
		wsClient.send(Json.encodeToString(message))
	}

	var onJoinGameResponse: ((JoinGameResponse) -> Unit)? = null


	fun leaveGame() {
		val message: Message = LeaveGameMessage
		wsClient.send(Json.encodeToString(message))
	}

	var onLeaveGameResponse: ((LeaveGameResponse) -> Unit)? = null

	private fun send(message: String) {
		wsClient.send(message)
	}

	@PublishedApi
	internal fun sendGameActionMessage(payload: JsonElement, pretty: String) {
		send(Json.encodeToString<Message>(GameActionMessage(payload, pretty)))
	}

	inline fun <reified T> sendGameActionMessage(payload: T) {
		sendGameActionMessage(Json.encodeToJsonElement(payload), payload.toString())
	}

	@PublishedApi
	internal fun sendInitializeGameMessage(payload: JsonElement) {
		send(Json.encodeToString<Message>(InitializeGameMessage(payload)))
	}

	inline fun <reified T> sendInitializeGameMessage(payload: T) {
		sendInitializeGameMessage(Json.encodeToJsonElement(payload))
	}

	@PublishedApi
	internal fun sendEndGameMessage(payload: JsonElement) {
		send(Json.encodeToString<Message>(EndGameMessage(payload)))
	}

	inline fun <reified T> sendEndGameMessage(payload: T) {
		sendEndGameMessage(Json.encodeToJsonElement(payload))
	}

	var onGameActionResponse: ((GameActionResponse) -> Unit)? = null

	var onGameActionReceived: ((payload: JsonElement) -> Unit)? = null


	var onInitializeGameResponse: ((InitializeGameResponse) -> Unit)? = null

	var onInitializeGameReceived: ((payload: JsonElement) -> Unit)? = null


	var onEndGameResponse: ((EndGameResponse) -> Unit)? = null

	var onEndGameReceived: ((payload: JsonElement) -> Unit)? = null

	var onUserJoined: ((UserJoinedNotification) -> Unit)? = null

	var onUserLeft: ((UserDisconnectedNotification) -> Unit)? = null
}