package tools.aqua.bgw.main

import javafx.application.Application
import javafx.concurrent.Worker
import javafx.scene.Scene
import javafx.scene.web.WebView
import javafx.stage.Stage
import netscape.javascript.JSObject

class Application(
    private val width: Double = 1280.0,
    private val height: Double = 720.0,
) : Application() {

    private fun addConsoleLogListener(webView: WebView) {
        val window = webView.engine.executeScript("window") as JSObject
        window.setMember("javaApp", this) // Expose Java object to JavaScript
        webView.engine.executeScript(
            "console.log = function(message) { javaApp.consoleLog(message); };"
        )
    }

    override fun start(primaryStage: Stage) {
        val rootComponent = WebView()


        // Get the WebView's WebEngine and enable JavaScript
        rootComponent.engine.isJavaScriptEnabled = true

        // Add a console listener to capture console messages
        rootComponent.engine.loadWorker.stateProperty().addListener { _, _, newValue ->
            if (newValue === Worker.State.SUCCEEDED) {
                addConsoleLogListener(rootComponent)
            }
        }

        rootComponent.apply {
            prefWidth = width
            prefHeight = height
            engine.load("http://localhost:$PORT/")
        }

        /*rootComponent.onMouseClicked = EventHandler { event ->
            runBlocking { sendToAllClients("Mouse clicked at ${event.x}, ${event.y}") }
        }*/

        primaryStage.scene = Scene(rootComponent, width, height)
        primaryStage.show()
    }

    fun show() = launch()

    fun consoleLog(message: String) {
        println("JavaScript console: $message")
    }
}