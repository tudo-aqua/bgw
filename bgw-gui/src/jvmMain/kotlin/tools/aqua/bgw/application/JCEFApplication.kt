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

import DialogButtonClickData
import DialogData
import ID
import data.event.*
import dev.dirs.ProjectDirectories
import java.awt.*
import java.awt.event.ComponentEvent
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
import org.cef.misc.BoolRef
import org.cef.network.CefRequest
import tools.aqua.bgw.builder.DialogBuilder
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.*
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.dialog.*
import tools.aqua.bgw.dialog.FileDialog
import tools.aqua.bgw.event.*

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

  override fun openNewDialog(dialogData: DialogData) {
    frame?.openNewDialog(dialogData)
  }
}

internal class MainFrame(
    startURL: String = "http://localhost",
    useOSR: Boolean = false,
    isTransparent: Boolean = false,
    val loadCallback: (Any) -> Unit,
    debugLogging: Boolean = false
) : JFrame() {
  private var browserFocus = true
  private var fullscreenFrame: JFrame? = null

  var msgRouter: CefMessageRouter? = null
  var animationMsgRouter: CefMessageRouter? = null
  var globalEventMsgRouter: CefMessageRouter? = null
  var dialogMsgRouter: CefMessageRouter? = null
  var client: CefClient
  var activeBrowser: CefBrowser

  var dialogMap: MutableMap<CefBrowser, DialogData> = mutableMapOf()

  init {
    // region - CEF Initialization / Settings
    val builder = CefAppBuilder()
    builder.cefSettings.windowless_rendering_enabled = useOSR

    if (!debugLogging) {
      builder.cefSettings.log_severity = CefSettings.LogSeverity.LOGSEVERITY_DISABLE
    } else {
      builder.cefSettings.log_severity = CefSettings.LogSeverity.LOGSEVERITY_INFO
      builder.cefSettings.remote_debugging_port = 2504
    }

    val BGWAppName = "bgw-runtime_${Config.BGW_VERSION}"
    val defaultDirs = ProjectDirectories.from("bgw-gui", "tools.aqua", BGWAppName)
    val installDir = Paths.get(defaultDirs.cacheDir).toFile()
    builder.setInstallDir(installDir)

    val tmpDir: String =
        System.getProperty("tmpDir")?.takeIf { it.isNotEmpty() && File(it).exists() }
            ?: Files.createTempDirectory("bgw-").toString().also { File(it).deleteOnExit() }

    builder.cefSettings.root_cache_path = tmpDir
    builder.cefSettings.cache_path = tmpDir
    builder.cefSettings.log_file = "$tmpDir/bgw.log"
    builder.jcefArgs.add("--disable-pinch")

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

    // Dialog Button Handler
    val dialogConfig = CefMessageRouterConfig()
    dialogConfig.jsQueryFunction = "cefDialogQuery"
    dialogConfig.jsCancelFunction = "cefDialogQueryCancel"
    dialogMsgRouter = CefMessageRouter.create(dialogConfig)
    client.addMessageRouter(dialogMsgRouter)
    val dialogButtonHandler =
        object : CefMessageRouterHandlerAdapter() {
          override fun onQuery(
              browser: CefBrowser,
              frame: CefFrame,
              query_id: Long,
              request: String,
              persistent: Boolean,
              callback: CefQueryCallback
          ): Boolean {
            try {
              val data = jsonMapper.decodeFromString<DialogButtonClickData>(request)

              // Find the dialog by ID and invoke the button callback
              val dialog = Frontend.openedDialogs[data.dialogId]
              val buttonType = dialog?.buttonTypes?.get(data.buttonIndex)
              if (dialog != null && buttonType != null) {
                Frontend.openedDialogs.remove(data.dialogId)
                dialog.onButtonClicked?.invoke(buttonType)
              }
              Frontend.openedDialogs.remove(data.dialogId)

              callback.success("")
              return true
            } catch (e: Exception) {
              e.printStackTrace()
            }
            return false
          }
        }

    dialogMsgRouter?.addHandler(dialogButtonHandler, true)
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

    // Prevent dragging files into the browser
    client.addDragHandler { browser, dragData, mask -> true }

    // Prevent navigation to external URLs
    client.addRequestHandler(
        object : CefRequestHandlerAdapter() {
          override fun onBeforeBrowse(
              browser: CefBrowser,
              frame: CefFrame,
              request: CefRequest,
              user_gesture: Boolean,
              is_redirect: Boolean
          ): Boolean {
            val originalURL = "$startURL:${Constants.PORT}"
            return !request.url.startsWith(originalURL) && frame.isMain
          }
        })

    // Disable keyboard shortcuts
    client.addKeyboardHandler(
        object : CefKeyboardHandlerAdapter() {
          override fun onPreKeyEvent(
              browser: CefBrowser?,
              event: CefKeyboardHandler.CefKeyEvent?,
              is_keyboard_shortcut: BoolRef?
          ): Boolean {
            return event != null &&
                event.type == CefKeyboardHandler.CefKeyEvent.EventType.KEYEVENT_KEYDOWN
          }
        })

    client.addLoadHandler(
        object : CefLoadHandlerAdapter() {
          override fun onLoadEnd(browserArg: CefBrowser, frame: CefFrame, httpStatusCode: Int) {
            if (browserArg != browser) {
              val validBrowser = dialogMap.keys.find { it == browserArg }
              val dialogData = dialogMap[validBrowser]

              if (dialogData != null && validBrowser != null) {
                if (dialogData.dialogType == DialogType.EXCEPTION)
                    DialogBuilder.setExceptionDialogContent(validBrowser, dialogData)
                else DialogBuilder.setDialogContent(validBrowser, dialogData)
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

    // Add component listener with debounce to detect window size changes
    var resizeTimer: Timer? = null
    val resizeDelay = 100L // milliseconds to wait after last resize event

    addComponentListener(
        object : java.awt.event.ComponentAdapter() {
          override fun componentResized(e: ComponentEvent) {
            // Cancel existing timer if still running
            resizeTimer?.cancel()

            // Create new timer that will trigger rescale events after delay
            resizeTimer =
                Timer().apply {
                  schedule(
                      object : TimerTask() {
                        override fun run() {
                          // Call onSceneRescaled for both scenes when window size changes
                          Frontend.menuScene?.onSceneRescaled?.invoke()
                          Frontend.boardGameScene?.onSceneRescaled?.invoke()
                        }
                      },
                      resizeDelay)
                }
          }
        })
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

  internal fun openNewDialog(dialogData: DialogData) {
    val dialogFrame = client.createBrowser("about:blank", false, false)
    val dialogUI = dialogFrame.uiComponent

    dialogMap[dialogFrame] = dialogData

    val dialog = JFrame()
    dialog.iconImage = iconImage
    dialog.background = java.awt.Color(0x161d29)
    dialog.contentPane.add(dialogUI, BorderLayout.CENTER)
    if (dialogData.dialogType == DialogType.EXCEPTION) {
      dialog.minimumSize = Dimension(600, 500)
      dialog.setSize(1200, 500)
      dialog.title = dialogData.message
    } else {
      dialog.minimumSize = Dimension(600, 350)
      dialog.setSize(600, 350)
      dialog.title = dialogData.title
    }
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
          FileDialogMode.CHOOSE_DIRECTORY -> TODO("This feature is currently not supported.")
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
