/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

@file:Suppress("TooManyFunctions")

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
import tools.aqua.bgw.builder.FXConverters.Companion.toButtonType
import tools.aqua.bgw.builder.SceneBuilder.Companion.buildGame
import tools.aqua.bgw.builder.SceneBuilder.Companion.buildMenu
import tools.aqua.bgw.core.*
import tools.aqua.bgw.dialog.ButtonType
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.FileDialog
import tools.aqua.bgw.dialog.FileDialogMode.*
import tools.aqua.bgw.observable.properties.BooleanProperty
import tools.aqua.bgw.observable.properties.LimitedDoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.observable.properties.StringProperty
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
		
		application.onWindowShown?.invoke()
	}
	
	/**
	 * Called when the application closes.
	 */
	override fun stop() {
		application.onWindowClosed?.invoke()
	}
	
	/**
	 * Starts the application.
	 */
	internal fun start() {
		launch()
	}
	
	companion object {
		/**
		 * Current scene scale.
		 */
		internal var sceneScale: Double = 1.0
		
		/**
		 * [BoardGameApplication] instance.
		 */
		internal lateinit var application: BoardGameApplication
		
		/**
		 * Initial aspect ratio passed to [BoardGameApplication].
		 */
		internal lateinit var initialAspectRatio: AspectRatio
		
		/**
		 * Property for the window title.
		 */
		internal val titleProperty: StringProperty = StringProperty()
		
		/**
		 * Property whether application is currently maximized.
		 */
		internal val maximizedProperty = BooleanProperty(false)
		
		/**
		 * Property whether application is currently fullscreen.
		 */
		internal val fullscreenProperty = BooleanProperty(false)
		
		/**
		 * Property for the current application width.
		 */
		internal val widthProperty = LimitedDoubleProperty(
			lowerBoundInclusive = 0,
			upperBoundInclusive = Double.POSITIVE_INFINITY,
			initialValue = 0
		)
		
		/**
		 * Property for the current application height.
		 */
		internal val heightProperty = LimitedDoubleProperty(
			lowerBoundInclusive = 0,
			upperBoundInclusive = Double.POSITIVE_INFINITY,
			initialValue = 0
		)
		
		/**
		 * Property for the background [Visual].
		 */
		internal val backgroundProperty: Property<Visual> = Property(ColorVisual(Color.BLACK))
		//endregion
		
		//region Private attributes
		/**
		 * Current [BoardGameScene].
		 */
		private var boardGameScene: BoardGameScene? = null
		
		/**
		 * Current [MenuScene].
		 */
		private var menuScene: MenuScene? = null
		
		/**
		 * Main pane in current scene.
		 */
		private var scenePane: Pane = Pane()
		
		/**
		 * The game's root pane.
		 */
		private var gamePane: Pane? = null
		
		/**
		 * The menu's root pane.
		 */
		private var menuPane: Pane? = null
		
		/**
		 * Background pane.
		 */
		private var backgroundPane = Pane().apply { style = "-fx-background-color: black" }
		
		/**
		 * Current stage.
		 */
		private var primaryStage: Stage? = null
		
		/**
		 * Current vertical alignment for scenes.
		 */
		private var verticalSceneAlignment = VerticalAlignment.CENTER
		
		/**
		 * Current horizontal alignment for scenes.
		 */
		private var horizontalSceneAlignment = HorizontalAlignment.CENTER
		
		/**
		 * Current [ScaleMode].
		 */
		private var scaleMode = ScaleMode.FULL
		
		/**
		 * A small value to add to stage with to force a refresh.
		 */
		private var epsilon: Double = 1.0
		//endregion
		
		//region Internal functions
		/**
		 * Shows given [MenuScene]. If [BoardGameScene] is currently displayed, it gets deactivated and blurred.
		 *
		 * @param scene menu scene to show.
		 * @param fadeTime time to fade in, specified in milliseconds. Default: [DEFAULT_FADE_TIME].
		 */
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
		
		/**
		 * Hides currently shown [MenuScene]. Activates [BoardGameScene] if present.
		 *
		 * @param fadeTime time to fade out, specified in milliseconds. Default: [DEFAULT_FADE_TIME].
		 */
		internal fun hideMenuScene(fadeTime: Double) {
			fadeMenu(false, fadeTime)
		}
		
		/**
		 * Shows given [BoardGameScene].
		 *
		 * @param scene [BoardGameScene] to show.
		 */
		internal fun showGameScene(scene: BoardGameScene) {
			boardGameScene = scene
			
			scene.zoomDetailProperty.setGUIListenerAndInvoke(scene.zoomDetail) { _, _ ->
				if (primaryStage != null) {
					gamePane = buildGame(scene)
					
					updateScene()
				}
			}
		}
		
		/**
		 * Sets [HorizontalAlignment] of all [Scene]s in this [BoardGameApplication].
		 *
		 * @param newHorizontalAlignment new alignment to set.
		 */
		internal fun setHorizontalSceneAlignment(newHorizontalAlignment: HorizontalAlignment) {
			horizontalSceneAlignment = newHorizontalAlignment
			sizeChanged()
		}
		
		/**
		 * Sets [VerticalAlignment] of all [Scene]s in this [BoardGameApplication].
		 *
		 * @param newVerticalAlignment new alignment to set.
		 */
		internal fun setVerticalSceneAlignment(newVerticalAlignment: VerticalAlignment) {
			verticalSceneAlignment = newVerticalAlignment
			sizeChanged()
		}
		
		/**
		 * Sets [ScaleMode] of all [Scene]s in this [BoardGameApplication].
		 *
		 * @param newScaleMode new scale mode to set.
		 */
		internal fun setScaleMode(newScaleMode: ScaleMode) {
			scaleMode = newScaleMode
			sizeChanged()
		}
		
		/**
		 * Manually refreshes currently displayed [Scene]s.
		 */
		internal fun updateScene() {
			val activePanes: MutableList<Pane> = ArrayList(2)
			
			gamePane?.apply {
				widthProperty().addListener { _, _, _ -> primaryStage?.forceRefresh() }
				heightProperty().addListener { _, _, _ -> primaryStage?.forceRefresh() }
				
				activePanes.add(this)
			}
			
			menuPane?.apply {
				widthProperty().addListener { _, _, _ -> primaryStage?.forceRefresh() }
				heightProperty().addListener { _, _, _ -> primaryStage?.forceRefresh() }
				
				activePanes.add(this)
			}
			
			if (activePanes.size == 2) {
				gamePane?.effect = GaussianBlur(DEFAULT_BLUR_RADIUS)
				boardGameScene?.lock()
			}
			
			scenePane = Pane().apply {
				children.clear()
				children.add(backgroundPane)
				children.addAll(activePanes)
			}
			
			primaryStage?.scene = Scene(scenePane)
			
			primaryStage?.forceRefresh()
		}
		
		/**
		 * Returns pane associated to scene.
		 *
		 * @return [gamePane] for [boardGameScene], [menuPane] for [menuScene] and `null` for other parameters.
		 */
		internal fun tools.aqua.bgw.core.Scene<*>.mapToPane(): Pane =
			checkNotNull(when (this) {
				boardGameScene -> gamePane
				menuScene -> menuPane
				else -> null
			})
		
		/**
		 * Returns scene associated to pane.
		 *
		 * @return  [boardGameScene] for [gamePane], [menuScene] for [menuPane] and `null` for other parameters.
		 */
		private fun Pane.mapToScene(): tools.aqua.bgw.core.Scene<*>? =
			when (this) {
				gamePane -> boardGameScene
				menuPane -> menuScene
				else -> null
			}
		
		/**
		 * Starts the application.
		 *
		 * @param stage application stage.
		 */
		internal fun startApplication(stage: Stage) {
			val primaryStage = stage.apply {
				//Initialize default DECORATED stage style allowing minimizing
				initStyle(StageStyle.DECORATED)
				
				val monitorHeight = Toolkit.getDefaultToolkit().screenSize.height
				
				//Set dimensions according to screen resolution and aspect ratio or, fixed height and width
				height = if(heightProperty.value > 0) heightProperty.value else monitorHeight * DEFAULT_WINDOW_BORDER
				width = if(widthProperty.value > 0) widthProperty.value else height * initialAspectRatio.ratio
				
				//reflect new window size
				heightProperty.value = height
				widthProperty.value = width
				
				//Set internal listeners as GUI listeners would get invoked in setSilent in FX -> BGW direction
				maximizedProperty.setInternalListenerAndInvoke(maximizedProperty.value) { _, nV -> isMaximized = nV }
				fullscreenProperty.setInternalListenerAndInvoke(fullscreenProperty.value) { _, nV ->
					Platform.runLater { isFullScreen = nV }
				}
				
				widthProperty.internalListener =  { _, nV ->
					if (!isFullScreen && !isMaximized)
						width = nV
				}
				heightProperty.internalListener = { _, nV ->
					if (!isFullScreen && !isMaximized)
						height = nV
				}
				titleProperty.setGUIListenerAndInvoke(titleProperty.value) { _, nV -> title = nV }
				
				maximizedProperty().addListener { _, _, nV -> maximizedProperty.setSilent(nV) }
				fullScreenProperty().addListener { _, _, nV -> fullscreenProperty.setSilent(nV) }
				
				heightProperty().addListener { _, _, nV ->
					heightProperty.setSilent(nV.toDouble())
					sizeChanged()
				}
				widthProperty().addListener { _, _, nV ->
					widthProperty.setSilent(nV.toDouble())
					sizeChanged()
				}
				
				show()
			}
			
			menuScene?.let { menuPane = buildMenu(it) }
			boardGameScene?.let { gamePane = buildGame(it) }
			
			backgroundProperty.setGUIListenerAndInvoke(backgroundProperty.value) { _, nV ->
				backgroundPane.children.clear()
				backgroundPane.children.add(VisualBuilder.buildVisual(nV).apply {
					prefWidthProperty().bind(primaryStage.widthProperty())
					prefHeightProperty().bind(primaryStage.heightProperty())
				})
			}
			
			this.primaryStage = primaryStage
			updateScene()
		}
		
		/**
		 * Shows a dialog and blocks further thread execution.
		 *
		 * @param dialog the [Dialog] to show
		 *
		 * @return chosen button or [Optional.empty] if canceled.
		 */
		internal fun showDialog(dialog: Dialog): Optional<ButtonType> =
			DialogBuilder.build(dialog).showAndWait().map { it.toButtonType() }
		
		/**
		 * Shows the given [FileDialog].
		 *
		 * @param dialog the [FileDialog] to be shown.
		 *
		 * @return chosen file(s) or [Optional.empty] if canceled.
		 */
		internal fun showFileDialog(dialog: FileDialog): Optional<List<File>> =
			Optional.ofNullable(
				when (dialog.mode) {
					OPEN_FILE ->
						FileChooserBuilder.buildFileChooser(dialog).showOpenDialog(primaryStage)?.let { listOf(it) }
					OPEN_MULTIPLE_FILES ->
						FileChooserBuilder.buildFileChooser(dialog).showOpenMultipleDialog(primaryStage)
					SAVE_FILE ->
						FileChooserBuilder.buildFileChooser(dialog).showSaveDialog(primaryStage)?.let { listOf(it) }
					CHOOSE_DIRECTORY ->
						FileChooserBuilder.buildDirectoryChooser(dialog).showDialog(primaryStage)?.let { listOf(it) }
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
		//endregion
		
		//region Private functions
		/**
		 * Fades [menuPane] in or out according to parameter [fadeIn] in given amount of milliseconds [fadeTime].
		 *
		 * @param fadeIn `true` if menu should fade in, `false` if it should fade out.
		 * @param fadeTime time to fade in milliseconds.
		 */
		private fun fadeMenu(fadeIn: Boolean, fadeTime: Double) {
			menuPane?.apply {
				if (!fadeIn)
					menuPane = null
					
				FadeTransition(Duration.millis(fadeTime / 2), this).apply {
					fromValue = if (fadeIn) 0.0 else 1.0
					toValue = if (fadeIn) 1.0 else 0.0
					interpolator = Interpolator.EASE_OUT
					onFinished = EventHandler {
						if (!fadeIn) {
							boardGameScene?.unlock()
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
					KeyFrame(Duration.ZERO, KeyValue(value, if (fadeIn) 0 else DEFAULT_BLUR_RADIUS)),
					KeyFrame(Duration.millis(fadeTime), KeyValue(value, if (fadeIn) DEFAULT_BLUR_RADIUS else 0))
				).play()
			}
		}
		
		/**
		 * Forces the stage to refresh by alternately adding and removing a small value from stage width.
		 */
		private fun Stage.forceRefresh() {
			width += epsilon
			epsilon *= -1.0
		}
		
		/**
		 * Refreshes scene scale.
		 */
		private fun sizeChanged() {
			//Wait for renderer to finish resize nodes
			Platform.runLater {
				val activePanes: List<Pane> = listOfNotNull(gamePane, menuPane).ifEmpty { return@runLater }
				
				val sceneHeight = scenePane.height
				val sceneWidth = scenePane.width
				
				if (sceneHeight == 0.0 || sceneWidth == 0.0)
					return@runLater
				
				activePanes.forEach { pane ->
					pane.apply {
						val contentHeight = height
						val contentWidth = width
						
						if (contentHeight == 0.0 || contentWidth == 0.0)
							return@apply
						
						//Set new content layout
						layoutX = (sceneWidth - contentWidth) * horizontalSceneAlignment.positionMultiplier
						layoutY = (sceneHeight - contentHeight) * verticalSceneAlignment.positionMultiplier
						
						//Set new content scale
						if (scaleMode != ScaleMode.NO_SCALE) {
							sceneScale = min(sceneWidth / contentWidth, sceneHeight / contentHeight)
							
							if (scaleMode == ScaleMode.ONLY_SHRINK)
								sceneScale = min(sceneScale, 1.0)
							
							scaleX = sceneScale
							scaleY = sceneScale
							
							translateX =
								contentWidth / 2 * horizontalSceneAlignment.pivotMultiplier * (1 - sceneScale)
							translateY =
								contentHeight / 2 * verticalSceneAlignment.pivotMultiplier * (1 - sceneScale)
						}
						
						//Zoom detail
						mapToScene()?.apply {
							val scale = min(
								(width - zoomDetail.width) / width,
								(height - zoomDetail.height) / height
							)
							
							scaleX += scale
							scaleY += scale
							translateX -= zoomDetail.topLeft.xCoord
							translateY -= zoomDetail.topLeft.yCoord
						}
					}
				}
				
			}
		}
		//endregion
	}
}