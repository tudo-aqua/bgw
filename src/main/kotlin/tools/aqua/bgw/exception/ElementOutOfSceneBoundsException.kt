@file:Suppress("unused")

package tools.aqua.bgw.exception

import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView

/**
 * Gets raised when an [ElementView] is placed in a [Scene] outside of its bounds.
 */
@Deprecated("Not used any more.")
internal class ElementOutOfSceneBoundsException(element: ElementView, scene: Scene<*>) :
	RuntimeException(
		"Element $element is placed out of scene bounds: " +
				"PosX = ${element.posX}, element width = ${element.width}, scene width = ${scene.width}; " +
				"PosY = ${element.posY}, element height = ${element.height}, scene height = ${scene.height}"
	)