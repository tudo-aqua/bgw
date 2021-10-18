package tools.aqua.bgw.net.client

import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
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

class BoardGameClient() : CoroutineScope, Closeable {
	private val client = HttpClient {
		install(WebSockets)
	}
	override val coroutineContext: CoroutineContext
		get() = client.coroutineContext
	override fun close() {
		client.close()
	}
	suspend fun startGame(host : String, port : Int) : BoardGameSession {
		val session = BoardGameSession(client.webSocketSession(method = HttpMethod.Get, host = host, port = port, path = "/chat"))
		session.setup()
		return session
	}
}

class BoardGameSession(private val defaultClientWebSocketSession: DefaultClientWebSocketSession) {
	var onMessageReceived : ((String) -> Unit)? = null

	suspend fun send(msg: String) {
		defaultClientWebSocketSession.send(msg)
	}

	internal suspend fun setup() {
		defaultClientWebSocketSession.launch {
			defaultClientWebSocketSession.outputMessages()
		}
	}

	private suspend fun DefaultClientWebSocketSession.outputMessages() {
		try {
			for (message in incoming) {
				message as? Frame.Text ?: continue
				val receivedText = message.readText()
				onMessageReceived?.invoke(receivedText)
			}
		} catch (e: Exception) {
			println("Error while receiving: " + e.localizedMessage)
		}
	}
}
