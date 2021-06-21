package tools.aqua.bgw.builder

import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.container.*
import tools.aqua.bgw.elements.gameelements.GameElementView

/**
 * ContainerNodeBuilder.
 * Factory for all BGW containers.
 */
internal class ContainerNodeBuilder {
	companion object {
		internal fun buildGameElementContainer(
			scene: Scene<out ElementView>,
			gameElementContainerView: GameElementContainerView<*>
		) =
			Pane().apply {
				gameElementContainerView.observableElements.guiListener = { refresh(scene, gameElementContainerView) }
				refresh(scene, gameElementContainerView)
			}
		
		private fun Pane.refresh(scene: Scene<out ElementView>, gameElementContainerView: GameElementContainerView<*>) {
			children.clear()
			gameElementContainerView.observableElements.forEach {
				children.add(NodeBuilder.build(scene, it))
			}
		}
		
		//---------------------------------------deprecated?
		
		internal fun buildGameElementArea(
			scene: Scene<out ElementView>,
			areaContainerView: AreaContainerView<*>
		): Region =
			Pane().apply {
				areaContainerView.observableElements.guiListener = { refreshAreaCards(scene, areaContainerView) }
				refreshAreaCards(scene, areaContainerView)
			}
		
		private fun Pane.refreshAreaCards(scene: Scene<out ElementView>, areaContainerView: AreaContainerView<*>) {
			children.clear()
			
			areaContainerView.getAllElements().forEach {
				children.add(NodeBuilder.build(scene, it))
			}
		}
		//endregion
		
		//region CardStack
		internal fun buildCardStack(scene: Scene<out ElementView>, containerView: CardStackView<*>): Region =
			StackPane().apply {
				containerView.observableElements.guiListener = { refreshCardStackCards(scene, containerView) }
				refreshCardStackCards(scene, containerView)
			}
		
		private fun StackPane.refreshCardStackCards(
			scene: Scene<out ElementView>,
			cardViewStackView: CardStackView<*>
		) {
			children.clear()
			cardViewStackView.observableElements.forEach {
				children.add(NodeBuilder.build(scene, it).apply {
					layoutXProperty().addListener { _, _, _ ->
						layoutX = 0.0
					}
					layoutYProperty().addListener { _, _, _ ->
						layoutY = 0.0
					}
					//Todo check for feedback loops with drag and drop or other features
				})
			}
		}
		//endregion
		
		//region GameElementPool
		internal fun buildGameElementPool(
			scene: Scene<out ElementView>,
			containerView: GameElementPoolView<out GameElementView>
		): Region =
			StackPane().apply {
				containerView.observableElements.guiListener = { refreshGameElementPool(scene, containerView) }
				refreshGameElementPool(scene, containerView)
			}
		
		@Suppress("UNUSED_PARAMETER")
		private fun StackPane.refreshGameElementPool(
			scene: Scene<out ElementView>,
			containerView: GameElementPoolView<*>
		) {
			//TODO("Not yet implemented.")
		}
		//endregion GameElementPool
		
		//region LinearLayout
		internal fun buildLinearLayout(scene: Scene<out ElementView>, containerView: LinearLayoutContainer<*>): Region =
			Pane().apply {
				containerView.observableElements.guiListener = { refreshLinearLayout(scene, containerView) }
				refreshLinearLayout(scene, containerView)
			}
		
		private fun Pane.refreshLinearLayout(
			scene: Scene<out ElementView>,
			linearLayoutContainer: LinearLayoutContainer<*>
		) {
			children.clear()
			linearLayoutContainer.observableElements.forEach {
				children.add(NodeBuilder.build(scene, it))
			}
		}
		//endregion
	}
}