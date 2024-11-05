package tools.aqua.bgw.application

import AnimationData
import DialogData
import ID
import InternalCameraPaneData
import data.event.*
import data.event.internal.*
import data.event.internal.LoadEventData
import jsonMapper
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import mapper.DialogMapper
import me.friwi.jcefmaven.CefAppBuilder
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter
import org.cef.CefApp
import org.cef.CefApp.CefAppState
import org.cef.CefClient
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.browser.CefMessageRouter
import org.cef.browser.CefMessageRouter.CefMessageRouterConfig
import org.cef.callback.CefContextMenuParams
import org.cef.callback.CefMenuModel
import org.cef.callback.CefQueryCallback
import org.cef.handler.*
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.Frontend
import tools.aqua.bgw.core.findComponent
import tools.aqua.bgw.core.getRootNode
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.DialogType
import tools.aqua.bgw.event.AnimationFinishedEvent
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.event.MouseEvent
import tools.aqua.bgw.util.Coordinate
import java.awt.BorderLayout
import java.awt.Component
import java.awt.EventQueue
import java.awt.KeyboardFocusManager
import java.awt.event.*
import java.lang.management.ManagementFactory
import java.net.ServerSocket
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import kotlin.system.exitProcess

object Constants {
    val PORT = ServerSocket(0).use { it.localPort }
    // val PORT = 5173
}

class JCEFApplication : Application {
    private var frame : MainFrame? = null

    private val handlersMap = mutableMapOf<ID, CefMessageRouterHandler>()

    override fun start(callback: (Any) -> Unit) {
        println("Starting JCEF Application on port ${Constants.PORT}")
        EventQueue.invokeLater {
            frame = MainFrame(loadCallback = callback)
            JCEFApplication::class.java.getResource("/icon.png").let { ImageIO.read(it) }.let { frame?.iconImage = it }
            frame?.title = "BoardGameWork Application"

            frame?.defaultCloseOperation = EXIT_ON_CLOSE
            frame?.isVisible = true
        }
    }

    override fun stop() {
        frame?.dispose()
        CefApp.getInstance().dispose()
    }

    override fun clearAllEventListeners() {
        handlersMap.forEach { (_, handler) ->
            frame?.msgRouter?.removeHandler(handler)
        }
        handlersMap.clear()
    }

    fun getTitle(): String {
        return frame?.title ?: ""
    }

    fun setTitle(title: String) {
        frame?.title = title
    }

