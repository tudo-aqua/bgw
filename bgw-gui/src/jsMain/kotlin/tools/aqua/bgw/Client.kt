package tools.aqua.bgw

import ActionProp
import AnimationData
import AppData
import Data
import ID
import JsonData
import PropData
import data.event.AnimationFinishedEventData
import data.event.internal.LoadEventData
import jsonMapper
import kotlinx.browser.document
import org.w3c.dom.CustomEvent
import org.w3c.dom.HTMLElement
import org.w3c.dom.WebSocket
import react.*
import react.dom.client.Root
import react.dom.client.createRoot
import react.dom.render
import tools.aqua.bgw.elements.App
import tools.aqua.bgw.event.JCEFEventDispatcher
import web.dom.Element
import web.timers.setTimeout
import kotlin.math.floor
import kotlin.random.Random

internal var internalSocket: WebSocket? = null
internal var webSocket: WebSocket? = null
internal var handlers: MutableMap<ID, (Data) -> Unit> = mutableMapOf()
internal var animator: Animator = Animator()

internal lateinit var container: HTMLElement
internal lateinit var root: Root

internal fun main() {
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

internal fun handleReceivedData(receivedData: Data) {
    when (receivedData) {
        is AppData -> {
            val app = receivedData

            if (app.action == ActionProp.HIDE_MENU_SCENE) {
                console.log("[SCENE] Hiding Menu Scene")
                val element = document.querySelector("#menuScene") as HTMLElement
                element.classList.toggle("scene--visible", false)
                setTimeout({
                    if(!Config.USE_SOCKETS) {
                        renderApp(app)
                    } else {
                        renderAppFast(app)
                    }
                }, 300)
            } else if (app.action == ActionProp.SHOW_MENU_SCENE) {
                if(!Config.USE_SOCKETS) {
                    renderApp(app)
                } else {
                    renderAppFast(app)
                }
                val element = document.querySelector("#menuScene") as HTMLElement
                setTimeout({
                    element.classList.toggle("scene--visible", true)
                }, 50)
            } else {
                if(!Config.USE_SOCKETS) {
                    renderApp(app)
                } else {
                    renderAppFast(app)
                }
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

/**
 * Renders the app with React 17 syntax to provide fallback for BGW Playground web app.
 */
internal fun renderApp(appData: AppData) {
    render(App.create { data = appData }, container as Element, callback = {
        JCEFEventDispatcher.dispatchEvent(LoadEventData())
    })
}

/**
 * Renders the app with React 18 syntax.
 */
internal fun renderAppFast(appData: AppData) {
    if(!::root.isInitialized) {
        root = createRoot(container as Element)
    }
    root.render(App.create { data = appData })
    JCEFEventDispatcher.dispatchEvent(LoadEventData())
}

internal fun List<ReactElement<*>>.toFC() = FC<Props> { appendChildren(this@toFC) }
internal fun ChildrenBuilder.appendChildren(components: List<ReactElement<*>>) = components.forEach { +it }

internal fun randomHexColor(): String {
    val chars = "0123456789ABCDEF"
    var color = "#"
    repeat(6) {
        color += chars[floor(Random.nextDouble() * 16).toInt()]
    }
    return color
}