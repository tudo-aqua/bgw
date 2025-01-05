package tools.aqua.bgw.binding

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedSendChannelException
import java.util.concurrent.CopyOnWriteArrayList

internal class Channel(val path: String = "/ws") {
    val activeSessions = CopyOnWriteArrayList<WebSocketSession>()
    var onClientConnected: suspend (WebSocketSession) -> Unit = { }
    var onClientMessage: (session: WebSocketSession, text: String) -> Unit = { _, _ -> }
    val onClientError: (session: WebSocketSession, e: ClosedSendChannelException) -> Unit = { _, _ -> }

    suspend fun sendToAllClients(message: String) {
        //println("Sending message to all clients: $message")
        for (session in activeSessions) {
            try {
                session.send(message)
            } catch (e: ClosedSendChannelException) {
                println("Client $session disconnected: ${e.message}")
            }
        }
    }

    fun install(application: Application) {
        with(application) {
            routing {
                webSocket(path) {
                    activeSessions.add(this)
                    onClientConnected(this)
                    try {
                        for (frame in incoming) {
                            if (frame is Frame.Text) {
                                val text = frame.readText()
                                onClientMessage(this, text)
                            }
                        }
                    } catch (e: ClosedSendChannelException) {
                        onClientError(this, e)
                    } finally {
                        activeSessions.remove(this)
                    }
                }
            }
        }
    }
}