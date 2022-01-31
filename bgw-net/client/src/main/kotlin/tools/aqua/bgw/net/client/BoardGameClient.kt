package tools.aqua.bgw.net.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import tools.aqua.bgw.net.common.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import kotlin.Exception

class BoardGameClient<IG, GA, EG>(
	val playerName: String,
	private val secret: String,
	private val initGameClass: Class<IG>,
	private val gameActionClass: Class<GA>,
	private val endGameClass: Class<EG>,
	host: String,
	port: Int,
	endpoint: String = "chat",
) {
	private val mapper = ObjectMapper().registerModule(kotlinModule())

	private val wsClient: WebSocketClient = MyWebSocketClient(URI.create("ws://$host:$port/$endpoint"))


	private inner class MyWebSocketClient(uri: URI) : WebSocketClient(uri) {
		override fun onOpen(handshakedata: ServerHandshake?) {
			this@BoardGameClient.onOpen?.invoke()
		}

		override fun onMessage(message: String?) {
			requireNotNull(message) {
				//TODO exception handling
			}
			val bgwMessage: Message = mapper.readValue(message, Message::class.java)
			messageMapping(bgwMessage)

		}

		override fun onClose(code: Int, reason: String?, remote: Boolean) {
			println("closed with code: $code and reason: $reason")
			this@BoardGameClient.onClose?.invoke(code, reason ?: "n/a", remote)
		}

		override fun onError(ex: Exception?) {
			println(ex?.localizedMessage)
		}

		private fun messageMapping(message: Message) {
			when (message) {
				is Request -> throw Exception("Client received a request") //TODO error handling
				is InitializeGameMessage -> onInitializeGameReceived?.invoke(
					mapper.readValue(message.payload, initGameClass),
					message.sender
				)
				is EndGameMessage -> onEndGameReceived?.invoke(
					mapper.readValue(message.payload, endGameClass),
					message.sender
				)
				is GameActionMessage -> onGameActionReceived?.invoke(
					mapper.readValue(message.payload, gameActionClass),
					message.sender
				)
				is InitializeGameResponse -> onInitializeGameResponse?.invoke(message)
				is EndGameResponse -> onEndGameResponse?.invoke(message)
				is CreateGameResponse -> onCreateGameResponse?.invoke(message)
				is GameActionResponse -> onGameActionResponse?.invoke(message)
				is JoinGameResponse -> onJoinGameResponse?.invoke(message)
				is LeaveGameResponse -> onLeaveGameResponse?.invoke(message)
				is UserJoinedNotification -> onUserJoined?.invoke(message)
				is UserDisconnectedNotification -> onUserLeft?.invoke(message)
			}
		}
	}

	var onOpen: (() -> Unit)? = null

	var onClose: ((code: Int, reason: String, remote: Boolean) -> Unit)? = null


	fun connect() {
		wsClient.addHeader("PlayerName", playerName)
		wsClient.addHeader("SoPraSecret", secret)
		wsClient.connectBlocking()
	}

	fun disconnect() {
		wsClient.closeBlocking()
	}


	fun createGame(gameID: String, sessionID: String) {
		val message: Message = CreateGameMessage(gameID, sessionID)
		wsClient.send(mapper.writeValueAsString(message))
	}

	var onCreateGameResponse: ((CreateGameResponse) -> Unit)? = null


	fun joinGame(sessionID: String, greetingMessage: String) {
		val message: Message = JoinGameMessage(sessionID, greetingMessage)
		wsClient.send(mapper.writeValueAsString(message))
	}

	var onJoinGameResponse: ((JoinGameResponse) -> Unit)? = null


	fun leaveGame(goodbyeMessage: String) {
		val message: Message = LeaveGameMessage(goodbyeMessage)
		wsClient.send(mapper.writeValueAsString(message))
	}

	var onLeaveGameResponse: ((LeaveGameResponse) -> Unit)? = null

	private fun send(message: String) {
		wsClient.send(message)
	}

	fun sendGameActionMessage(payload: GA) {
		send(mapper.writeValueAsString(GameActionMessage(mapper.writeValueAsString(payload), payload.toString(), "")))
	}

	fun sendInitializeGameMessage(payload: IG) {
		send(
			mapper.writeValueAsString(
				InitializeGameMessage(
					mapper.writeValueAsString(payload),
					payload.toString(),
					""
				)
			)
		)
	}

	fun sendEndGameMessage(payload: EG) {
		send(mapper.writeValueAsString(EndGameMessage(mapper.writeValueAsString(payload), payload.toString(), "")))
	}

	var onGameActionResponse: ((GameActionResponse) -> Unit)? = null

	var onGameActionReceived: ((payload: GA, sender: String) -> Unit)? = null


	var onInitializeGameResponse: ((InitializeGameResponse) -> Unit)? = null

	var onInitializeGameReceived: ((payload: IG, sender: String) -> Unit)? = null


	var onEndGameResponse: ((EndGameResponse) -> Unit)? = null

	var onEndGameReceived: ((payload: EG, sender: String) -> Unit)? = null

	var onUserJoined: ((UserJoinedNotification) -> Unit)? = null

	var onUserLeft: ((UserDisconnectedNotification) -> Unit)? = null
}