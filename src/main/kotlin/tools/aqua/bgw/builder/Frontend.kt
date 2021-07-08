package tools.aqua.bgw.builder

import javafx.animation.*
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.SimpleDoubleProperty
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.effect.GaussianBlur
import javafx.scene.layout.Pane
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import tools.aqua.bgw.builder.SceneBuilder.Companion.buildGame
import tools.aqua.bgw.builder.SceneBuilder.Companion.buildMenu
import tools.aqua.bgw.core.*
import tools.aqua.bgw.dialog.ButtonType
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.FileDialog
import tools.aqua.bgw.dialog.FileDialog.FileDialogMode.*
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.StringProperty
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual
import java.awt.Color
import java.awt.Toolkit
import java.io.File
import java.util.*
import kotlin.math.min

/**
 * Frontend JavaFX wrapper.
 */
internal class Frontend : Application() {
	
	/**
	 * Starts the application.
	 */
	override fun start(primaryStage: Stage) {
		Thread.setDefaultUncaughtExceptionHandler { _, e ->
			e.printStackTrace()
			showDialog(Dialog("Exception", "An uncaught exception occurred.", e.message ?: "", e))
		}
		
		startApplication(primaryStage)
		
		application?.onWindowShown?.invoke()
	}
	
	/**
	 * Called when the application closes.
	 */
	override fun stop() {
		application?.onWindowClosed?.invoke()
	}
	
	/**
	 * Starts the application.
	 */
	internal fun start() {
		launch()
	}
	
