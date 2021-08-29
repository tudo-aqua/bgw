package tools.aqua.bgw.builder

import javafx.scene.layout.Pane
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.Scene

/**
 * This function is used in various places to increase the performance of rebuilding a [Pane].
 *
 * @param scene the scene that is responsible for the building of this [Pane].
 * @param components the [ComponentView]s that should make up this [Pane]s children.
 * @param cached the [ComponentView]s that currently make up this [Pane]s children.
 */
internal fun Pane.buildChildren(
	scene: Scene<out ComponentView>,
	components: Iterable<ComponentView>,
	cached: Set<ComponentView>
) {
	children.clear()
	(cached - components).forEach { scene.componentsMap.remove(it) }
	components.forEach {
		if (it in cached) {
			children.add(scene.componentsMap[it])
		} else {
			children.add(NodeBuilder.build(scene, it))
		}
	}
}