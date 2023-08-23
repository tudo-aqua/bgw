package tools.aqua.bgw

import ComponentView
import Scene
import kotlinx.browser.document
import kotlinx.serialization.decodeFromString
import mapper
import org.w3c.dom.WebSocket
import react.*
import react.dom.client.createRoot
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.components.App
import kotlin.math.floor
import kotlin.random.Random

fun main() {
    val container = document.createElement("div")
    container.id = "root"
    document.body!!.appendChild(container)
    val webSocket = WebSocket("ws://localhost:8080/ws")
    val root = createRoot(container)
    root.render(App.create())
    webSocket.onmessage = { event ->
        val scene = mapper.decodeFromString<Scene<ComponentView>>(event.data.toString())
        root.render(App.create { components = scene.components.map { NodeBuilder.build(it) } })
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