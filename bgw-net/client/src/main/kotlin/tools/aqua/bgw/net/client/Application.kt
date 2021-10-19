package tools.aqua.bgw.net.client

import Message
import Request
import Response
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class BoardGameClient() :  Closeable {
	private val client = HttpClient {
		install(WebSockets)
	}

	override fun close() {
		client.close()
	}

	suspend fun startGame(host: String, port: Int): BoardGameSession {
		return BoardGameSession(
			client.webSocketSession(
				method = HttpMethod.Get,
				host = host,
				port = port,
				path = "/chat"
			), client
		)
	}
}

class BoardGameSession(private val defaultClientWebSocketSession: DefaultClientWebSocketSession, private val scope: CoroutineScope) {
	var onResponseReceived : ((Response) -> Unit)? = null
	private val input : Channel<Message> = Channel(Channel.UNLIMITED)

	init {
		scope.launch {
			val messageOutputRoutine = launch { defaultClientWebSocketSession.outputMessages() }
			val userInputRoutine = launch { defaultClientWebSocketSession.inputMessages(input) }

			userInputRoutine.join() // Wait for completion; either input is closed or error
			messageOutputRoutine.cancelAndJoin() // Cancel receiver
			defaultClientWebSocketSession.close()
		}
	}

	fun send(request: Request) {
		scope.launch { input.send(request) }
	}

	fun close() {
		input.close()
	}

	private suspend fun DefaultClientWebSocketSession.outputMessages() {
		try {
			for (message in incoming) {
				when(message) {
					is Frame.Text -> {
						when(val received : Message = Json.decodeFromString(message.readText())) {
							is Response -> onResponseReceived?.invoke(received)
							is Request -> throw Exception("Client can't handle Requests") //TODO improve Error handling
						}

					}
					else -> continue
				}

			}
		} catch (e: Exception) {
			println("Error while receiving: " + e.localizedMessage)
		}
	}

	private suspend fun DefaultClientWebSocketSession.inputMessages(input: Channel<Message>) {
		for (data in input) {
			try {
				this.send(Json.encodeToString(data))
			} catch (e: Exception) {
				println("Error while sending: " + e.localizedMessage)
				return
			}
		}
	}
}
