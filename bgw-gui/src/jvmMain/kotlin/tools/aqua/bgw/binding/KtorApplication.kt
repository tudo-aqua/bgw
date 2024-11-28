package tools.aqua.bgw.binding

import ActionProp
import PropData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import jsonMapper
import kotlinx.coroutines.*
import kotlinx.html.*
import tools.aqua.bgw.application.JCEFApplication
import tools.aqua.bgw.core.Frontend
import java.time.Duration

val componentChannel: Channel = Channel("/ws").apply {
    onClientConnected = {
        Frontend.application
        val json = jsonMapper.encodeToString(PropData(SceneMapper.map(
            menuScene= Frontend.menuScene,
            gameScene= Frontend.boardGameScene
        ).apply {
            fonts = Frontend.loadedFonts.map { (path, fontName, weight) ->
                Triple(path, fontName, weight.toInt())
            }
        }))
        it.send(json)
        if(!uiJob.isActive) uiJob.start()
    }
}

val internalChannel: Channel = Channel("/internal").apply {
    onClientMessage = { session, text ->

    }
}

fun HTML.index() {
    body {
        div {
            classes = setOf("bgw-root-container")
            div {
                classes = setOf("bgw-portal")
                id = "bgw-portal"
            }
            div {
                classes = setOf("bgw-root")
                id = "bgw-root"
            }
        }
        script(src = "/static/bgw-gui.js") {}
    }
}

fun Application.module() {
    configureRouting()
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    componentChannel.install(this)
    internalChannel.install(this)
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

internal val messageQueue = mutableListOf<ActionProp>()

fun CoroutineScope.launchPeriodicAsync(
    repeatMillis: Long,
    action: (suspend () -> Unit)
) = this.async {
    if (repeatMillis > 0) {
        while (isActive) {
            action()
            delay(repeatMillis)
        }
    } else {
        action()
    }
}

var uiJob = CoroutineScope(Dispatchers.IO).launchPeriodicAsync(10) {
    if((Frontend.applicationEngine as JCEFApplication).getTitle() !== Frontend.application.title) {
        (Frontend.applicationEngine as JCEFApplication).setTitle(Frontend.application.title)
    }

    if (messageQueue.isNotEmpty()) {
        val isSceneLoaded = Frontend.boardGameScene != null
        val message = messageQueue.removeFirst()
        val result = runCatching {
            val appData = SceneMapper.map(menuScene= Frontend.menuScene, gameScene= Frontend.boardGameScene).apply {
                fonts = Frontend.loadedFonts.map { (path, fontName, weight) ->
                    Triple(path, fontName, weight.toInt())
                }
            }
            appData.action = message
            val json = jsonMapper.encodeToString(PropData(appData))
            runBlocking { componentChannel.sendToAllClients(json) }
        }
        messageQueue.clear()
    }
}

