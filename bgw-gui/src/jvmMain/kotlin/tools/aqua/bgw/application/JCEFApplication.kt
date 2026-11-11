/*
 * Copyright 2025-2026 The BoardGameWork Authors
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
import jsonMapper
import kotlin.concurrent.timer
import kotlin.system.exitProcess
import me.friwi.jcefmaven.CefAppBuilder
import me.friwi.jcefmaven.CefBuildInfo
import me.friwi.jcefmaven.EnumPlatform
import me.friwi.jcefmaven.EnumProgress
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter
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
import tools.aqua.bgw.core.*
import tools.aqua.bgw.dialog.*
import tools.aqua.bgw.dialog.FileDialog
import tools.aqua.bgw.util.LogType
import tools.aqua.bgw.util.Logger
import tools.aqua.bgw.visual.ImageVisual

private const val MIN_WIDTH = 200
private const val MIN_HEIGHT = 150

internal object Constants {
  val PORT = ServerSocket(0).use { it.localPort }
  const val DEBUG = false
}

internal object StdErrFilter {
  private val originalErr = System.err
  private val originalOut = System.out
  private val filters = mutableListOf<String>()

  fun install(vararg suppressedSubstrings: String) {
    filters.clear()
    filters.addAll(suppressedSubstrings)

    val filteringErrStream =
        object : java.io.PrintStream(originalErr) {
          override fun println(x: String?) {
            if (x != null && filters.any { x.contains(it) }) {
              return
            }
            super.println(x)
          }

          override fun print(s: String?) {
            if (s != null && filters.any { s.contains(it) }) {
              return
            }
            super.print(s)
          }
        }

    val filteringOutStream =
        object : java.io.PrintStream(originalOut) {
          override fun println(x: String?) {
            if (x != null && filters.any { x.contains(it) }) {
              return
            }
            super.println(x)
          }

          override fun print(s: String?) {
            if (s != null && filters.any { s.contains(it) }) {
              return
            }
            super.print(s)
          }
        }

    System.setErr(filteringErrStream)
    System.setOut(filteringOutStream)
  }

  fun uninstall() {
    System.setErr(originalErr)
    System.setOut(originalOut)
  }
}

internal class JCEFApplication : Application {
  var frame: MainFrame? = null

  private val handlersMap = mutableMapOf<ID, CefMessageRouterHandler>()

  init {
    StdErrFilter.install("SLF4J", "initialize on Thread", "google_apis")
  }

  internal companion object {
    const val WAYLAND_SESSION_TYPE = "wayland"

    /** System property (or `BGW_RENDER_MODE` environment variable) to force a render mode. */
    const val RENDER_MODE_PROPERTY = "bgw.renderMode"

    private val isLinux: Boolean
      get() =
          when (EnumPlatform.getCurrentPlatform()) {
            EnumPlatform.LINUX_AMD64,
            EnumPlatform.LINUX_ARM64,
            EnumPlatform.LINUX_ARM -> true
            else -> false
          }

    /**
     * Checks if the application is running on Linux with Wayland (through XWayland).
     *
     * Besides `XDG_SESSION_TYPE`, the Wayland display and compositor variables are checked.
     */
    internal fun checkIfLinuxWayland(): Boolean {
      if (!isLinux) return false
      return System.getenv("XDG_SESSION_TYPE").equals(WAYLAND_SESSION_TYPE, ignoreCase = true) ||
          !System.getenv("WAYLAND_DISPLAY").isNullOrBlank() ||
          !System.getenv("HYPRLAND_INSTANCE_SIGNATURE").isNullOrBlank() ||
          !System.getenv("SWAYSOCK").isNullOrBlank()
    }

    /**
     * Whether the browser is rendered off-screen (into a Swing component) instead of into a native
     * child window. This is the case under Wayland, unless forced otherwise with
     * `-Dbgw.renderMode=osr` or `-Dbgw.renderMode=windowed` (or the `BGW_RENDER_MODE` environment
     * variable).
     */
    internal fun useOffscreenRendering(): Boolean =
        when ((System.getProperty(RENDER_MODE_PROPERTY) ?: System.getenv("BGW_RENDER_MODE"))
            ?.lowercase()) {
          "osr",
          "offscreen" -> true
          "windowed",
          "native" -> false
          else -> checkIfLinuxWayland()
        }

    private val nonReparentingDesktops =
        listOf(
            "hyprland", "sway", "i3", "bspwm", "dwm", "qtile", "niri", "river", "awesome", "xmonad")

    /**
     * Warns if the application runs under a non-reparenting window manager (e.g. Hyprland or sway)
     * and `_JAVA_AWT_WM_NONREPARENTING` is not set.
     */
    internal fun warnAboutNonReparentingWindowManager() {
      if (!isLinux || !System.getenv("_JAVA_AWT_WM_NONREPARENTING").isNullOrBlank()) return
      val desktop =
          listOfNotNull(System.getenv("XDG_CURRENT_DESKTOP"), System.getenv("DESKTOP_SESSION"))
              .joinToString(":")
              .lowercase()
      val tiling =
          nonReparentingDesktops.any { desktop.contains(it) } ||
              !System.getenv("HYPRLAND_INSTANCE_SIGNATURE").isNullOrBlank() ||
              !System.getenv("SWAYSOCK").isNullOrBlank()
      if (tiling) {
        Logger.warning(
            "Detected a non-reparenting window manager (${desktop.ifBlank { "tiling" }}). If the " +
                "window content is offset or blank, set the environment variable " +
                "_JAVA_AWT_WM_NONREPARENTING=1 before starting the application.")
      }
    }

    /** Runs [block] on the AWT event dispatch thread. */
    internal fun onEdt(block: () -> Unit) {
      if (EventQueue.isDispatchThread()) block() else EventQueue.invokeLater(block)
    }
  }

  override fun start(onClose: () -> Unit, callback: (Any) -> Unit) {
    if (Constants.DEBUG) Logger.info("Starting BGW Runtime (http://localhost:${Constants.PORT})")
    else Logger.info("Starting BGW Runtime (${Constants.PORT})")

    val useOSR = useOffscreenRendering()
    warnAboutNonReparentingWindowManager()

    val cef = CefRuntime.create(useOSR = useOSR, debugLogging = Constants.DEBUG)

    EventQueue.invokeLater {
      val mainFrame =
          MainFrame(
              cef = cef, loadCallback = callback, debugLogging = Constants.DEBUG, useOSR = useOSR)
      frame = mainFrame

      mainFrame.iconImage =
          loadIcon(Frontend.iconProperty.value)
              ?: JCEFApplication::class.java.getResource("/icon.png")?.let { ImageIO.read(it) }
      mainFrame.title = Frontend.titleProperty.value.ifEmpty { DEFAULT_WINDOW_TITLE }
      mainFrame.isVisible = true
      mainFrame.repaint()
    }

    // Window properties changed by the application are applied on the event dispatch thread.
    Frontend.titleProperty.internalListener = { _, title ->
      onEdt { frame?.title = title.ifEmpty { DEFAULT_WINDOW_TITLE } }
    }
    Frontend.iconProperty.internalListener = { _, icon ->
      onEdt { loadIcon(icon)?.let { frame?.iconImage = it } }
    }

    // Call the onClose callback when the application is closed
    Runtime.getRuntime().addShutdownHook(Thread { onClose() })
  }

  /** Loads the image of an [ImageVisual] used as window icon, `null` if it cannot be loaded. */
  private fun loadIcon(icon: ImageVisual?): Image? {
    val path = icon?.path ?: return null
    return try {
      when {
        path.startsWith("http://") || path.startsWith("https://") ->
            ImageIO.read(java.net.URI(path).toURL())
        path.startsWith("data:image") ->
            ImageIO.read(Base64.getDecoder().decode(path.substringAfter(",")).inputStream())
        else ->
            JCEFApplication::class.java.getResource("/${path.removePrefix("/")}")?.let {
              ImageIO.read(it)
            } ?: File(path).takeIf { it.exists() }?.let { ImageIO.read(it) }
      }
    } catch (e: Exception) {
      Logger.warning("Could not load window icon $path: ${e.message}")
      null
    }
  }

  override fun stop() {
    onEdt { frame?.gracefulShutdown() }
  }

  override fun toggleFullscreen(boolean: Boolean) {
    onEdt { frame?.toggleFullscreen(boolean) }
  }

  override fun toggleMaximized(boolean: Boolean) {
    onEdt { frame?.extendedState = if (boolean) JFrame.MAXIMIZED_BOTH else JFrame.NORMAL }
  }

  override fun resize(width: Int, height: Int) {
    onEdt { frame?.setSize(width, height) }
  }

  override fun clearAllEventListeners() {
    handlersMap.forEach { (_, handler) -> frame?.msgRouter?.removeHandler(handler) }
    handlersMap.clear()
  }

  fun getTitle(): String {
    return frame?.title ?: ""
  }

  fun setTitle(title: String) {
    onEdt { frame?.title = title }
  }

  override fun openNewDialog(dialogData: DialogData) {
    frame?.openNewDialog(dialogData)
  }
}

