package tools.aqua.bgw


import AnimationData
import AppData
import ComponentViewData
import Data
import DialogData
import ID
import JsonData
import PropData
import SceneData
import VisualData
import data.event.AnimationFinishedEventData
import data.event.internal.LoadEventData
import kotlinx.browser.document
import kotlinx.serialization.decodeFromString
import jsonMapper
import kotlinx.browser.window
import org.w3c.dom.CustomEvent
import web.timers.setTimeout
import org.w3c.dom.HTMLElement
import org.w3c.dom.WebSocket
import org.w3c.dom.events.Event
import org.w3c.dom.get
import react.*
import react.dom.render
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.elements.App
import tools.aqua.bgw.event.JCEFEventDispatcher
import web.dom.Element
import web.serviceworker.Client
import webViewType
import kotlin.js.Json
import kotlin.math.floor
import kotlin.random.Random

var internalSocket : WebSocket? = null
var webSocket : WebSocket? = null
var handlers : MutableMap<ID, (Data) -> Unit> = mutableMapOf()
var animator : Animator = Animator()

lateinit var container : HTMLElement

fun main() {
    if (Config.USE_SOCKETS) {
        webSocket = WebSocket("ws://${document.location?.host}/ws")
        webSocket?.onopen = { }
        webSocket?.onmessage = { event ->
            val cont = document.getElementById("bgw-root")
            if (cont != null) {
                container = cont as HTMLElement
                val receivedData = jsonMapper.decodeFromString<PropData>(event.data.toString()).data
                handleReceivedData(receivedData!!)
            }
        }
    } else {
        document.addEventListener("BGW_MSG", {
            val event = it as CustomEvent
            val data = event.detail
            val jsonData = jsonMapper.decodeFromString<JsonData>(data.toString())
            val receivedData = jsonData.props.data
            val containerId = jsonData.container

            val cont = document.getElementById(containerId)
            if (cont != null) {
                container = cont as HTMLElement
                handleReceivedData(receivedData!!)
            }
        })
    }
}

fun handleReceivedData(receivedData: Data) {
    when (receivedData) {
        is AppData -> {
            val app = receivedData

            if (app.action == Action.HIDE_MENU_SCENE) {
                console.log("[SCENE] Hiding Menu Scene")
                val element = document.querySelector("#menuScene") as HTMLElement
                element.classList.toggle("scene--visible", false)
                setTimeout({
                    renderApp(app)
                }, 300)
            } else if (app.action == Action.SHOW_MENU_SCENE) {
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
            animator.startAnimation(receivedData) {
                JCEFEventDispatcher.dispatchEvent(AnimationFinishedEventData().apply { id = it })
            }
        }
        else -> {}
    }
}

fun renderApp(appData : AppData) {
    render(App.create { data = appData }, container as Element, callback = {
        JCEFEventDispatcher.dispatchEvent(LoadEventData())
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