package tools.aqua.bgw


import AnimationData
import AppData
import ComponentViewData
import Data
import ID
import PropData
import SceneData
import VisualData
import data.event.internal.LoadEventData
import kotlinx.browser.document
import kotlinx.serialization.decodeFromString
import jsonMapper
import kotlinx.js.timers.setTimeout
import org.w3c.dom.HTMLElement
import org.w3c.dom.WebSocket
import org.w3c.dom.events.Event
import org.w3c.dom.get
import react.*
import react.dom.findDOMNode
import react.dom.render
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.elements.App
import tools.aqua.bgw.event.JCEFEventDispatcher
import webViewType
import kotlin.math.floor
import kotlin.random.Random

var internalSocket : WebSocket? = null
var webSocket : WebSocket? = null
var handlers : MutableMap<ID, (Data) -> Unit> = mutableMapOf()
var animator : Animator = Animator()

val container = document.createElement("div")

fun main() {
    container.id = "root"
    document.body!!.appendChild(container)
    webSocket = WebSocket("ws://localhost:8080/ws")
    webSocket?.onopen = { //println("Connected to Server via WebSocket!")
    }
    webSocket?.onmessage = { event ->
        println("Received: ${event.data}")
        val receivedData = jsonMapper.decodeFromString<PropData>(event.data.toString()).data
        when(receivedData) {
            is AppData -> {
                val app = receivedData

                if(app.action == Action.HIDE_MENU_SCENE) {
                    console.log("[SCENE] Hiding Menu Scene")
                    val element = document.querySelector("#menuScene") as HTMLElement
                    element.classList.toggle("scene--visible", false)
                    setTimeout({
                        renderApp(app)
                    }, 300)
                } else if(app.action == Action.SHOW_MENU_SCENE) {
                    renderApp(app)
                    val element = document.querySelector("#menuScene") as HTMLElement
                    setTimeout({
                        element.classList.toggle("scene--visible", true)
                    }, 50)
                } else {
                    renderApp(app)
                }
            }
            is AnimationData -> {
                animator.startAnimation(receivedData)
            }
            /* is SceneData -> {
                val scene = receivedData
                val sceneComponents = scene.components.map { NodeBuilder.build(it) }
                //println("Built: $sceneComponents")
                println("Received SceneData with ${sceneComponents.size} components.")
                render(App.create { data = scene }, container, callback = {
                    println("Rendered App to DOM!")
                    when(webViewType) {
                        WebViewType.JCEF -> { JCEFEventDispatcher.dispatchEvent(LoadEventData()) }
                        WebViewType.JAVAFX -> container.dispatchEvent(Event("bgwLoaded"))
                    }
                    Unit
                })
            }
            is ComponentViewData -> {
                //println("Received ComponentViewData for id ${receivedData.id} with ${receivedData.visual?.id}")
                val component =  receivedData
                val handler = handlers[component.id]
                handler?.invoke(component)
            }
            is VisualData -> {
                val visual = receivedData
                val handler = handlers[visual.id]
                handler?.invoke(visual)
            } */
            else -> {}
        }
    }

    internalSocket?.onmessage = {
    }
}

fun renderApp(appData : AppData) {
    render(App.create { data = appData }, container, callback = {
        println("Rendered App to DOM!")
        when(webViewType) {
            WebViewType.JCEF -> { JCEFEventDispatcher.dispatchEvent(LoadEventData()) }
            WebViewType.JAVAFX -> container.dispatchEvent(Event("bgwLoaded"))
        }
        Unit
    })
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