    override fun registerEventListeners(component: ComponentView) {
        if (handlersMap.containsKey(component.id)) return
        val handler: CefMessageRouterHandler = object : CefMessageRouterHandlerAdapter() {
            override fun onQuery(browser: CefBrowser, frame: CefFrame, query_id: Long, request: String, persistent: Boolean, callback: CefQueryCallback): Boolean {


                val json = Base64.decode(request)
                val eventData = jsonMapper.decodeFromString<EventData>(json)
                if(eventData.id != component.id) return false
                //println("Received: $eventData for ${component.id}")

                try {
                    when(eventData) {
                        is MouseEventData -> component.onMouseClicked?.invoke(MouseEvent(eventData.button, eventData.posX,eventData.posY))
                        is KeyEventData -> {
                            val keyEvent = KeyEvent(eventData.keyCode, eventData.character, eventData.isControlDown, eventData.isShiftDown, eventData.isAltDown)
                            when(eventData.action) {
                                KeyEventAction.PRESS -> component.onKeyPressed?.invoke(keyEvent)
                                KeyEventAction.RELEASE -> component.onKeyReleased?.invoke(keyEvent)
                                KeyEventAction.TYPE -> component.onKeyTyped?.invoke(keyEvent)
                            }
                        }
                        is SelectionChangedEventData -> {
                            //println("Selection changed")
                            if(component is ComboBox<*>) component.select(eventData.selectedItem)
                        }
                        is StructuredDataSelectEventData -> {
                            if(component is StructuredDataView<*>) component.select(eventData.index)
                        }
                        is TextInputChangedEventData -> {
                            //println("Text changed")
                            if(component is TextField) component.textProperty.value = eventData.value
                        }
                        is ColorInputChangedEventData -> {
                            //println("Text changed")
                            if(component is ColorPicker) component.selectedColor = Color(eventData.value)
                        }
                        is CheckBoxChangedEventData -> {
                            if(component is CheckBox) component.isCheckedProperty.value = eventData.value
                        }
                        is RadioChangedEventData -> {
                            if(component is BinaryStateButton) component.isSelected = eventData.value
                        }
                        is ScrollChangedEventData -> {
                            if(component is CameraPane<*>) {
                                component.anchorPointProperty.setInternal(Coordinate(eventData.scrollLeft, eventData.scrollTop))
                            }
                        }
                        is ZoomChangedEventData -> {
                            if(component is CameraPane<*>) {
                                component.zoomProperty.setInternal(eventData.zoomLevel)
                            }
                        }
                        is InternalCameraPaneData -> {
                            if(component is CameraPane<*>) {
                                component.internalData = eventData
                            }
                        }
                        is DragGestureStartedEventData -> {
                            if(component is DynamicComponentView) {
                                component.onDragGestureStarted?.invoke(DragEvent(component))
                            }
                        }
                        is DragDroppedEventData -> {
                            val root = component.getRootNode()
                            val target = root.findComponent(eventData.target)
                            val dropped = target?.dropAcceptor?.invoke(DragEvent(component))
                            if(dropped == true) target.onDragDropped?.invoke(DragEvent(component))
                            //TODO: Call back to JS
                        }
                    }

                } catch(e : Exception) {
                    val mainFrame = this@JCEFApplication.frame
                    mainFrame?.openNewDialog(
                        DialogMapper.map(
                            Dialog(
                                DialogType.ERROR,
                                "Error",
                                "Error",
                                "An error occurred while handling an event: ${e.message}"
                            )
                        )
                    )
                }
                return true
            }
        }
        handlersMap[component.id] = handler
        frame?.msgRouter?.addHandler(handler, false)
    }

    override fun openNewDialog(dialogData: DialogData) {
        frame?.openNewDialog(dialogData)
    }

}