internal class BGWAppHandler() : MavenCefAppHandlerAdapter() {
  override fun stateHasChanged(state: CefApp.CefAppState?) {
    if (state == CefApp.CefAppState.TERMINATED) {
      exitProcess(0)
    }
  }

  companion object {
    /**
     * Shuts CEF down and exits once it terminated (see [stateHasChanged]). If CEF does not
     * terminate in time, the process exits anyway.
     */
    fun shutdown(window: JFrame) {
      Thread {
            Thread.sleep(SHUTDOWN_TIMEOUT_MILLIS)
            exitProcess(0)
          }
          .apply {
            isDaemon = true
            name = "BGW Shutdown Watchdog"
          }
          .start()
      CefApp.getInstance().dispose()
      window.dispose()
    }

    private const val SHUTDOWN_TIMEOUT_MILLIS = 5000L
  }
}

/** The CEF runtime and the temporary directory it uses. Created before AWT is initialized. */
internal class CefRuntime(val app: CefApp, val tmpDir: String) {
  companion object {
    fun create(useOSR: Boolean, debugLogging: Boolean): CefRuntime {
      val builder = CefAppBuilder()
      builder.cefSettings.windowless_rendering_enabled = useOSR

      if (!debugLogging) {
        builder.cefSettings.log_severity = CefSettings.LogSeverity.LOGSEVERITY_DISABLE
      } else {
        // TODO: Switch back to INFO
        builder.cefSettings.log_severity = CefSettings.LogSeverity.LOGSEVERITY_DISABLE
        builder.cefSettings.remote_debugging_port = 2504
      }

      val bgwAppName = "bgw-runtime_${Config.BGW_VERSION}"
      val defaultDirs = ProjectDirectories.from("bgw-gui", "tools.aqua", bgwAppName)
      val installDir = Paths.get(defaultDirs.cacheDir).toFile()
      builder.setInstallDir(installDir)

      val tmpDir: String =
          System.getProperty("tmpDir")?.takeIf { it.isNotEmpty() && File(it).exists() }
              ?: Files.createTempDirectory("bgw-").toString().also { File(it).deleteOnExit() }

      builder.cefSettings.root_cache_path = tmpDir
      builder.cefSettings.cache_path = tmpDir
      builder.cefSettings.log_file = "$tmpDir/bgw.log"
      builder.addJcefArgs("--disable-pinch", "--use-client-dialogs")

      builder.setAppHandler(BGWAppHandler())

      builder.setProgressHandler { enumProgress, fl ->
        if (enumProgress == EnumProgress.DOWNLOADING || enumProgress == EnumProgress.EXTRACTING) {
          if (fl >= 0) Logger.logSingle("Downloading BGW Runtime... $fl%\r", LogType.INFO)
        } else if (enumProgress == EnumProgress.LOCATING || enumProgress == EnumProgress.INSTALL)
            Logger.debug("Initializing BGW Runtime...")
        else if (enumProgress == EnumProgress.INITIALIZING) Logger.log("Loading BGW Runtime...")
      }

      val cefApp = builder.build()
      cefApp.setSettings(builder.cefSettings)

      Logger.debug("Platform: ${EnumPlatform.getCurrentPlatform()}")
      val buildInfo = CefBuildInfo.fromClasspath()
      Logger.debug("Build: ${buildInfo.jcefUrl} ${buildInfo.releaseUrl}")
      Logger.debug("Runtime Version: ${cefApp.version.toString().replace(Regex("\n"), " ")}")
      return CefRuntime(cefApp, tmpDir)
    }
  }
}

