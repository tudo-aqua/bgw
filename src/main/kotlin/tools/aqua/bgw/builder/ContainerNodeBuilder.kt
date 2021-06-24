package tools.aqua.bgw.builder

import javafx.scene.layout.Pane
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.container.GameElementContainerView

/**
 * ContainerNodeBuilder.
 * Factory for all BGW containers.
 */
internal class ContainerNodeBuilder {
	companion object {
		internal fun buildGameElementContainer(
			scene: Scene<out ElementView>,
			gameElementContainerView: GameElementContainerView<*>
		) = Pane().apply {
			gameElementContainerView.observableElements.setGUIListenerAndInvoke {
				refresh(
					scene,
					gameElementContainerView
				)
			}
		}
		
		private fun Pane.refresh(scene: Scene<out ElementView>, gameElementContainerView: GameElementContainerView<*>) {
			children.clear()
			gameElementContainerView.observableElements.forEach {
				children.add(NodeBuilder.build(scene, it))
			}
		}
	}
}