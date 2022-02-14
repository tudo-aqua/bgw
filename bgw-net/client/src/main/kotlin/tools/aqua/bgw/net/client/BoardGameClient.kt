package tools.aqua.bgw.net.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import tools.aqua.bgw.net.common.*
import java.net.URI

open class BoardGameClient<IG, GA, EG>(
	val playerName: String,
	secret: String,
	private val initGameClass: Class<IG>,
	private val gameActionClass: Class<GA>,
	private val endGameClass: Class<EG>,
	host: String,
	port: Int,
	endpoint: String = "chat",
) {
	private val mapper = ObjectMapper().registerModule(kotlinModule())

	private val wsClient: WebSocketClient = MyWebSocketClient(URI.create("ws://$host:$port/$endpoint"))

	init {
		wsClient.addHeader("PlayerName", playerName)
		wsClient.addHeader("SoPraSecret", secret)
	}

	private inner class MyWebSocketClient(uri: URI) : WebSocketClient(uri) {
		override fun onOpen(handshakedata: ServerHandshake?) {
			this@BoardGameClient.onOpen?.invoke()
		}

		override fun onMessage(message: String?) {
			try {
				val bgwMessage: Message = mapper.readValue(message, Message::class.java)
				messageMapping(bgwMessage)
			} catch (e: Exception) {
				onError?.invoke(e)
			}
		}

		override fun onClose(code: Int, reason: String?, remote: Boolean) {
			this@BoardGameClient.onClose?.invoke(code, reason ?: "n/a", remote)
		}

		override fun onError(ex: Exception?) {
			onError?.invoke(ex!!)
		}

		private fun messageMapping(message: Message) {
			when (message) {
				is Request -> throw Exception("Client received a request")
				is InitializeGameMessage ->
					onGameActionReceived?.invoke(
						mapper.readValue(message.payload, gameActionClass),
						message.sender

					)

				is EndGameMessage ->
					onGameActionReceived?.invoke(
						mapper.readValue(message.payload, gameActionClass),
						message.sender

					)

				is GameActionMessage ->
					onGameActionReceived?.invoke(
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

	var onError: ((throwable: Throwable) -> Unit)? = { throw it }

	var onOpen: (() -> Unit)? = null

	var onClose: ((code: Int, reason: String, remote: Boolean) -> Unit)? = null


	fun connect() {
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