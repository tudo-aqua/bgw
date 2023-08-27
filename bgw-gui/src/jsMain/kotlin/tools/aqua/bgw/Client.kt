package tools.aqua.bgw


import SceneData
import data.event.MouseEventData
import data.event.internal.LoadEventData
import kotlinx.browser.document
import kotlinx.serialization.decodeFromString
import jsonMapper
import org.w3c.dom.WebSocket
import org.w3c.dom.asList
import org.w3c.dom.events.Event
import react.*
import react.dom.render
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.elements.App
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.LoadEvent
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.event.MouseButtonType
import webViewType
import kotlin.math.floor
import kotlin.random.Random

var webSocket : WebSocket? = null

fun main() {
    val container = document.createElement("div")
    container.id = "root"
    document.body!!.appendChild(container)
    webSocket = WebSocket("ws://localhost:8080/ws")
    webSocket?.onopen = { println("Connected to Server via WebSocket!") }
    webSocket?.onmessage = { event ->
        println("Received: ${event.data}")
        val scene = jsonMapper.decodeFromString<SceneData>(event.data.toString())
        println("Decoded: $scene")
        val sceneComponents = scene.components.map { NodeBuilder.build(it) }
        println("Built: $sceneComponents")
        render(App.create { data = scene }, container, callback = {
            println("Rendered App to DOM!")
            when(webViewType) {
                WebViewType.JCEF -> { JCEFEventDispatcher.dispatchEvent(LoadEventData()) }
                WebViewType.JAVAFX -> container.dispatchEvent(Event("bgwLoaded"))
            }
            Unit
        })
    }
}

fun List<ReactElement<*>>.toFC() = FC<Props> { appendChildren(this@toFC) }
fun ChildrenBuilder.appendChildren(components: List<ReactElement<*>>) = components.forEach { +it }

fun randomHexColor(): String {
    val chars = "0123456789ABCDEF"
    var color = "#"
    repeat(6) {
        color += chars[floor(Random.nextDouble() * 16).toInt()]
    }
    return color
}