internal class MainFrame(
    cef: CefRuntime,
    startURL: String = "http://localhost",
    private val useOSR: Boolean = false,
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
    val cefApp = cef.app
    val tmpDir = cef.tmpDir

    val pids = mutableSetOf<Long>()
    var pidsUnchanged = 0

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
              Logger.error(e.stackTraceToString())
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
            if (debugLogging) {
              browserArg.openDevTools()
            }
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
    size = initialWindowSize()
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
            Logger.info("[BGW] Closing BGW Runtime...")
            BGWAppHandler.shutdown(this@MainFrame)
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

    // Closing is handled by the window listener above, which shuts CEF down before exiting.
    defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE

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
    Logger.info("[BGW] Exiting BGW Runtime...")
    BGWAppHandler.shutdown(this)
  }

  /**
   * The window size requested by the application, limited to the usable screen area. Applications
   * that keep the default size get a window of 1280 x 750.
   */
  private fun initialWindowSize(): Dimension {
    val requestedWidth = Frontend.widthProperty.value.toInt()
    val requestedHeight = Frontend.heightProperty.value.toInt()
    if (requestedWidth == DEFAULT_WINDOW_WIDTH.toInt() &&
        requestedHeight == DEFAULT_WINDOW_HEIGHT.toInt()) {
      return Dimension(1280, 750)
    }
    val screen =
        try {
          GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
        } catch (_: Exception) {
          Rectangle(0, 0, requestedWidth, requestedHeight)
        }
    return Dimension(
        requestedWidth.coerceIn(MIN_WIDTH, maxOf(MIN_WIDTH, screen.width)),
        requestedHeight.coerceIn(MIN_HEIGHT, maxOf(MIN_HEIGHT, screen.height)))
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
    if (!EventQueue.isDispatchThread()) {
      EventQueue.invokeLater { openNewDialog(dialogData) }
      return
    }
    val dialogFrame = client.createBrowser("about:blank", useOSR, false)
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
    if (!EventQueue.isDispatchThread()) {
      EventQueue.invokeLater { openNewFileDialog(fileDialog) }
      return
    }
    val type =
        when (fileDialog.mode) {
          FileDialogMode.OPEN_FILE -> CefDialogHandler.FileDialogMode.FILE_DIALOG_OPEN
          FileDialogMode.OPEN_MULTIPLE_FILES ->
              CefDialogHandler.FileDialogMode.FILE_DIALOG_OPEN_MULTIPLE

          FileDialogMode.SAVE_FILE -> CefDialogHandler.FileDialogMode.FILE_DIALOG_SAVE
          FileDialogMode.CHOOSE_DIRECTORY -> CefDialogHandler.FileDialogMode.FILE_DIALOG_OPEN_FOLDER
        }

    val defaultFile = fileDialog.initialFileName
    val extensions = fileDialog.extensionFilters.mapNotNull { it.getExtensionsString() }
    Logger.debug(extensions)

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
