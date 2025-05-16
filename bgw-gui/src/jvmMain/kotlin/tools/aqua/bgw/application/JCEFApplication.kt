/*
 * Copyright 2025 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua.bgw.application

import Base64
import DialogData
import ID
import data.event.*
import dev.dirs.ProjectDirectories
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File
import java.net.ServerSocket
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.stream.Collectors
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.WindowConstants
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import jsonMapper
import kotlin.concurrent.timer
import kotlin.system.exitProcess
import me.friwi.jcefmaven.CefAppBuilder
import me.friwi.jcefmaven.CefBuildInfo
import me.friwi.jcefmaven.EnumPlatform
import me.friwi.jcefmaven.EnumProgress
import org.cef.CefApp
import org.cef.CefClient
import org.cef.CefSettings
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
import tools.aqua.bgw.core.*
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.FileDialog
import tools.aqua.bgw.dialog.FileDialogMode
import tools.aqua.bgw.event.*
import tools.aqua.bgw.mapper.DialogMapper
import tools.aqua.bgw.util.Coordinate

internal object Constants {
  val PORT = ServerSocket(0).use { it.localPort }
  const val DEBUG = false
}

internal class JCEFApplication : Application {
  var frame: MainFrame? = null

  private val handlersMap = mutableMapOf<ID, CefMessageRouterHandler>()

  override fun start(onClose: () -> Unit, callback: (Any) -> Unit) {
    if (Constants.DEBUG) println("[BGW] Starting BGW Runtime (http://localhost:${Constants.PORT})")
    else println("[BGW] Starting BGW Runtime (${Constants.PORT})")
    EventQueue.invokeLater {
      frame = MainFrame(loadCallback = callback, debugLogging = Constants.DEBUG)
      JCEFApplication::class
          .java
          .getResource("/icon.png")
          .let { ImageIO.read(it) }
          .let { frame?.iconImage = it }
      frame?.title = DEFAULT_WINDOW_TITLE

      frame?.defaultCloseOperation = EXIT_ON_CLOSE
      frame?.isVisible = true
    }

    // Call the onClose callback when the application is closed
    Runtime.getRuntime().addShutdownHook(Thread { onClose() })
  }

  override fun stop() {
    frame?.gracefulShutdown()
  }

  override fun toggleFullscreen(boolean: Boolean) {
    frame?.toggleFullscreen(boolean)
  }

  override fun toggleMaximized(boolean: Boolean) {
    frame?.extendedState = if (boolean) JFrame.MAXIMIZED_BOTH else JFrame.NORMAL
  }

  override fun resize(width: Int, height: Int) {
    frame?.setSize(width, height)
  }

  override fun clearAllEventListeners() {
    handlersMap.forEach { (_, handler) -> frame?.msgRouter?.removeHandler(handler) }
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
    val handler: CefMessageRouterHandler =
        object : CefMessageRouterHandlerAdapter() {
          override fun onQuery(
              browser: CefBrowser,
              frame: CefFrame,
              query_id: Long,
              request: String,
              persistent: Boolean,
              callback: CefQueryCallback
          ): Boolean {
            val json = Base64.decode(request)
            val eventData = jsonMapper.decodeFromString<EventData>(json)
            if (eventData.id != component.id) return false
            // println("Received: $eventData for ${eventData.id}")

            try {
              when (eventData) {
                is FilesPickedEventData -> {
                  if (Frontend.openedFileDialog != null &&
                      eventData.id == Frontend.openedFileDialog?.id) {
                    if (eventData.paths.isNotEmpty()) {
                      Frontend.openedFileDialog?.onPathsSelected?.invoke(eventData.paths)
                    } else {
                      Frontend.openedFileDialog?.onSelectionCancelled?.invoke()
                    }
                    Frontend.openedFileDialog = null
                  }
                }
                is MouseEnteredEventData -> {
                  component.onMouseEntered?.invoke(
                      MouseEvent(MouseButtonType.UNSPECIFIED, eventData.posX, eventData.posY))
                }
                is MouseExitedEventData -> {
                  component.onMouseExited?.invoke(
                      MouseEvent(MouseButtonType.UNSPECIFIED, eventData.posX, eventData.posY))
                }
                is MousePressedEventData -> {
                  component.onMousePressed?.invoke(
                      MouseEvent(eventData.button, eventData.posX, eventData.posY))
                }
                is ScrollEventData -> {
                  component.onMouseWheel?.invoke(
                      WheelEvent(
                          eventData.direction, eventData.ctrl, eventData.shift, eventData.alt))
                }
                is MouseReleasedEventData -> {
                  component.onMouseReleased?.invoke(
                      MouseEvent(eventData.button, eventData.posX, eventData.posY))
                }
                is MouseEventData ->
                    component.onMouseClicked?.invoke(
                        MouseEvent(eventData.button, eventData.posX, eventData.posY))
                is KeyEventData -> {
                  val keyEvent =
                      KeyEvent(
                          eventData.keyCode,
                          eventData.character,
                          eventData.isControlDown,
                          eventData.isShiftDown,
                          eventData.isAltDown)
                  when (eventData.action) {
                    KeyEventAction.PRESS -> component.onKeyPressed?.invoke(keyEvent)
                    KeyEventAction.RELEASE -> component.onKeyReleased?.invoke(keyEvent)
                    KeyEventAction.TYPE -> component.onKeyTyped?.invoke(keyEvent)
                  }
                }
                is SelectionChangedEventData -> {
                  // println("Selection changed")
                  if (component is ComboBox<*>) component.select(eventData.selectedItem)
                }
                is StructuredDataSelectEventData -> {
                  if (component is StructuredDataView<*>) component.selectIndex(eventData.index)
                }
                is TextInputChangedEventData -> {
                  if (component is TextInputUIComponent) {
                    component.textProperty.setSilent(eventData.value)
                    component.onTextChanged?.invoke(eventData.value)
                  }
                }
                is ColorInputChangedEventData -> {
                  // println("Text changed")
                  if (component is ColorPicker) component.selectedColor = Color(eventData.value)
                }
                is CheckBoxChangedEventData -> {
                  if (component is CheckBox) {
                    if (component.isIndeterminateAllowedProperty.value) {
                      if (eventData.value &&
                          !component.isCheckedProperty.value &&
                          !component.isIndeterminateProperty.value) {
                        component.isIndeterminate = true
                      } else if (eventData.value &&
                          component.isIndeterminateProperty.value &&
                          !component.isCheckedProperty.value) {
                        component.isIndeterminate = false
                        component.isChecked = true
                      } else if (!eventData.value &&
                          !component.isIndeterminateProperty.value &&
                          component.isCheckedProperty.value) {
                        component.isChecked = false
                      }
                    } else {
                      component.isChecked = eventData.value
                      if (component.isIndeterminateProperty.value) {
                        component.isIndeterminate = false
                      }
                    }
                  }
                }
                is RadioChangedEventData -> {
                  if (component is BinaryStateButton) component.isSelected = eventData.value
                }
                is TransformChangedEventData -> {
                  if (component is CameraPane<*>) {
                    if (component.zoomProperty.value != eventData.zoomLevel) {
                      component.onZoomed?.invoke(eventData.zoomLevel)
                    }
                    component.zoomProperty.setInternal(eventData.zoomLevel)
                    component.panDataProperty.setInternal(InternalCameraPanData())
                    component.anchorPointProperty.setInternal(
                        Coordinate(eventData.anchor.first, eventData.anchor.second))
                  }
                }
                is DragGestureStartedEventData -> {
                  if (component is DynamicComponentView) {
                    component.onDragGestureStarted?.invoke(DragEvent(component))
                    component.isDragged = true
                  }
                }
                is DragGestureMovedEventData -> {
                  if (component is DynamicComponentView) {
                    component.onDragGestureMoved?.invoke(DragEvent(component))
                  }
                }
                is DragGestureEnteredEventData -> {
                  if (component is DynamicComponentView &&
                      eventData.target.isNotBlank() &&
                      component.parent != null) {
                    val root = component.getRootNode()
                    val target = root.findComponent(eventData.target)
                    if (target?.dropAcceptor != null) {
                      target.onDragGestureEntered?.invoke(DragEvent(component))
                    }
                  }
                }
                is DragGestureEndedEventData -> {
                  if (component is DynamicComponentView && component.parent != null) {
                    if (eventData.droppedOn != null) {
                      val root = component.getRootNode()
                      val target = root.findComponent(eventData.droppedOn!!)
                      if (target?.dropAcceptor != null) {
                        component.onDragGestureEnded?.invoke(
                            DropEvent(component, target),
                            target.dropAcceptor?.invoke(DragEvent(component)) == true)
                      }
                    } else {
                      component.onDragGestureEnded?.invoke(DropEvent(component, null), false)
                    }
                  }
                }
                is DragGestureExitedEventData -> {
                  if (component is DynamicComponentView &&
                      eventData.target.isNotBlank() &&
                      component.parent != null) {
                    val root = component.getRootNode()
                    val target = root.findComponent(eventData.target)
                    if (target?.dropAcceptor != null) {
                      target.onDragGestureExited?.invoke(DragEvent(component))
                    }
                  }
                }
                is DragDroppedEventData -> {
                  val root = component.getRootNode()
                  val target = root.findComponent(eventData.target)
                  val dropped = target?.dropAcceptor?.invoke(DragEvent(component))
                  if (dropped == true) {
                    target.onDragDropped?.invoke(DragEvent(component))
                    (component as DynamicComponentView).isDragged = false
                  }
                }
              }
            } catch (e: Exception) {
              val mainFrame = this@JCEFApplication.frame
              mainFrame?.openNewDialog(
                  DialogMapper.map(
                      Dialog(
                          "Error",
                          "Error",
                          "An error occurred while handling an event: ${e.message}",
                          exception = e)))
              e.printStackTrace()
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

internal class MainFrame(
    startURL: String = "http://localhost",
    useOSR: Boolean = false,
    isTransparent: Boolean = false,
    loadCallback: (Any) -> Unit,
    debugLogging: Boolean = false
) : JFrame() {
  private var browserFocus = true
  private var fullscreenFrame: JFrame? = null

  var msgRouter: CefMessageRouter? = null
  var animationMsgRouter: CefMessageRouter? = null
  var globalEventMsgRouter: CefMessageRouter? = null
  var client: CefClient
  var activeBrowser: CefBrowser

  var dialogMap: MutableMap<CefBrowser, DialogData> = mutableMapOf()

  init {
    // region - CEF Initialization / Settings
    val builder = CefAppBuilder()
    builder.cefSettings.windowless_rendering_enabled = useOSR

    if (!debugLogging) {
      builder.cefSettings.log_severity = CefSettings.LogSeverity.LOGSEVERITY_DISABLE
    }

    val BGWAppName = "bgw-runtime_${Config.BGW_VERSION}"
    val defaultDirs = ProjectDirectories.from("bgw-gui", "tools.aqua", BGWAppName)
    val installDir = Paths.get(defaultDirs.cacheDir).toFile()
    builder.setInstallDir(installDir)

    val tmpDir: String =
        System.getProperty("tmpDir")?.takeIf { it.isNotEmpty() && File(it).exists() }
            ?: Files.createTempDirectory("bgw-").toString().also { File(it).deleteOnExit() }

    builder.cefSettings.root_cache_path = tmpDir

    builder.setProgressHandler { enumProgress, fl ->
      if (enumProgress == EnumProgress.DOWNLOADING || enumProgress == EnumProgress.EXTRACTING) {
        if (fl >= 0) print("[BGW] Downloading BGW Runtime... $fl%\r")
      } else if (enumProgress == EnumProgress.LOCATING || enumProgress == EnumProgress.INSTALL)
          println("[BGW] Initializing BGW Runtime...")
      else if (enumProgress == EnumProgress.INITIALIZING) println("[BGW] Loading BGW Runtime...")
    }

    val cefApp = builder.build()
    cefApp.setSettings(builder.cefSettings)

    val pids = mutableSetOf<Long>()
    var pidsUnchanged = 0

    val platform = EnumPlatform.getCurrentPlatform()
    println("[BGW] Platform: $platform")
    val buildInfo = CefBuildInfo.fromClasspath()
    // println("[BGW] Build: ${buildInfo.jcefUrl} ${buildInfo.releaseUrl}")
    val cefVersion = cefApp.version
    println("[BGW] Runtime Version: ${cefVersion.toString().replace(Regex("\n"), " ")}")

    client = cefApp.createClient()
    // endregion

    // region - Component Update Message Router
    val config = CefMessageRouterConfig()
    config.jsQueryFunction = "cefQuery"
    config.jsCancelFunction = "cefQueryCancel"
    msgRouter = CefMessageRouter.create(config)
    client.addMessageRouter(msgRouter)
    val myHandler: CefMessageRouterHandler =
        object : CefMessageRouterHandlerAdapter() {
          override fun onQuery(
              browser: CefBrowser,
              frame: CefFrame,
              query_id: Long,
              request: String,
              persistent: Boolean,
              callback: CefQueryCallback
          ): Boolean {
            val json = Base64.decode(request)
            val eventData = jsonMapper.decodeFromString<EventData>(json)
            if (eventData is LoadEventData) {
              loadCallback.invoke(Unit)
              return true
            }
            return false
          }
        }
    msgRouter?.addHandler(myHandler, true)
    // endregion

    // region - Global Event Message Router
    val globalEventConfig = CefMessageRouterConfig()
    globalEventConfig.jsQueryFunction = "cefSceneQuery"
    globalEventConfig.jsCancelFunction = "cefSceneQueryCancel"
    globalEventMsgRouter = CefMessageRouter.create(globalEventConfig)
    client.addMessageRouter(globalEventMsgRouter)
    val globalHandler =
        object : CefMessageRouterHandlerAdapter() {
          override fun onQuery(
              browser: CefBrowser,
              frame: CefFrame,
              query_id: Long,
              request: String,
              persistent: Boolean,
              callback: CefQueryCallback
          ): Boolean {
            val json = Base64.decode(request)
            val eventData = jsonMapper.decodeFromString<KeyEventData>(json)

            try {
              val keyEvent =
                  KeyEvent(
                      eventData.keyCode,
                      eventData.character,
                      eventData.isControlDown,
                      eventData.isShiftDown,
                      eventData.isAltDown)
              val menuScene = Frontend.menuScene
              val boardGameScene = Frontend.boardGameScene

              when (eventData.action) {
                KeyEventAction.PRESS -> {
                  menuScene?.onKeyPressed?.invoke(keyEvent)
                  boardGameScene?.onKeyPressed?.invoke(keyEvent)
                }
                KeyEventAction.RELEASE -> {
                  menuScene?.onKeyReleased?.invoke(keyEvent)
                  boardGameScene?.onKeyReleased?.invoke(keyEvent)
                }
                KeyEventAction.TYPE -> {
                  menuScene?.onKeyTyped?.invoke(keyEvent)
                  boardGameScene?.onKeyTyped?.invoke(keyEvent)
                }
              }
              return true
            } catch (e: Exception) {}

            return false
          }
        }
    globalEventMsgRouter?.addHandler(globalHandler, true)
    // endregion

    // region - Animation Message Router
    val animationConfig = CefMessageRouterConfig()
    animationConfig.jsQueryFunction = "cefAnimationQuery"
    animationConfig.jsCancelFunction = "cefAnimationQueryCancel"
    animationMsgRouter = CefMessageRouter.create(animationConfig)
    client.addMessageRouter(animationMsgRouter)
    val animationHandler: CefMessageRouterHandler =
        object : CefMessageRouterHandlerAdapter() {
          override fun onQuery(
              browser: CefBrowser,
              frame: CefFrame,
              query_id: Long,
              request: String,
              persistent: Boolean,
              callback: CefQueryCallback
          ): Boolean {
            val json = Base64.decode(request)
            val eventData = jsonMapper.decodeFromString<AnimationFinishedEventData>(json)
            if (eventData is AnimationFinishedEventData) {
              val menuSceneAnimations = Frontend.menuScene?.animations?.toList() ?: listOf()
              val boardGameSceneAnimations =
                  Frontend.boardGameScene?.animations?.toList() ?: listOf()
              val animations = menuSceneAnimations + boardGameSceneAnimations
              val animation = animations.find { it.id == eventData.id }
              animation?.onFinished?.invoke(AnimationFinishedEvent())
              return true
            }
            return false
          }
        }
    animationMsgRouter?.addHandler(animationHandler, true)
    // endregion

    val browser = client.createBrowser("$startURL:${Constants.PORT}", useOSR, isTransparent)
    activeBrowser = browser
    val browserUI = browser.uiComponent

    // region - Browser Client Handlers
    client.addFocusHandler(
        object : CefFocusHandlerAdapter() {
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
    client.addLoadHandler(
        object : CefLoadHandlerAdapter() {
          override fun onLoadEnd(browserArg: CefBrowser, frame: CefFrame, httpStatusCode: Int) {
            if (browserArg != browser) {
              val validBrowser = dialogMap.keys.find { it == browserArg }
              val dialogData = dialogMap[validBrowser]

              if (dialogData != null && validBrowser != null) {
                setDialogContent(validBrowser, dialogData)
                dialogMap.remove(validBrowser)
              }
            }

            Frontend.application.onWindowShown?.invoke()
          }
        })
    client.addContextMenuHandler(
        object : CefContextMenuHandlerAdapter() {
          override fun onBeforeContextMenu(
              browser: CefBrowser,
              frame: CefFrame,
              params: CefContextMenuParams,
              model: CefMenuModel
          ) {
            model.clear()
          }
        })

    client.addDisplayHandler(
        object : CefDisplayHandlerAdapter() {
          override fun onFullscreenModeChange(browser: CefBrowser?, fullscreen: Boolean) {
            if (fullscreen) {
              Frontend.isFullScreenProperty.setInternal(true)
            } else {
              Frontend.isFullScreenProperty.setInternal(false)
            }
          }
        })
    // endregion

    // region - Frame Settings
    contentPane.add(browserUI, BorderLayout.CENTER)
    pack()
    setSize(1280, 750)
    isVisible = true
    if (Frontend.isMaximizedProperty.value) {
      extendedState = MAXIMIZED_BOTH
    }

    if (Frontend.isFullScreenProperty.value) {
      toggleFullscreen(true)
    } else {
      toggleFullscreen(false)
    }

    setLocationRelativeTo(null)

    addWindowListener(
        object : WindowAdapter() {
          override fun windowClosing(e: WindowEvent) {
            Frontend.application.onWindowClosed?.invoke()
            println("[BGW] BGW Runtime shutting down...")
            try {
              CefApp.getInstance().dispose()
            } catch (_: Exception) {} finally {
              dispose()
            }
          }

          override fun windowOpened(e: WindowEvent?) {
            super.windowOpened(e)

            timer(period = 1000) {
              val pidsBefore = pids.size
              pids += filterJCEFHelperProcesses(getChildProcessIds())
              File("$tmpDir/application.pid").writeText(pids.joinToString(",").trim())
              if (pids.size == pidsBefore) {
                pidsUnchanged++
              } else {
                pidsUnchanged = 0
              }

              if (pidsUnchanged >= 5) {
                this.cancel()
              }
            }
          }
        })
    // endregion

    defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
    addWindowListener(
        object : WindowAdapter() {
          override fun windowClosing(e: WindowEvent) {
            CefApp.getInstance().dispose()
            dispose()
          }
        })

    addWindowStateListener { e ->
      if (e.newState == MAXIMIZED_BOTH) {
        Frontend.isMaximizedProperty.setInternal(true)
      } else {
        Frontend.isMaximizedProperty.setInternal(false)
      }
    }

    //    addComponentListener(
    //        object : ComponentAdapter() {
    //          override fun componentResized(e: ComponentEvent) {
    //            Frontend.widthProperty.setInternal(this@MainFrame.width.toDouble())
    //            Frontend.heightProperty.setInternal(this@MainFrame.height.toDouble())
    //          }
    //        })
  }

  internal fun gracefulShutdown() {
    Frontend.application.onWindowClosed?.invoke()
    println("[BGW] BGW Runtime shutting down...")
    try {
      CefApp.getInstance().dispose()
    } catch (_: Exception) {} finally {
      exitProcess(0)
    }
  }

  internal fun toggleFullscreen(boolean: Boolean) {
    if (isDisplayable) {
      if (boolean) {
        if (fullscreenFrame == null) {
          fullscreenFrame = JFrame()
          fullscreenFrame?.isUndecorated = true
          fullscreenFrame?.extendedState = JFrame.MAXIMIZED_BOTH
          fullscreenFrame?.contentPane = contentPane
          fullscreenFrame?.iconImage = iconImage
          fullscreenFrame?.title = title
          fullscreenFrame?.isVisible = true
          isVisible = false
        }
      } else {
        fullscreenFrame?.let { fsFrame ->
          contentPane = fsFrame.contentPane
          fsFrame.isVisible = false
          fullscreenFrame = null
          isVisible = true
        }
      }
    }
  }

  internal fun getChildProcessIds(process: ProcessHandle = ProcessHandle.current()): Set<Long> {
    val childrenList = mutableListOf<ProcessHandle>()
    val children =
        process.children().let {
          it.forEach { childrenList.add(it) }
          childrenList
        }
    return (children.flatMap { getChildProcessIds(it) } + children.map { it.pid() }).toSet()
  }

  internal fun getChildrenJCEFHelperProcesses(): Set<Long> {
    val currentProcess = ProcessHandle.current()
    return currentProcess
        .children()
        .filter {
          it.info().command().toString().contains(Config.BGW_VERSION) &&
              it.info().command().toString().contains("jcef_helper")
        }
        .map { it.pid() }
        .collect(Collectors.toSet())
  }

  internal fun filterJCEFHelperProcesses(pids: Set<Long>): Set<Long> {
    return pids.filter { it in getChildrenJCEFHelperProcesses() }.toSet()
  }

  internal fun setDialogContent(browser: CefBrowser, dialogData: DialogData) {
    browser.executeJavaScript(
        """
        document.write(`
            <html>
                <head>        
                    <style>
                        * {
                            padding: 0;
                            margin: 0;
                            box-sizing: border-box;
                            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
                        }
                        
                        *::-webkit-scrollbar {
                            display: none;
                        }
                        
                        body {
                            background-color: #161d29;
                            color: white;
                            padding: 1.5rem;
                            padding-inline: 2rem;
                            width: 100%;
                            height: 100vh;
                            display: flex;
                            flex-direction: column;
                        }
                        
                        .header {
                            display: flex;
                            align-items: baseline;
                            margin-bottom: 1rem;
                            padding-bottom: 0.5rem;
                            flex-direction: column;
                            gap: 0.25rem;
                        }
                        
                        h1 {
                            font-size: 1.25rem;
                            font-weight: 700;
                            color: white;
                        }
                        
                        .message {
                            font-size: 0.85rem;
                            color: #9ca3af;
                            flex: 1;
                        }
                        
                        .stack-container {
                            flex: 1;
                            border-radius: 0.75rem;
                            background: #121824;
                            overflow: hidden;
                            display: flex;
                            flex-direction: column;
                        }
                        
                        .stack-header {
                            background: #0f141f;
                            color: #e6e6e6;
                            padding: 0.75rem 1rem;
                            padding-inline: 1.5rem;
                            font-size: 0.9rem;
                            font-weight: 500;
                        }
                        
                        .code-scroll {
                            padding: 1rem;
                            padding-inline: 1.5rem;
                            overflow: auto;
                            height: 100%;
                        }
                        
                        .code-scroll::-webkit-scrollbar {
                            display: none;
                        }
                        
                        code {
                            color: #e6e6e6;
                            font-family: monospace;
                            font-size: 0.9rem;
                            line-height: 1.5;
                        }
                        
                        .footer {
                            margin-top: 1rem;
                            display: flex;
                            justify-content: flex-end;
                        }
                        
                        button {
                            background-color: #121824;
                            color: white;
                            border: none;
                            border-radius: 0.25rem;
                            padding: 0.5rem 1.25rem;
                            font-size: 0.9rem;
                            font-weight: 500;
                            cursor: pointer;
                            transition: background-color 0.2s;
                        }
                        
                        button:hover {
                            background-color: #0f141f;
                        }
                        
                        /* Syntax highlighting classes */
                        .pkg { color: #6dbeff; font-family: monospace; }
                        .cls { color: #ffc656; font-family: monospace; }
                        .method { color: #ffc656; font-family: monospace; }
                        .line { color: #9ca3af; font-family: monospace; }
                        .elem { color: #ea7afc; font-family: monospace; }
                        .msg { color: #9ca3af; font-style: italic; font-family: monospace; }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h1>${extractExceptionType(dialogData.exception)}</h1>
                        <div class="message">${dialogData.message}</div>
                    </div>
                    
                    <div class="stack-container">
                        <div class="stack-header">Stacktrace</div>
                        <div class="code-scroll">
                            <code>${syntaxHighlightStackTrace(dialogData.exception)}</code>
                        </div>
                    </div>
                    
                    <div class="footer">
                        <button onClick="window.close()">Dismiss</button>
                    </div>
                </body>
            </html>`);
        """.trimIndent(),
        browser.url,
        0)
  }

  private fun extractExceptionType(trace: String?): String {
    if (trace == null) return ""

    val exceptionType = trace.substringBefore(":")
    return exceptionType.replace(":", "").split(".").last().trim()
  }

  private fun syntaxHighlightStackTrace(trace: String?): String {
    if (trace == null) return ""

    return trace
        .replace(Regex("\\\$(\\d+|\\w+)"), "")
        // Match package.Class@hexIdentifier patterns
        .replace(Regex("(\\w+(?:\\.\\w+)+)@([0-9a-f]+)"), "<span class=\"elem\">$1@$2</span>")
        // Match standard stack trace lines with file and line numbers
        .replace(
            Regex("at ((?:[\\w.]+\\.)+)(.+)\\((.*?):(\\d+)\\)"),
            "<span class=\"msg\">at</span> <span class=\"pkg\">$1</span><span class=\"method\">$2</span><span class=\"line\">($3:$4)</span>")
        // Match exception class names with message
        .replace(
            Regex("^([^:\\n]+Exception|[^:\\n]+Error):(.*)", RegexOption.MULTILINE),
            "<span class=\"cls\">$1</span>:<span class=\"msg\">$2</span>")
        // Highlight "Caused by:" sections
        .replace("Caused by: ", "<span class=\"msg\">Caused by: </span>")
        // Replace tabs with proper spacing
        .replace("\t", "<span style=\"display:inline-block;width:20px;\"></span>")
        // Replace newlines with HTML breaks
        .replace("\n", "<br>")
        .trimIndent()
        .trim()
  }

  internal fun openNewDialog(dialogData: DialogData) {
    val dialogFrame = client.createBrowser("about:blank", false, false)
    val dialogUI = dialogFrame.uiComponent

    dialogMap[dialogFrame] = dialogData

    val dialog = JFrame()
    dialog.title = dialogData.message
    dialog.iconImage = iconImage
    dialog.background = java.awt.Color(0x161d29)
    dialog.contentPane.add(dialogUI, BorderLayout.CENTER)
    dialog.pack()
    dialog.setSize(1200, 500)
    dialog.setLocationRelativeTo(this)
    dialog.isVisible = true
  }

  internal fun openNewFileDialog(fileDialog: FileDialog) {
    val type =
        when (fileDialog.mode) {
          FileDialogMode.OPEN_FILE -> CefDialogHandler.FileDialogMode.FILE_DIALOG_OPEN
          FileDialogMode.OPEN_MULTIPLE_FILES ->
              CefDialogHandler.FileDialogMode.FILE_DIALOG_OPEN_MULTIPLE
          FileDialogMode.SAVE_FILE -> CefDialogHandler.FileDialogMode.FILE_DIALOG_SAVE
          // FileDialogMode.CHOOSE_DIRECTORY ->
          // CefDialogHandler.FileDialogMode.FILE_DIALOG_OPEN_FOLDER
          FileDialogMode.CHOOSE_DIRECTORY -> TODO("Not yet implemented")
        }

    val defaultFile = fileDialog.initialFileName
    val extensions = fileDialog.extensionFilters.mapNotNull { it.getExtensionsString() }
    println(extensions)

    activeBrowser.runFileDialog(
        type,
        fileDialog.title,
        defaultFile,
        Vector(extensions),
        0,
    ) { selectedFiles ->
      if (selectedFiles.isNotEmpty()) {
        fileDialog.onPathsSelected?.invoke(selectedFiles)
      } else {
        fileDialog.onSelectionCancelled?.invoke()
      }
    }
  }
}