@OptIn(ExperimentalSerializationApi::class)
class MainFrame(
    startURL: String = "http://localhost",
    useOSR: Boolean = false,
    isTransparent: Boolean = false,
    loadCallback: (Any) -> Unit
) : JFrame() {
    private var browserFocus = true

    var msgRouter : CefMessageRouter? = null
    var animationMsgRouter : CefMessageRouter? = null
    var client : CefClient

    var dialogMap : MutableMap<CefBrowser, DialogData> = mutableMapOf()

    init {
        val builder = CefAppBuilder()
        builder.cefSettings.windowless_rendering_enabled = useOSR
        builder.setAppHandler(object : MavenCefAppHandlerAdapter() {
            override fun stateHasChanged(state: CefAppState) {
                // println("CEF State: $state")
                if (state == CefAppState.TERMINATED) {
                    // println("CEF Terminated")
                    exitProcess(0)
                }
            }
        })

        val cefApp = builder.build()
        client = cefApp.createClient()
        /* Component Update Message Router */
        val config = CefMessageRouterConfig()
        config.jsQueryFunction = "cefQuery"
        config.jsCancelFunction = "cefQueryCancel"
        msgRouter = CefMessageRouter.create(config)
        client.addMessageRouter(msgRouter)
        val myHandler: CefMessageRouterHandler = object : CefMessageRouterHandlerAdapter() {
            override fun onQuery(browser: CefBrowser, frame: CefFrame, query_id: Long, request: String, persistent: Boolean, callback: CefQueryCallback): Boolean {
                val json = Base64.decode(request)
                val eventData = jsonMapper.decodeFromString<EventData>(json)
                if(eventData is LoadEventData) {
                    loadCallback.invoke(Unit)
                    return true
                }
                return false
            }
        }
        msgRouter?.addHandler(myHandler, true)

        /* Animation Message Router */
        val animationConfig = CefMessageRouterConfig()
        animationConfig.jsQueryFunction = "cefAnimationQuery"
        animationConfig.jsCancelFunction = "cefAnimationQueryCancel"
        animationMsgRouter = CefMessageRouter.create(animationConfig)
        client.addMessageRouter(animationMsgRouter)
        val animationHandler: CefMessageRouterHandler = object : CefMessageRouterHandlerAdapter() {
            override fun onQuery(browser: CefBrowser, frame: CefFrame, query_id: Long, request: String, persistent: Boolean, callback: CefQueryCallback): Boolean {
                val json = Base64.decode(request)
                val eventData = jsonMapper.decodeFromString<AnimationFinishedEventData>(json)
                if(eventData is AnimationFinishedEventData) {
                    val menuSceneAnimations = Frontend.menuScene?.animations?.toList() ?: listOf()
                    val boardGameSceneAnimations = Frontend.boardGameScene?.animations?.toList() ?: listOf()
                    val animations = menuSceneAnimations + boardGameSceneAnimations
                    val animation = animations.find { it.id == eventData.id }
                    animation?.onFinished?.invoke(AnimationFinishedEvent())
                    return true
                }
                return false
            }
        }
        animationMsgRouter?.addHandler(animationHandler, true)

        val browser = client.createBrowser("$startURL:${Constants.PORT}", useOSR, isTransparent)
        val browserUI = browser.uiComponent
        client.addFocusHandler(object : CefFocusHandlerAdapter() {
            override fun onGotFocus(browser: CefBrowser) {
                if (browserFocus) return
                browserFocus = true
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner()
                browser.setFocus(true)
            }

            override fun onTakeFocus(browser: CefBrowser, next: Boolean) {
                browserFocus = false
            }
        })
        client.addLoadHandler(object : CefLoadHandlerAdapter() {
            override fun onLoadEnd(browserArg: CefBrowser, frame: CefFrame, httpStatusCode: Int) {
                if(browserArg != browser) {
                    val validBrowser = dialogMap.keys.find { it == browserArg }
                    val dialogData = dialogMap[validBrowser]

                    if(dialogData != null && validBrowser != null) {
                        setDialogContent(validBrowser, dialogData)
                        dialogMap.remove(validBrowser)
                    }
                }
            }
        })
        client.addContextMenuHandler(object : CefContextMenuHandlerAdapter() {
            override fun onBeforeContextMenu(browser: CefBrowser, frame: CefFrame, params: CefContextMenuParams, model: CefMenuModel) {
                model.clear()
            }
        })
        contentPane.add(browserUI, BorderLayout.CENTER)
        pack()
        setSize(1280, 720)
        isVisible = true
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                // println("Application shutting down...")
                // println(CefApp.getState())
                CefApp.getInstance().dispose()
                // println(CefApp.getState())
                dispose()
            }

            override fun windowOpened(e: WindowEvent?) {
                super.windowOpened(e)
            }
        })
    }

    fun setDialogContent(browser : CefBrowser, dialogData : DialogData) {
        browser.executeJavaScript(
            """
                document.write(`
                    <html>
                        <head>        
                            <style>
                                body {
                                    background-color: #f0f0f0;
                                    font-family: sans-serif;
                                }
                            </style>
                        </head>
                        <body>
                            <h1>${dialogData.header}</h1>
                            <p>${dialogData.message}</p>
                            <code>${dialogData.exception}</code>
                            <button onClick="window.close()">Close</button>
                        </body>
                    </html>`);
            """.trimIndent(),
            browser.url,
            0
        )
    }

    fun openNewDialog(dialogData: DialogData) {
        val dialogFrame = client.createBrowser("about:blank", false, false)
        val dialogUI = dialogFrame.uiComponent

        dialogMap[dialogFrame] = dialogData

        val dialog = JFrame()
        dialog.title = dialogData.message
        dialog.iconImage = iconImage
        dialog.contentPane.add(dialogUI, BorderLayout.CENTER)
        dialog.pack()
        dialog.setSize(600, 300)
        dialog.isVisible = true
    }
}
