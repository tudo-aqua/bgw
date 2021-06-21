package tools.aqua.bgw.builder

import javafx.event.EventHandler
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import tools.aqua.bgw.builder.DragDropHelper.Companion.tryFindDropTarget
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.DynamicView
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.StaticView
import tools.aqua.bgw.elements.container.GameElementContainerView
import tools.aqua.bgw.elements.gameelements.CardView
import tools.aqua.bgw.elements.gameelements.DiceView
import tools.aqua.bgw.elements.gameelements.GameElementView
import tools.aqua.bgw.elements.gameelements.TokenView
import tools.aqua.bgw.elements.layoutviews.ElementPane
import tools.aqua.bgw.elements.layoutviews.GridLayoutView
import tools.aqua.bgw.elements.layoutviews.LayoutElement
import tools.aqua.bgw.elements.uielements.*
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.event.Event
import tools.aqua.bgw.event.EventConverter.Companion.toKeyEvent
import tools.aqua.bgw.event.EventConverter.Companion.toMouseEvent
import tools.aqua.bgw.exception.IllegalInheritanceException
import tools.aqua.bgw.util.Coordinate

/**
 * NodeBuilder.
 * Factory for all BGW nodes.
 */
internal class NodeBuilder {
	companion object {
		//private const val TOLERANCE = 0.099
		
		/**
		 * Switches between top level element types.
		 */
		internal fun build(scene: Scene<out ElementView>, elementView: ElementView): Region {
			/*if (elementView.posX < -TOLERANCE
				|| elementView.posY < -TOLERANCE
				|| elementView.posX + elementView.width > scene.width + TOLERANCE
				|| elementView.posY + elementView.height > scene.height + TOLERANCE
			) {
				throw ElementOutOfSceneBoundsException(element = elementView, scene = scene)
			}*///Fixme
			
			//if(scene.elementsMap.containsKey(elementView))
			//	throw RuntimeException("Duplicate element") //TODO
			
			val node = when (elementView) {
				is GameElementContainerView<out GameElementView> ->
					buildContainer(scene, elementView)
				is GameElementView ->
					buildGameElement(elementView)
				is LayoutElement<out ElementView> ->
					buildLayoutElement(scene, elementView)
				is UIElementView ->
					buildUIElement(elementView)
				is StaticView<*> ->
					throw IllegalInheritanceException(elementView, StaticView::class.java)
				is DynamicView ->
					throw IllegalInheritanceException(elementView, DynamicView::class.java)
				else ->
					throw IllegalInheritanceException(elementView, ElementView::class.java)
			}
			val background = VisualBuilder.build(elementView)
			val stackPane = StackPane(background, node)
			
			//JavaFX -> Framework
			elementView.registerEvents(stackPane, node, scene)
			
			//Framework -> JavaFX
			elementView.registerObservers(stackPane, node, background)
			
			//Register in elementsMap
			scene.elementsMap[elementView] = stackPane
			
			return stackPane
		}
		
		
		/**
		 * Switches between Containers.
		 */
		private fun buildContainer(
			scene: Scene<out ElementView>,
			containerView: GameElementContainerView<out GameElementView>
		): Region = ContainerNodeBuilder.buildGameElementContainer(scene, containerView)
		
		/**
		 * Switches between GameElements.
		 */
		private fun buildGameElement(gameElementView: GameElementView): Region =
			when (gameElementView) {
				is CardView ->
					ElementNodeBuilder.buildCardView(gameElementView)
				is DiceView ->
					ElementNodeBuilder.buildDiceView(gameElementView)
				is TokenView ->
					ElementNodeBuilder.buildToken(gameElementView)
			}
		
		/**
		 * Switches between LayoutElements.
		 */
		private fun buildLayoutElement(
			scene: Scene<out ElementView>,
			layoutElementView: LayoutElement<out ElementView>
		): Region =
			when (layoutElementView) {
				is ElementPane ->
					LayoutNodeBuilder.buildElementPane(scene, layoutElementView)
				is GridLayoutView<*> ->
					LayoutNodeBuilder.buildGrid(scene, layoutElementView)
			}
		
		/**
		 * Switches between UIElements.
		 */
		private fun buildUIElement(uiElementView: UIElementView): Region =
			when (uiElementView) {
				is Button ->
					UINodeBuilder.buildButton(uiElementView)
				is CheckBox ->
					UINodeBuilder.buildCheckBox(uiElementView)
				is ComboBox<*> ->
					UINodeBuilder.buildComboBox(uiElementView)
				is Label ->
					UINodeBuilder.buildLabel(uiElementView)
				is ListView<*> ->
					UINodeBuilder.buildListView(uiElementView)
				is TableView<*> ->
					UINodeBuilder.buildTableView(uiElementView)
				is TextArea ->
					UINodeBuilder.buildTextArea(uiElementView)
				is ToggleButton ->
					UINodeBuilder.buildToggleButton(uiElementView)
				is ColorPicker ->
					UINodeBuilder.buildColorPicker(uiElementView)
				is ProgressBar ->
					UINodeBuilder.buildProgressBar(uiElementView)
			}
		
		private fun ElementView.registerEvents(stackPane: StackPane, node: Region, scene: Scene<*>) {
			var mouseStartCoord = Coordinate()
			var posStartCoord = Coordinate()
			var relativeParentRotation = 0.0
			
			stackPane.onDragDetected = EventHandler {
				if (this is DynamicView && isDraggable) {
					Frontend.draggedElement = this
					
					mouseStartCoord = Coordinate(
						xCoord = it.sceneX / Frontend.sceneScale,
						yCoord = it.sceneY / Frontend.sceneScale
					)
					posStartCoord = Coordinate(
						xCoord = stackPane.layoutX,
						yCoord = stackPane.layoutY
					)
					
					val pathToChild = scene.findPathToChild(this)
					
					//get dragged Object toFront globally
					pathToChild.forEach { t -> scene.elementsMap[t]?.toFront() } //TODO dont bring to front
					
					
					if (pathToChild.size > 1)
						relativeParentRotation = pathToChild.drop(1).sumOf { t -> t.rotation }
					
					stackPane.toFront()
					isDragged = true
					preDragGestureStarted?.invoke(DragEvent(this))
					onDragGestureStarted?.invoke(DragEvent(this))
					postDragGestureStarted?.invoke(DragEvent(this))
				}
			}
			stackPane.onMouseDragged = EventHandler {
				if (this is DynamicView && isDragged) {
					val rotated = Coordinate(
						xCoord = it.sceneX / Frontend.sceneScale - mouseStartCoord.xCoord,
						yCoord = it.sceneY / Frontend.sceneScale - mouseStartCoord.yCoord
					).rotated(-relativeParentRotation)
					
					stackPane.layoutX = posStartCoord.xCoord + rotated.xCoord
					stackPane.layoutY = posStartCoord.yCoord + rotated.yCoord
					
					onDragGestureMoved?.invoke(DragEvent(this))
				}
			}
			
			node.setOnMouseClicked { onMouseClicked?.invoke(it.toMouseEvent()) }
			node.setOnMousePressed { onMousePressed?.invoke(it.toMouseEvent()) }
			node.setOnMouseEntered { onMouseEntered?.invoke(Event()) }
			node.setOnMouseExited { onMouseExited?.invoke(Event()) }
			node.setOnMouseReleased {
				onMouseReleased?.invoke(it.toMouseEvent())
				if (this is DynamicView && isDragged) {
					Frontend.draggedElement = null
					isDragged = false
					
					if (!tryFindDropTarget(stackPane, scene, it.sceneX, it.sceneY)) {
						//TODO: Remove refresh all and only update drag source
						Frontend.boardGameScene?.rootElements?.notifyChange()
						Frontend.menuScene?.rootElements?.notifyChange()
					}
				}
			}
			
			node.setOnKeyPressed { onKeyPressed?.invoke(it.toKeyEvent()) }
			node.setOnKeyReleased { onKeyReleased?.invoke(it.toKeyEvent()) }
			node.setOnKeyTyped { onKeyTyped?.invoke(it.toKeyEvent()) }
		}
		
		@Suppress("DuplicatedCode")
		private fun ElementView.registerObservers(stackPane: StackPane, node: Region, background: Region) {
			posXProperty.setGUIListenerAndInvoke(posX) {
				stackPane.layoutX = it - if (layoutFromCenter) width / 2 else 0.0
			}
			posYProperty.setGUIListenerAndInvoke(posY) {
				stackPane.layoutY = it - if (layoutFromCenter) height / 2 else 0.0
			}
			
			rotationProperty.setGUIListenerAndInvoke(rotation) { stackPane.rotate = it }
			opacityProperty.setGUIListenerAndInvoke(opacity) { stackPane.opacity = it }
			
			heightProperty.setGUIListenerAndInvoke(height) {
				node.prefHeight = it
				background.prefHeight = it
			}
			widthProperty.setGUIListenerAndInvoke(width) {
				node.prefWidth = it
				background.prefWidth = it
			}
			
			isVisibleProperty.setGUIListenerAndInvoke(isVisible) {
				node.isVisible = it
				background.isVisible = it
			}
			isDisabledProperty.setGUIListenerAndInvoke(isDisabled) {
				node.isDisable = it
				background.isDisable = it
			}
			
			if (this is UIElementView) {
				backgroundStyleProperty.setGUIListenerAndInvoke(backgroundStyle) {
					if (it.isNotEmpty())
						background.style = it
				}
				componentStyleProperty.setGUIListenerAndInvoke(componentStyle) {
					if (it.isNotEmpty())
						node.style = it
				}
			}
		}
	}
}