package tools.aqua.bgw.application

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
import java.awt.BorderLayout
import java.awt.EventQueue
import java.awt.KeyboardFocusManager
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame

class JCEFApplication : Application, JFrame() {
    override fun start(callback: (Any) -> Unit) {
        EventQueue.invokeLater {
            val frame: JFrame = MainFrame(loadCallback = callback)
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE)
            frame.isVisible = true
        }
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun registerMouseEventListener(component: ComponentView) {
        TODO("Not yet implemented")
    }

}
class MainFrame(
    startURL: String = "http://localhost:8080",
    useOSR: Boolean = false,
    isTransparent: Boolean = false,
    loadCallback: (Any) -> Unit
) : JFrame() {
    private var browserFocus = true

    init {
        val builder = CefAppBuilder()
        builder.cefSettings.windowless_rendering_enabled = useOSR
        builder.setAppHandler(object : MavenCefAppHandlerAdapter() {
            override fun stateHasChanged(state: CefAppState) {
                if (state == CefAppState.TERMINATED) System.exit(0)
            }
        })
        val cefApp = builder.build()
        val client = cefApp.createClient()
        val config = CefMessageRouterConfig()
        config.jsQueryFunction = "cefQuery"
        config.jsCancelFunction = "cefQueryCancel"
        val msgRouter = CefMessageRouter.create()
        client.addMessageRouter(msgRouter)
        val myHandler: CefMessageRouterHandler = object : CefMessageRouterHandlerAdapter() {
            override fun onQuery(browser: CefBrowser, frame: CefFrame, query_id: Long, request: String, persistent: Boolean, callback: CefQueryCallback): Boolean {
                if(request == "bgwLoaded") loadCallback.invoke(Unit)
                return true
            }
        }
        msgRouter.addHandler(myHandler, true)
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
