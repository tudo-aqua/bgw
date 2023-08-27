package tools.aqua.bgw.application

import ID
import data.event.EventData
import data.event.KeyEventAction
import data.event.KeyEventData
import data.event.MouseEventData
import data.event.internal.LoadEventData
import jsonMapper
import kotlinx.serialization.decodeFromString
import me.friwi.jcefmaven.CefAppBuilder
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter
import org.cef.CefApp
import org.cef.CefApp.CefAppState
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.browser.CefMessageRouter
import org.cef.browser.CefMessageRouter.CefMessageRouterConfig
import org.cef.callback.CefQueryCallback
import org.cef.handler.CefFocusHandlerAdapter
import org.cef.handler.CefLoadHandlerAdapter
import org.cef.handler.CefMessageRouterHandler
import org.cef.handler.CefMessageRouterHandlerAdapter
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.event.MouseEvent
import java.awt.BorderLayout
import java.awt.EventQueue
import java.awt.KeyboardFocusManager
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import kotlin.system.exitProcess

class JCEFApplication : Application {
    private var frame : MainFrame? = null

    private val handlersMap = mutableMapOf<ID, CefMessageRouterHandler>()

    override fun start(callback: (Any) -> Unit) {
        EventQueue.invokeLater {
            frame = MainFrame(loadCallback = callback)
            frame?.defaultCloseOperation = EXIT_ON_CLOSE
            frame?.isVisible = true
        }
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun registerEventListeners(component: ComponentView) {
        println(handlersMap.keys.size)
        if (handlersMap.containsKey(component.id)) return
        val handler: CefMessageRouterHandler = object : CefMessageRouterHandlerAdapter() {
            override fun onQuery(browser: CefBrowser, frame: CefFrame, query_id: Long, request: String, persistent: Boolean, callback: CefQueryCallback): Boolean {
                val json = Base64.decode(request)
                val eventData = jsonMapper.decodeFromString<EventData>(json)
                if(eventData.id != component.id) return false
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
                }
                return true
            }
        }
        handlersMap[component.id] = handler
        frame?.msgRouter?.addHandler(handler, false)
    }

}
class MainFrame(
    startURL: String = "http://localhost:8080",
    useOSR: Boolean = false,
    isTransparent: Boolean = false,
    loadCallback: (Any) -> Unit
) : JFrame() {
    private var browserFocus = true

    var msgRouter : CefMessageRouter? = null

    init {
        val builder = CefAppBuilder()
        builder.cefSettings.windowless_rendering_enabled = useOSR
        builder.setAppHandler(object : MavenCefAppHandlerAdapter() {
            override fun stateHasChanged(state: CefAppState) {
                if (state == CefAppState.TERMINATED) exitProcess(0)
            }
        })
        val cefApp = builder.build()
        val client = cefApp.createClient()
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
        val browser = client.createBrowser(startURL, useOSR, isTransparent)
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
            override fun onLoadEnd(browser: CefBrowser, frame: CefFrame, httpStatusCode: Int) {
                if (frame.isMain) {
                    //browser.executeJavaScript("alert('Hello World!');", browser.getURL(), 0);
                }
            }
        })
        contentPane.add(browserUI, BorderLayout.CENTER)
        pack()
        setSize(1280, 720)
        isVisible = true
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                CefApp.getInstance().dispose()
                dispose()
            }
        })
    }
}
