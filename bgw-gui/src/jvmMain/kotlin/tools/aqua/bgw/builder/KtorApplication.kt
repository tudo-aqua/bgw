package tools.aqua.bgw.builder

import PropData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.script
import kotlinx.serialization.encodeToString
import jsonMapper
import java.time.Duration
import java.util.concurrent.CopyOnWriteArrayList

const val PORT = 8080

val activeSessions = CopyOnWriteArrayList<WebSocketSession>()
fun HTML.index() {
    body {
        script(src = "/static/bgw-gui.js") {}
    }
}
fun Application.module() {
    configureRouting()
    configureSockets()
}
fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        webSocket("/ws") {
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

fun onClientMessage(session: WebSocketSession, text: String) {
    //println("Received message from client $session: $text")
}

fun onClientError(session: WebSocketSession, e: ClosedSendChannelException) {
    //println("Client $session disconnected: ${e.message}")
}

suspend fun onClientConnected(webSocketSession: WebSocketSession) {
    //println("Client connected: $webSocketSession")
    val scene = checkNotNull(Frontend.boardGameScene)
    scene.fonts = Frontend.loadedFonts
    val json = jsonMapper.encodeToString(PropData(SceneMapper.map(scene)))
    webSocketSession.send(json)
}

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        static("/static") {
            resources()
        }
    }
}

suspend fun sendToAllClients(message: String) {
    //println("Sending message to all clients: $message")
    for (session in activeSessions) {
        //println("Sending message to client $session: $message")
        try {
            session.send(message)
        } catch (e: ClosedSendChannelException) {
            // Handle session closed
        }
    }
}

