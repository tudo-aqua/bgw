import kotlinx.browser.document
import kotlinx.serialization.decodeFromString
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.WebSocket
import org.w3c.dom.get
import react.*
import react.dom.client.createRoot
import react.dom.html.ReactHTML.h1

fun main() {
    val container = document.createElement("div")

    container.id = "root"

    document.body!!.appendChild(container)

    val webSocket = WebSocket("ws://localhost:8080/ws")

    webSocket.onopen = {
        println("Connected to server")
    }

    console.log("Hello from Kotlin/JS")
    val root = createRoot(container)
    root.render(Welcome.create())

    webSocket.onmessage = { event ->
        println("Received message from server")
        println(event.data.toString())
        val scene = mapper.decodeFromString<Scene<ComponentView>>(event.data.toString())
        val components = scene.components.map { NodeBuilder.build(it) }

        root.render(components(components).create())

        null
    }

}

fun components(components: List<ReactElement<*>>) = FC<Props> { components(components) }

fun ChildrenBuilder.components(components: List<ReactElement<*>>) = components.forEach { +it }


object NodeBuilder {
    fun build(componentView: ComponentView): ReactElement<*> {
        return when (componentView) {
            is Button -> ButtonBuilder.build(componentView)
            else -> throw Exception("Unknown component type")
        }
    }
}

object ButtonBuilder {
    fun build(componentView: ComponentView): ReactElement<*> {
        return ReactButton.create {
            id = componentView.id
        }
    }
}
