package tools.aqua.bgw.main

import SceneMapper
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
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
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

val scene = BoardGameScene(1920.0, 1080.0, ColorVisual.GREEN).apply {
    /*val label = Label(visual=ColorVisual.RED, width = 200, height = 200, text = "Hello, SoPra!")
    val label2 = Label(posX=200, posY=200, visual=ColorVisual.BLUE, width = 200, height = 200, text = "Hello, SoPra!")

    val pane = Pane<ComponentView>(posX=400, posY=0, visual=ColorVisual.MAGENTA, width = 300, height = 500)
    val button = Button(posX=50, posY=50, visual=ColorVisual.ORANGE, width = 200, height = 200, text = "Click")
    val button2 = Button(posX=50, posY=250, visual=ColorVisual.ORANGE, width = 200, height = 200, text = "Click 2")
    pane.addAll(button, button2)*/

    val hexPane = HexagonGrid<HexagonView>(posX=400, posY=0, visual=ColorVisual.WHITE, width = 300, height = 500, coordinateSystem = HexagonGrid.CoordinateSystem.OFFSET)
    val hex1 = HexagonView(posX=900, posY=0, visual=ColorVisual.MAGENTA, size = 100.0)
    val hex2 = HexagonView(posX=900, posY=200, visual=ColorVisual.RED, size = 100.0)
    val hex3 = HexagonView(posX=900, posY=400, visual=ColorVisual.BLUE, size = 100.0)
    val hex4 = HexagonView(posX=900, posY=600, visual=ColorVisual.CYAN, size = 100.0)
    val hexNew = HexagonView(posX=900, posY=600, visual=ColorVisual.BLACK, size = 100.0)

    hexPane[-1, 0] = hex1
    hexPane[1, 0] = hex2
    hexPane[0, 1] = hex3
    hexPane[1, 1] = hex4
    hexPane[0, -2] = hexNew

    addComponents(hexPane)
}

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
            println("New session: $this")
            var json : String = ""
            try {
                json = mapper.encodeToString(SceneMapper.map(scene))
            } catch (e : Exception) {
                println("Exception: $e")
            }

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

fun main() {
    /* Frontend */
    println("Starting server...")
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

