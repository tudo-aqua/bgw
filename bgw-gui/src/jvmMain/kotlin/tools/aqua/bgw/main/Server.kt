package tools.aqua.bgw.main

import Button
import ColorVisual
import ComponentView
import Scene
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.html.*
import kotlinx.serialization.encodeToString
import mapper
import tools.aqua.bgw.builder.Frontend
import java.time.Duration
import java.util.concurrent.CopyOnWriteArrayList
import io.ktor.server.application.Application as KtorApplication


fun HTML.index() {
    body {
        script(src = "/static/bgw-gui.js") {}
    }
}

fun KtorApplication.module() {
    configureRouting()
    configureSockets()
}

fun KtorApplication.configureRouting() {
    routing {
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        static("/static") {
            resources()
        }
    }
}

val activeSessions = CopyOnWriteArrayList<WebSocketSession>()

fun KtorApplication.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        webSocket("/ws") {
            activeSessions.add(this) // Store the WebSocket session
            val json = mapper.encodeToString(Frontend.menuScene ?: Frontend.gamePane ?: Scene<ComponentView>(0.0, 0.0, ColorVisual.BLACK))
            println("Sending scene: $json")
            this.send(json)
            try {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        println("Received: $text")
                    }
                }
            } catch (e: ClosedSendChannelException) {
                // Handle session closed
            } finally {
                activeSessions.remove(this) // Remove the WebSocket session
            }
        }
    }
}

const val PORT = 8080

val scene = Scene<ComponentView>(1920.0, 1080.0, ColorVisual.RED).apply {
    repeat(1_000) {
        components.add(Button(0.0, 0.0, 100.0, 100.0, ColorVisual.BLUE))
    }
}

fun main() {
    /* Frontend */
    embeddedServer(Netty, port = PORT, host = "localhost", module = KtorApplication::module).start(wait = false)
    Application().show()
}

suspend fun sendToAllClients(message: String) {
    println("Sending message to all clients: $message")
    for (session in activeSessions) {
        println("Sending message to client $session: $message")
        try {
            session.send(message)
        } catch (e: ClosedSendChannelException) {
            // Handle session closed
        }
    }
}

