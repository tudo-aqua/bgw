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
import tools.aqua.bgw.application.Application
import tools.aqua.bgw.application.FXApplication
import tools.aqua.bgw.application.JCEFApplication
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import java.time.Duration
import java.util.concurrent.CopyOnWriteArrayList
import io.ktor.server.application.Application as KtorApplication
import tools.aqua.bgw.observable.properties.Property


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

val label = Label(visual = ColorVisual.RED, width = 200, height = 200, text = "Hello, SoPra!")
val label2 = Label(posX = 200, posY = 200, visual = ColorVisual.BLUE, width = 200, height = 200, text = "Hello, SoPra!")

val pane = Pane<ComponentView>(posX = 400, posY = 0, visual = ColorVisual.MAGENTA, width = 300, height = 500)
val button = Button(posX = 50, posY = 50, visual = ColorVisual.ORANGE, width = 200, height = 200, text = "Click").apply {
    onMouseClicked = { println("Clicked Button 1!") }
}
val button2 = Button(posX = 50, posY = 250, visual = ColorVisual.ORANGE, width = 200, height = 200, text = "Click 2").apply {
    onMouseClicked = { println("Clicked Button 2!") }
}

val scene = object : BoardGameScene(1920.0, 1080.0, ColorVisual.GREEN) {
    init {
        pane.addAll(button, button2)
        addComponents(label, label2, pane)
    }
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
            var json = mapper.encodeToString(SceneMapper.map(scene))
            println("Sending scene: $json")
            this.send(json)
            try {
                for (frame in incoming) {
                    println("Incomming Message!")
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        println("Received: $text")
                        observable.notifyChange(observable.value, true)
                    }
                }
            } catch (e: ClosedSendChannelException) {
                // Handle session closed
            } finally {
                println("Session Closed")
                activeSessions.remove(this) // Remove the WebSocket session
            }
        }
    }
}

val observable: Property<Boolean> = Property(false)

const val PORT = 8080
val bgwApplication : Application = JCEFApplication()
fun main() {
    /* Frontend */
    println("Starting server...")
    embeddedServer(Netty, port = PORT, host = "localhost", module = KtorApplication::module).start(wait = false)
    /*
    bgwApplication.start {
        println("Registering events...")
        scene.components.forEach { component ->
            bgwApplication.registerMouseEventListener(component)
            when(component) {
                is Pane<*> -> {
                    component.components.forEach { bgwApplication.registerMouseEventListener(it) }
                }
                else -> {}
            }
        }
    }*/
    bgwApplication.start {
        println("Loaded")
    }
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

