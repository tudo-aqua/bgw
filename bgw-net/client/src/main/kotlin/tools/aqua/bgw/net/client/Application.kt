package tools.aqua.bgw.net.client

import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext

fun main() {
	val client = BoardGameClient()
	runBlocking {
		val session = client.startGame("127.0.0.1", 8080)
		session.onMessageReceived = { println(it) }
		session.send("Hello World!")
	}
	client.close()
	println("Connection closed. Goodbye!")
}

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
	var onMessageReceived : ((String) -> Unit)? = null
	private val input : Channel<String> = Channel(Channel.UNLIMITED)

	init {
		scope.launch {
			val messageOutputRoutine = launch { defaultClientWebSocketSession.outputMessages() }
			val userInputRoutine = launch { defaultClientWebSocketSession.inputMessages(input) }

			userInputRoutine.join() // Wait for completion; either input is closed or error
			messageOutputRoutine.cancelAndJoin() // Cancel receiver
			defaultClientWebSocketSession.close()
		}
	}

	fun send(msg: String) {
		scope.launch { input.send(msg) }
	}

	fun close() {
		input.close()
	}

	private suspend fun DefaultClientWebSocketSession.outputMessages() {
		try {
			for (message in incoming) {
				when(message) {
					is Frame.Text -> {
						val receivedText = message.readText()
						message.readText()
						onMessageReceived?.invoke(receivedText)
					}
					else -> continue
				}

			}
		} catch (e: Exception) {
			println("Error while receiving: " + e.localizedMessage)
		}
	}

	private suspend fun DefaultClientWebSocketSession.inputMessages(input: Channel<String>) {
		for (data in input) {
			try {
				send(data)
			} catch (e: Exception) {
				println("Error while sending: " + e.localizedMessage)
				return
			}
		}
	}
}