	companion object {
		private const val BLUR_RADIUS = 63.0
		private const val MINIMIZED_FACTOR = 0.8
		
		private var boardGameScene: BoardGameScene? = null
		private var menuScene: MenuScene? = null
		private var scenePane: Pane = Pane()
		private var gamePane: Pane? = null
		private var menuPane: Pane? = null
		private var backgroundPane = Pane().apply { style = "-fx-background-color: black" }
		private var primaryStage: Stage? = null
		private var verticalSceneAlignment = VerticalAlignment.CENTER
		private var horizontalSceneAlignment = HorizontalAlignment.CENTER
		private var scaleMode = ScaleMode.FULL
		private var fullscreen = false
		
		internal var sceneScale: Double = 1.0
		internal var sceneX: Double = 0.0
		internal var sceneY: Double = 0.0
		
		internal val application: BoardGameApplication? = null
		internal val titleProperty: StringProperty = StringProperty()
		internal val backgroundProperty: ObjectProperty<Visual> = ObjectProperty(ColorVisual(Color.BLACK))
		
		internal fun showMenuScene(scene: MenuScene, fadeTime: Double) {
			menuScene = scene
			
			scene.zoomDetailProperty.setGUIListenerAndInvoke(scene.zoomDetail) { _, _ ->
				if (primaryStage != null) {
					menuPane = buildMenu(scene)
					boardGameScene?.lock()
					
					updateScene()
					fadeMenu(true, fadeTime)
				}
			}
		}
		
		internal fun hideMenuScene(fadeTime: Double) {
			fadeMenu(false, fadeTime)
		}
		
		internal fun showGameScene(scene: BoardGameScene) {
			boardGameScene = scene
			
			scene.zoomDetailProperty.setGUIListenerAndInvoke(scene.zoomDetail) { _, _ ->
				if (primaryStage != null) {
					gamePane = buildGame(scene)
					
					updateScene()
				}
			}
		}
		
		internal fun setHorizontalSceneAlignment(newHorizontalAlignment: HorizontalAlignment) {
			horizontalSceneAlignment = newHorizontalAlignment
			sizeChanged()
		}
		
		internal fun setVerticalSceneAlignment(newVerticalAlignment: VerticalAlignment) {
			verticalSceneAlignment = newVerticalAlignment
			sizeChanged()
		}
		
		internal fun setScaleMode(newScaleMode: ScaleMode) {
			scaleMode = newScaleMode
			sizeChanged()
		}

//		internal fun setFullScreen(fullscreen: Boolean) {
//			this.fullscreen = fullscreen
//			this.primaryStage?.isFullScreen = fullscreen
//		}
		
		internal fun updateScene() {
			val activePanes: MutableList<Pane> = ArrayList(2)
			
			gamePane?.apply {
				widthProperty().addListener { _, _, _ -> sizeChanged() }
				heightProperty().addListener { _, _, _ -> sizeChanged() }
				
				activePanes.add(this)
			}
			
			menuPane?.apply {
				widthProperty().addListener { _, _, _ -> sizeChanged() }
				heightProperty().addListener { _, _, _ -> sizeChanged() }
				
				activePanes.add(this)
			}
			
			if (activePanes.size == 2) {
				gamePane?.effect = GaussianBlur(BLUR_RADIUS)
				boardGameScene?.lock()
			}
			
			scenePane = Pane().apply {
				children.clear()
				children.add(backgroundPane)
				children.addAll(activePanes)
			}
			
			primaryStage?.scene = Scene(scenePane)
			
			sizeChanged()
		}
		
		internal fun mapScene(scene: tools.aqua.bgw.core.Scene<*>): Pane? =
			when (scene) {
				boardGameScene -> gamePane
				menuScene -> menuPane
				else -> null
			}
		
		private fun fadeMenu(fadeIn: Boolean, fadeTime: Double) {
			menuPane?.apply {
				FadeTransition(Duration.millis(fadeTime / 2), menuPane).apply {
					fromValue = if (fadeIn) 0.0 else 1.0
					toValue = if (fadeIn) 1.0 else 0.0
					interpolator = Interpolator.EASE_OUT
					onFinished = EventHandler {
						if (!fadeIn) {
							menuPane = null
							if (boardGameScene != null) boardGameScene!!.unlock()
							updateScene()
						}
					}
				}.play()
			}
			
			gamePane?.apply {
				val blur = GaussianBlur(0.0).also { effect = it }
				
				val value = SimpleDoubleProperty(0.0).apply {
					addListener { _, _, newValue -> blur.radius = newValue.toDouble() }
				}
				
				Timeline(
					KeyFrame(Duration.ZERO, KeyValue(value, if (fadeIn) 0 else BLUR_RADIUS)),
					KeyFrame(Duration.millis(fadeTime), KeyValue(value, if (fadeIn) BLUR_RADIUS else 0))
				).play()
			}
		}
		
		private fun sizeChanged() {
			val activePanes: List<Pane> = listOfNotNull(gamePane, menuPane).ifEmpty { return }
			
			//Wait for renderer to finish resize nodes
			Platform.runLater {
				val sceneHeight = scenePane.height
				val sceneWidth = scenePane.width
				
				if (sceneHeight == 0.0 || sceneWidth == 0.0)
					return@runLater
				
				activePanes.forEach {
					it.apply {
						val contentHeight = height
						val contentWidth = width
						
						if (contentHeight == 0.0 || contentWidth == 0.0)
							return@apply
						
						//Set new content layout
						layoutX = (sceneWidth - contentWidth) * horizontalSceneAlignment.positionMultiplier
						layoutY = (sceneHeight - contentHeight) * verticalSceneAlignment.positionMultiplier
						sceneX = layoutX
						sceneY = layoutY
						
						//Set new content scale
						if (scaleMode != ScaleMode.NO_SCALE) {
							sceneScale = min(sceneWidth / contentWidth, sceneHeight / contentHeight)
							
							if (scaleMode == ScaleMode.ONLY_SHRINK)
								sceneScale = min(sceneScale, 1.0)
							
							scaleX = sceneScale
							scaleY = sceneScale
							
							translateX = contentWidth / 2 * horizontalSceneAlignment.pivotMultiplier * (1 - sceneScale)
							translateY = contentHeight / 2 * verticalSceneAlignment.pivotMultiplier * (1 - sceneScale)
							
							sceneX = (sceneWidth - contentWidth * sceneScale) / 2 *
									(1 + horizontalSceneAlignment.pivotMultiplier)
							sceneY = (sceneHeight - contentHeight * sceneScale) / 2 *
									(1 + verticalSceneAlignment.pivotMultiplier)
						}
						
						//Zoom detail
						val scene = this.toScene()!!
						val scale = min(
							(scene.width - scene.zoomDetail.width) / scene.width,
							(scene.height - scene.zoomDetail.height) / scene.height
						)
						scaleX += scale
						scaleY += scale
						translateX -= scene.zoomDetail.topLeft.xCoord
						translateY -= scene.zoomDetail.topLeft.yCoord
						
					}
				}
			}
		}
		
		private fun Pane.toScene() =
			if (this == gamePane) boardGameScene else menuScene
		
		internal fun startApplication(stage: Stage) {
			primaryStage = stage.apply {
				//Initialize default DECORATED stage style allowing minimizing
				initStyle(StageStyle.DECORATED)
				
				scene = Scene(Pane())
				
				//Get screen size and set ratio in relation to screen resolution
				val screenSize = Toolkit.getDefaultToolkit().screenSize
				val bgwScene: tools.aqua.bgw.core.Scene<out ElementView>? = boardGameScene ?: menuScene
				
				//Set minimized window size as 80% of screen resolution and set default window mode to maximized
				if (bgwScene == null) {
					width = screenSize.getWidth() * MINIMIZED_FACTOR
					height = screenSize.getHeight() * MINIMIZED_FACTOR
				} else {
					//Get BoardGameScene ratio
					val sceneWidth: Double = bgwScene.width
					val sceneHeight: Double = bgwScene.height
					
					val relativeSceneWidth: Double = sceneWidth / screenSize.getWidth()
					val relativeSceneHeight: Double = sceneHeight / screenSize.getHeight()
					
					if (relativeSceneWidth > relativeSceneHeight) {
						width = screenSize.getWidth() * MINIMIZED_FACTOR
						height = width / sceneWidth * sceneHeight
					} else {
						height = screenSize.getHeight() * MINIMIZED_FACTOR
						width = height / sceneHeight * sceneWidth
					}
				}
				
				isMaximized = false
				isFullScreen = fullscreen //TODO: Fullscreen not working
				
				heightProperty().addListener { _, _, _ -> sizeChanged() }
				widthProperty().addListener { _, _, _ -> sizeChanged() }
				
				show()
				
				//Adjust for title bar height
				height += (stage.height - scene.height)
			}
			
			menuScene?.let { menuPane = buildMenu(it) }
			boardGameScene?.let { gamePane = buildGame(it) }
			
			titleProperty.setGUIListenerAndInvoke(titleProperty.value) { _, nV ->
				primaryStage?.title = nV
			}
			
			backgroundProperty.setGUIListenerAndInvoke(backgroundProperty.value) { _, nV ->
				backgroundPane.children.clear()
				backgroundPane.children.add(VisualBuilder.buildVisual(nV).apply {
					prefWidthProperty().bind(primaryStage!!.widthProperty())
					prefHeightProperty().bind(primaryStage!!.heightProperty())
				})
			}
			
			updateScene()
		}
		
		internal fun showDialog(dialog: Dialog): Optional<ButtonType> =
			DialogBuilder.build(dialog).showAndWait().map { it.toButtonType() }
		
		internal fun showFileDialog(dialog: FileDialog): Optional<List<File>> =
			Optional.ofNullable(
				when (dialog.mode) {
					OPEN_FILE ->
						listOf(FileChooserBuilder.buildFileChooser(dialog).showOpenDialog(primaryStage))
					OPEN_MULTIPLE_FILES ->
						FileChooserBuilder.buildFileChooser(dialog).showOpenMultipleDialog(primaryStage)
					SAVE_FILE ->
						listOf(FileChooserBuilder.buildFileChooser(dialog).showSaveDialog(primaryStage))
					CHOOSE_DIRECTORY ->
						listOf(FileChooserBuilder.buildDirectoryChooser(dialog).showDialog(primaryStage))
				}
			)
		
		/**
		 * Starts the application.
		 */
		internal fun show() {
			Frontend().start()
		}
		
		/**
		 * Stops the application.
		 */
		internal fun exit() {
			Platform.exit()
		}
	}
}