package tools.aqua.bgw.builder

import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.container.*
import tools.aqua.bgw.elements.gameelements.GameElementView

/**
 * ContainerNodeBuilder.
 * Factory for all BGW containers.
 */
@Suppress("UNUSED_PARAMETER")
internal class ContainerNodeBuilder {
	companion object {
		/**
		 * Switches between Containers.
		 */
		internal fun buildContainer(
			scene: Scene<out ElementView>,
			containerView: GameElementContainerView<out GameElementView>
		): Region =
			when (containerView) {
				is AreaContainerView -> buildAreaContainer(containerView)
				is CardStackView -> buildCardStack(containerView)
				is GameElementPoolView -> buildGameElementPool(containerView)
				is LinearLayoutContainer -> buildLinearLayout(containerView)
			}.apply {
				containerView.observableElements.setGUIListenerAndInvoke {
					refresh(
						scene,
						containerView
					)
				}
			}
		
		/**
		 * Builds [AreaContainerView].
		 */
		private fun buildAreaContainer(containerView: GameElementContainerView<*>): Pane = Pane()
		
		/**
		 * Builds [CardStackView].
		 */
		private fun buildCardStack(containerView: GameElementContainerView<*>): Pane = Pane()
		
		/**
		 * Builds [GameElementPoolView].
		 */
		private fun buildGameElementPool(containerView: GameElementContainerView<*>): Pane = Pane()
		
		/**
		 * Builds [LinearLayoutContainer].
		 */
		private fun buildLinearLayout(containerView: GameElementContainerView<*>): Pane = Pane()
		
		/**
		 * Refreshes children in this container.
		 */
		private fun Pane.refresh(scene: Scene<out ElementView>, gameElementContainerView: GameElementContainerView<*>) {
			children.clear()
			gameElementContainerView.observableElements.forEach {
				children.add(NodeBuilder.build(scene, it))
			}
		}
	}
}