package tools.aqua.bgw.application

import javafx.application.Platform
import javafx.concurrent.Worker
import javafx.scene.Scene
import javafx.scene.web.WebView
import javafx.stage.Stage
import netscape.javascript.JSObject
import org.w3c.dom.events.EventTarget
import tools.aqua.bgw.builder.PORT
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.event.MouseButtonType
import tools.aqua.bgw.event.MouseEvent
import org.w3c.dom.events.MouseEvent as W3CMouseEvent

class FXApplication(
    private val width: Double = 1280.0,
    private val height: Double = 720.0,
) : Application {

    lateinit var rootComponent: WebView

    override fun start(callback: (Any) -> Unit) {
        Platform.startup {
            val primaryStage = Stage()
            rootComponent = WebView()
            rootComponent.engine.isJavaScriptEnabled = true
            rootComponent.engine.loadWorker.stateProperty().addListener { _, _, newValue ->
                if (newValue == Worker.State.SUCCEEDED) {
                    addConsoleLogListener(rootComponent)
                    addOnLoadListener(rootComponent, callback)
                }
            }
            rootComponent.apply {
                prefWidth = width
                prefHeight = height
                engine.load("http://localhost:$PORT/")
            }
            primaryStage.scene = Scene(rootComponent, width, height)
            primaryStage.scene.userAgentStylesheet = javafx.application.Application.STYLESHEET_CASPIAN
            primaryStage.show()
        }
    }

    override fun stop() {
        Platform.exit()
    }

    override fun registerMouseEventListener(component: ComponentView) {
        val eventTarget = rootComponent.engine.document.getElementById(component.id) as EventTarget
        eventTarget.addEventListener("click", {
            if (it is W3CMouseEvent) { component.onMouseClicked?.invoke(MouseEvent(it.getButtonType(), it.clientX, it.clientY)) }
        }, false)
    }

    private fun addConsoleLogListener(webView: WebView) {
        val window = webView.engine.executeScript("window") as JSObject
        window.setMember("javaApp", object {
            fun consoleLog(message: String) {
                println("JavaScript console: $message")
            }
        }) // Expose Java object to JavaScript
        webView.engine.executeScript(
            "console.log = function(message) { javaApp.consoleLog(message); };"
        )
    }

    private fun addOnLoadListener(webView: WebView, callback: (Any) -> Unit) {
        val target = webView.engine.document.getElementById("root") as EventTarget
        target.addEventListener("bgwLoaded", callback, false)

        /*val onLoad = {
            println("Loaded event triggered")
            val btn = webView.engine.document.getElementById(button.id) as EventTarget
            btn.addEventListener("click", {
                button.onMouseClicked?.invoke(MouseEvent(MouseButtonType.MOUSE_WHEEL, 0, 0))
            }, false)
        }*/
    }
}

fun W3CMouseEvent.getButtonType(): MouseButtonType {
    return when (this.button.toInt()) {
        0 -> MouseButtonType.LEFT_BUTTON
        1 -> MouseButtonType.MOUSE_WHEEL
        2 -> MouseButtonType.RIGHT_BUTTON
        else -> MouseButtonType.OTHER
    }
}

